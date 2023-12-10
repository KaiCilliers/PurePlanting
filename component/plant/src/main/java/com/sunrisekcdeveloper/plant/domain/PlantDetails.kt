package com.sunrisekcdeveloper.plant.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlantDetails(
    val name: String,
    val size: String,
    val description: String,
    val imageSrcUri: String,
): Parcelable