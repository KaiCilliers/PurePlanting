package com.sunrisekcdeveloper.pureplanting.components.addEdit.models

import androidx.annotation.StringRes
import com.sunrisekcdeveloper.pureplanting.R

enum class PlantSize(
    @StringRes open val textResId: Int
) {
    Small(R.string.label_plant_size_small),
    Medium(R.string.label_plant_size_medium),
    Large(R.string.label_plant_size_large),
    XLarge(R.string.label_plant_size_xlarge),
}