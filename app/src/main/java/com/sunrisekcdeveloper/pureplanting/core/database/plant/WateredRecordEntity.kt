package com.sunrisekcdeveloper.pureplanting.library.db_tables.plant

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * Foreign Key is used to enforce valid relationships
 * between entities when modifying the database
 */
@Entity(
    foreignKeys = [ForeignKey(
        entity = PlantEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("plantId"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
    )]
)
data class WateredRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val plantId: String,
    val wateredAt: LocalDateTime,
)