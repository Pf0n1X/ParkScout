package com.example.parkscout.Repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class Images (
    @PrimaryKey
    @ColumnInfo(name = "trainingSpotId")
    var trainingSpotId : String,
    var ImgUrl: String

)