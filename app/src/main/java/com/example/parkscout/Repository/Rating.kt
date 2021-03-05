package com.example.parkscout.Repository

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.type.DateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "rating",primaryKeys = ["user_Id","trainingSpotId"])
data class Rating (
    @ColumnInfo(name = "user_Id")
    var user_Id : String,
    @ColumnInfo(name = "trainingSpotId")
    var trainingSpotId : String,
    var rate : Float,
    var rate_dateTime : Long
) {
    fun fromMap(map: Map<String?, Any?>) {
        trainingSpotId = (map["trainingSpotId"] as String?)!!;
        user_Id = map["user_Id"] as String;
        var rateDpuble = map["rate"] as Double;
        rate = rateDpuble.toFloat();
        rate_dateTime = map["rate_dateTime"] as Long;
    }

    fun toMap(): Map<String, Any> {
        val result: HashMap<String, Any> = HashMap()
        result["trainingSpotId"] = trainingSpotId;
        result["user_Id"] = user_Id;
        result["rate"] = rate
        result["rate_dateTime"] = Timestamp.now()

        return result
    }
}