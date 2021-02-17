package com.example.parkscout.Repository

import androidx.room.Embedded
import androidx.room.Relation
import com.example.parkscout.data.model.SportType

data class TrainingSpot_SportTypes (
    @Embedded val trainingSpot : TrainingSpot,
    @Relation(
        parentColumn = "sportTypes",
        entityColumn = "type_id"
    )
    val sport_types: List<SportType>
)