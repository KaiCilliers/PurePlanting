package com.sunrisekcdeveloper.pureplanting.features

import androidx.fragment.app.Fragment
import com.sunrisekcdeveloper.addEdit.AddEditComponent
import com.sunrisekcdeveloper.addEdit.Router
import com.sunrisekcdeveloper.navigation.FragmentKey
import com.sunrisekcdeveloper.plant.domain.Plant
import com.zhuinden.simplestack.ServiceBinder
import com.zhuinden.simplestackextensions.servicesktx.add
import com.zhuinden.simplestackextensions.servicesktx.lookup
import com.zhuinden.simplestackextensions.servicesktx.rebind
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddEditKey(
    val plant: Plant? = null
) : FragmentKey() {
    override fun bindServices(serviceBinder: ServiceBinder) {
        with(serviceBinder) {
            AddEditComponent.Default(
                plantRepository = lookup(),
                router = object : Router {
                    override fun jumpToRoot() {
                        backstack.jumpToRoot()
                    }
                },
                plant = plant
            ).let {  component ->
                add(component)
                rebind<AddEditComponent>(component)
            }
        }
    }

    override fun instantiateFragment(): Fragment = AddEditPlantFragment()
}