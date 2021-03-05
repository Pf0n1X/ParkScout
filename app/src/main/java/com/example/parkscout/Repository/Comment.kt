package com.example.parkscout.Repository

import androidx.room.*
import java.time.format.DateTimeFormatter

@Entity(tableName = "comments",primaryKeys = arrayOf("trainingId","userId") )
data class Comment(
    @ColumnInfo(name = "trainingId")
    var trainingSpotId: String,
    @ColumnInfo(name = "userId")
    var userId: String,
    var c_text: String,
    @Embedded(prefix = "comment_") var c_dateTime: DateTimeFormatter?
){

}