package com.sunrisekcdeveloper.plant.data

import androidx.room.Embedded
import androidx.room.Relation

/**
 * This performs the opposite of the foreign key
 * whereas this enforces relationships by
 * returning these values together based on
 * the ID when queried
 */
data class PlantWithWaterRecords(
    @Embedded val plant: PlantEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "plantId"
    )
    val waterRecords: List<WateredRecordEntity>
)