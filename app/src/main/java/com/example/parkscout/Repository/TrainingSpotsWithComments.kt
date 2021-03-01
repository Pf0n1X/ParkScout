package com.example.parkscout.Repository
import androidx.room.Embedded
import androidx.room.Relation

data class TrainingSpotsWithComments(
    @Embedded val trainingSpot: TrainingSpot,
    @Relation(
        parentColumn = "parkId",
        entityColumn = "trainingSpotId"
    )
    val comments: List<Comment>? = null

){
    @JvmName("getComments1")
    fun getComments(): List<Comment>? {
        return comments
    }
}
