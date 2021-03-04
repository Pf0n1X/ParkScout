package com.example.parkscout.Repository

import androidx.room.*
import com.google.firebase.Timestamp
import java.time.format.DateTimeFormatter

@Entity(tableName = "comments",primaryKeys = arrayOf("trainingId","userId") )
data class Comment(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,
    @ColumnInfo(name = "trainingId")
    var trainingSpotId: String,
    @ColumnInfo(name = "userId")
    var userId: String,
    var c_text: String,
    @Embedded(prefix = "comment_")
    var time: Long
) {
    fun fromMap(map: Map<String?, Any?>) {
        id = (map["id"] as String?)!!;
        trainingSpotId = (map["trainingSpotId"] as String?)!!;
        userId = map["userId"] as String;
        c_text = map["c_text"] as String;
        var ts: Timestamp = map["time"] as Timestamp;
        time = ts.seconds as Long;
    }

    fun toMap(): Map<String, Any> {
        val result: HashMap<String, Any> = HashMap()
        result["id"] = id
        result["trainingSpotId"] = trainingSpotId;
        result["userId"] = userId;
        result["c_text"] = c_text
        result["time"] = Timestamp.now()

        return result
    }
}