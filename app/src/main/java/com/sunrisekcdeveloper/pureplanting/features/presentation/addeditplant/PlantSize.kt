package com.sunrisekcdeveloper.pureplanting.features.presentation.addeditplant

import androidx.annotation.StringRes
import com.sunrisekcdeveloper.pureplanting.R

sealed class PlantSize(
    @StringRes open val textResId: Int
) {
    data class Small(override val textResId: Int = R.string.label_plant_size_small) : PlantSize(textResId)
    data class Medium(override val textResId: Int = R.string.label_plant_size_medium) : PlantSize(textResId)
    data class Large(override val textResId: Int = R.string.label_plant_size_large) : PlantSize(textResId)
    data class XLarge(override val textResId: Int = R.string.label_plant_size_xlarge) : PlantSize(textResId)
}