package com.sunrisekcdeveloper.pureplanting.domain.plant

import com.sunrisekcdeveloper.pureplanting.library.db_tables.plant.PlantEntity
import com.sunrisekcdeveloper.pureplanting.library.db_tables.plant.PlantWithWaterRecords
import com.sunrisekcdeveloper.pureplanting.library.db_tables.plant.WateredRecordEntity

fun Plant.toWaterRecordsEntity(): List<WateredRecordEntity> {
    return wateringInfo.history.map {
        WateredRecordEntity(
            plantId = id,
            wateredAt = it
        )
    }
}

fun Plant.toEntity(): PlantEntity {
    return PlantEntity(
        id = id,
        name = details.name,
        size = details.size,
        description = details.description,
        imageSrcUri = details.imageSrcUri,
        timeToWater = wateringInfo.time,
        daysToWater = wateringInfo.days,
        amountToWaterWith = wateringInfo.amount,
        createdAt = wateringInfo.lastModifiedWateringDays,
    )
}

fun PlantWithWaterRecords.toPlant(): Plant {
    return Plant(
        id = plant.id,
        details = PlantDetails(
            name = plant.name,
            size = plant.size,
            description = plant.description,
            imageSrcUri = plant.imageSrcUri,
        ),
        wateringInfo = WateringInfo(
            time = plant.timeToWater,
            days = plant.daysToWater,
            amount = plant.amountToWaterWith,
            history = waterRecords.map { it.wateredAt },
            lastModifiedWateringDays = plant.createdAt,
        ),
        createdAt = plant.createdAt,
    )
}