package com.example.parkscout.Repository

import androidx.room.Embedded
import androidx.room.Relation

data class TrainingSpotWithRating(
    @Embedded
    var trainingSpot: TrainingSpot,
    @Relation(parentColumn = "parkId", entityColumn = "trainingSpotId", entity = Rating::class)
    var sport_rating: List<Rating>? = null
){
    fun getRating(): List<Rating>? {
        return sport_rating
    }

}