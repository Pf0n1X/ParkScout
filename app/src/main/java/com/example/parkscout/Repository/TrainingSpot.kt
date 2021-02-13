package com.example.parkscout.Repository

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.parkscout.data.model.Comment
import com.example.parkscout.data.model.Location
import com.example.parkscout.data.model.Rating
import com.example.parkscout.data.model.SportType

@Entity(tableName = "training_spot")
data class TrainingSpot(
    @PrimaryKey
    val parkId: String,
    val parkName : String,
    val parkLocation: Location,
    val comments : Array<Comment>,
    val ratings : Array<Rating>,
    val sportTypes : Array<SportType>,
    val chatId : String
){

}