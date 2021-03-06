package com.example.parkscout.Repository

import android.R.attr.name
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.FieldValue
import com.google.firebase.Timestamp


@Entity(tableName = "chat_message")
data class ChatMessage(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,
    var chatId: String,
    var sender: String,
    var message: String,
    var last_updated: Long
) {
    fun fromMap(map: Map<String?, Any?>) {
        id = (map["id"] as String?)!!;
        chatId = (map["chatId"] as String?)!!;
        sender = map["sender"] as String;
        message = map["message"] as String;
        var ts: Timestamp = map["last_updated"] as Timestamp;
        last_updated = ts.seconds as Long;
    }

    fun toMap(): Map<String, Any> {
        val result: HashMap<String, Any> = HashMap()
        result["id"] = id
        result["chatId"] = chatId;
        result["sender"] = sender
        result["message"] = message
        result["last_updated"] = Timestamp.now()

        return result
    }
}