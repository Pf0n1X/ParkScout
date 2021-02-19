package com.example.parkscout.Repository
import androidx.room.Embedded
import androidx.room.Relation

data class TrainingSpotsComments (
    @Embedded val trainingSpot : TrainingSpot,
    @Relation(
        parentColumn = "parkId",
        entityColumn = "trainingSpotId"
    )
    val comments: List<Comment>

)
