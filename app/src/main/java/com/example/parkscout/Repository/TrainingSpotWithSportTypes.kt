package com.example.parkscout.Repository

import androidx.room.Embedded
import androidx.room.Relation

data class TrainingSpotWithSportTypes(
    @Embedded
    var trainingSpot: TrainingSpot,
    @Relation(parentColumn = "parkId", entityColumn = "park_Id", entity = SportTypes::class)
    var sport_types: List<SportTypes>? = null

){
    fun TrainingSpotWithTypes(park: TrainingSpot, types: List<SportTypes?>) {
        trainingSpot = park
        sport_types = types as List<SportTypes>
    }
    fun getTypes(): List<SportTypes>? {
        return sport_types
    }
    fun getTariningSpot():TrainingSpot{
        return trainingSpot
    }
}
