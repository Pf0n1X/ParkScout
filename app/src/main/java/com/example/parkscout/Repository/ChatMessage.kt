package com.example.parkscout.Repository

import android.R.attr.name
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.FieldValue
import java.sql.Timestamp


@Entity(tableName = "chat_message")
data class ChatMessage(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,
    var sender: String,
    var receiver: String,
    var message: String,
    var lastUpdated: Long
) {
    fun fromMap(map: Map<String?, Any?>) {
        id = (map["id"] as String?)!!;
        sender = map["sender"] as String;
        receiver = map["receiver"] as String;
        message = map["message"] as String;
        var ts: Timestamp = Timestamp(map["lastUpdated"] as Long);
        lastUpdated = ts.seconds as Long;
    }

    fun toMap(): Map<String, Any>? {
        val result: HashMap<String, Any> = HashMap()
        result["id"] = id
        result["sender"] = sender
        result["receiver"] = receiver
        result["message"] = message
        result["lastUpdated"] = FieldValue.serverTimestamp()

        return result
    }
}