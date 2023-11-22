package com.sunrisekcdeveloper.pureplanting.features.component.plants

fun Plant.toWaterRecordsEntity(): List<WateredRecordEntity> {
    return wateringInfo.datesWatered.map {
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
        lastModified = userLastModifiedDate,
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
            datesWatered = waterRecords.map { it.wateredAt }
        ),
        userLastModifiedDate = plant.lastModified,
    )
}