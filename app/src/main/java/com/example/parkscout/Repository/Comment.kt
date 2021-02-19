package com.example.parkscout.Repository

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.DateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "comments" , primaryKeys = ["trainingId","userId" ])
data class Comment (
    @ColumnInfo(name = "trainingId")
    val trainingSpotId : String,
    @ColumnInfo(name = "userId")
    val userId : String,
    val c_text: String,
    @Embedded(prefix = "comment_") val c_dateTime : DateTimeFormatter
){

}