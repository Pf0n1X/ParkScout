package com.example.parkscout.Repository

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.DateTime

@Entity(tableName = "rating")
data class Rating (
    @PrimaryKey
    @ColumnInfo(name = "user_Id")
    val user_Id : String,
    @PrimaryKey
    @ColumnInfo(name = "trainingSpotId")
    val trainingSpotId : String,
    val rate : Int,
    @Embedded val rate_dateTime : DateTime
)