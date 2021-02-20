package com.example.parkscout.Repository

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.DateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "rating",primaryKeys = ["user_Id","trainingSpotId"])
data class Rating (
    @ColumnInfo(name = "user_Id")
    val user_Id : String,
    @ColumnInfo(name = "trainingSpotId")
    val trainingSpotId : String,
    val rate : Int,
    @Embedded val rate_dateTime : DateTimeFormatter
)