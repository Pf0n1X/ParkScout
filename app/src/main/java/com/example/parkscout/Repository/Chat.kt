package com.example.parkscout.Repository

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(tableName = "chat"
//    ,
//    foreignKeys = [ForeignKey(entity = TrainingSpot::class,
//        parentColumns = ["parkId"],
//        childColumns = ["training_spot_id"],
//        onDelete = CASCADE)]
)
data class Chat(
    @PrimaryKey
    @ColumnInfo(name = "chatId")
    var chatId: String,
    var training_spot_id: String
){
    fun fromMap(map: Map<String?, Any?>) {
        chatId = (map["id"] as String?)!!;
        training_spot_id = map["training_spot_id"] as String;
    }
    fun toMap(): Map<String, Any> {
        val result: HashMap<String, Any> = HashMap()
        result["chatId"] = chatId
        result["training_spot_id"] = training_spot_id;

        return result
    }

}