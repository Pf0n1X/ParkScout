package com.example.parkscout.Repository

import androidx.room.Embedded
import androidx.room.Relation

data class TrainingSpotWithImages(
    @Embedded
    var trainingSpot: TrainingSpot,
    @Relation(parentColumn = "parkId", entityColumn = "trainingSpotId", entity = Images::class)
    var images: List<Images>? = null
){
    @JvmName("getImages1")
    fun getImages(): List<Images>? {
        return images
    }
}