package com.example.parkscout.data.model

data class TrainingSpot(
    val parkId: String,
    val parkName : String,
    val parkLocation: Location,
    val comments : Array<Comment>,
    val ratings : Array<Rating>,
    val sportTypes : Array<SportType>,
    val chatId : String
)