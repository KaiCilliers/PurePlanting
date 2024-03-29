package com.sunrisekcdeveloper.pureplanting.domain.plant

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlantDetails(
    val name: String,
    val size: String,
    val description: String,
    val imageSrcUri: String, // todo can be null due to image not being captured
): Parcelable