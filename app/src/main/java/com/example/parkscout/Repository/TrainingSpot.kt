package com.example.parkscout.Repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.parkscout.data.model.Comment
import com.example.parkscout.data.model.Location
import com.example.parkscout.data.model.Rating
import com.example.parkscout.data.model.SportType
import java.util.*

@Entity(tableName = "training_spot")
data class TrainingSpot(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val parkId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "parkName")
    val parkName : String = ""
//    @ColumnInfo(name = "parkLocation")
//    val parkLocation: Location ,
//    @ColumnInfo(name = "comments")
//    val comments : Array<Comment>,
//    @ColumnInfo(name = "ratings")
//    val ratings : Array<Rating>,
//    @ColumnInfo(name = "sportTypes")
//    val sportTypes : Array<SportType>,
//    @ColumnInfo(name = "chatId")
//    val chatId : String = ""
){

}