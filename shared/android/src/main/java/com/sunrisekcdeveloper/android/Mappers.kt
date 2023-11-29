package com.sunrisekcdeveloper.android

import com.sunrisekcdeveloper.plant.Plant
import com.sunrisekcdeveloper.plant.PlantDetails
import com.sunrisekcdeveloper.plant.WateringInfo

fun ParcelablePlant.toDomain(): Plant {
    return Plant(
        id = id,
        details = PlantDetails(name = name, size = size, description = description, imageSrcUri = imageSrcUri),
        wateringInfo = WateringInfo(time = time, days = days, amount = amount, datesWatered = datesWatered),
        userLastModifiedDate = userLastModifiedDate,
    )
}

fun Plant.toParcelable(): ParcelablePlant {
    return ParcelablePlant(
        id = id,
        name = details.name,
        size = details.size,
        description = details.description,
        imageSrcUri = details.imageSrcUri,
        time = wateringInfo.time,
        days = wateringInfo.days,
        amount = wateringInfo.amount,
        datesWatered = wateringInfo.datesWatered,
        userLastModifiedDate = userLastModifiedDate,
    )
}