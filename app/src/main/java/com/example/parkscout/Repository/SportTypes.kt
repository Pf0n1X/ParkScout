package com.example.parkscout.Repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sport_types")
data class SportTypes (
    @PrimaryKey
    @ColumnInfo(name = "type_id")
    val type_id : String,
    val type_name : String
)