package com.sunrisekcdeveloper.plantList

import com.sunrisekcdeveloper.android.toParcelable
import com.sunrisekcdeveloper.notification.NotificationCache
import com.sunrisekcdeveloper.plant.Plant
import com.sunrisekcdeveloper.plant.PlantCache
import com.zhuinden.simplestack.Bundleable
import com.zhuinden.statebundle.StateBundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDateTime

class PlantsViewModel(
    private val plantCache: PlantCache,
    notificationCache: NotificationCache,
    private val router: PlantsRouter,
    private val clock: Clock = Clock.systemDefaultZone(),
) : Bundleable {
    private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
    private var lastRemovedPlant: Plant? = null

    val activeFilter = MutableStateFlow(PlantListFilter.UPCOMING)

    val hasUnreadNotifications = notificationCache
        .observe()
        .onEach { println("all notifications are: ${it.map { it.seen }}") }
        .map { allNotifications -> allNotifications.any { !it.seen } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), false)

    val plants: StateFlow<List<Plant>> = combine(plantCache.observe(), activeFilter) { plants, filter ->
        println("plants size is ${plants.size}")
        println("filter is ${filter}")
        val res = plants.filter { plant ->
            when (filter) {
                PlantListFilter.UPCOMING -> plant.needsWater(LocalDateTime.now(clock))
                PlantListFilter.FORGOT_TO_WATER -> plant.forgotToWater(LocalDateTime.now(clock))
                PlantListFilter.HISTORY -> plant.dateLastWatered != null
            }
        }
       val sorted =  when (filter) {
            PlantListFilter.UPCOMING -> res.sortedBy { it.wateringInfo.time }
            PlantListFilter.FORGOT_TO_WATER -> res.sortedBy { it.wateringInfo.time }
            PlantListFilter.HISTORY -> res.sortedByDescending { it.dateLastWatered ?: LocalDateTime.now() }
        }
        println("new emission size is ${sorted.map { it.dateLastWatered }}")
        sorted
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), emptyList())

    fun waterPlant(plant: Plant) = viewModelScope.launch {
        plantCache.save(plant.water())
    }

    fun undoWaterPlant(plant: Plant) = viewModelScope.launch {
        plantCache.save(plant.undoLastWatering())
    }

    fun setFilter(filter: PlantListFilter) = viewModelScope.launch {
        activeFilter.update { filter }
    }

    fun removePlant(plant: Plant) = viewModelScope.launch {
        lastRemovedPlant = plant
        plantCache.remove(plant.id)
    }

    fun undoRemove(plantId: String) = viewModelScope.launch {
        plantCache.save(lastRemovedPlant!!)
    }

    fun navigateToNotification() {
        router.goToNotificationList()
    }

    fun navigateToAddPlant() {
        router.goToAddPlant()
    }

    fun navigateToPlantDetail(plant: Plant) {
        router.goToPlantDetail(plant.toParcelable())
    }

    override fun toBundle(): StateBundle = StateBundle().apply {
        putString("activeFilter", activeFilter.value.toString())
    }

    override fun fromBundle(bundle: StateBundle?) {
        bundle?.run {
            activeFilter.update { PlantListFilter.valueOf(getString("activeFilter", PlantListFilter.UPCOMING.toString())) }
        }
    }
}

