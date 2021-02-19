package com.example.parkscout.Repository

import androidx.room.Embedded
import androidx.room.Relation
import com.example.parkscout.data.model.Rating

data class TrainingSpotRates (
    @Embedded val trainingSpot : TrainingSpot,
    @Relation(
        parentColumn = "parkId",
        entityColumn = "trainingSpotId"
    )
    val rate: List<Rating>

    )