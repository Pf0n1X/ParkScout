package com.example.parkscout.Repository

import androidx.room.*
import java.time.format.DateTimeFormatter

@Entity(tableName = "comments",primaryKeys = arrayOf("trainingId","userId") )
//@Entity(tableName = "comments" ,
//    foreignKeys = arrayOf(ForeignKey(entity = TrainingSpot::class,
//    parentColumns = arrayOf("parkId"),
//    childColumns = arrayOf("trainingId"),
//    onDelete = ForeignKey.CASCADE),
//    ForeignKey(entity = User::class,parentColumns = arrayOf("uId"),
//        childColumns = arrayOf("userId"),onDelete =ForeignKey.CASCADE)))
data class Comment(
    @ColumnInfo(name = "trainingId")
    val trainingSpotId: String,
    @ColumnInfo(name = "userId")
    val userId: String,
    val c_text: String,
    @Embedded(prefix = "comment_") val c_dateTime: DateTimeFormatter?
){
//    companion object {
//        public var instance: Comment = Comment("","","",null)
//            public get
//            private set;
//    }

}