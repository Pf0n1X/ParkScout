package com.example.parkscout.Repository

import androidx.room.*
import java.util.*

@Entity(tableName = "training_spot")
data class TrainingSpot(
    @PrimaryKey
    @ColumnInfo(name = "parkId")
    var parkId: String = UUID.randomUUID().toString(),
    var parkName: String,
    @Embedded var parkLocation: com.example.parkscout.data.Types.Location,
    var chatId: String,
    var facilities : String
//    var ImgUrl:
){
    fun fromMap(map: Map<String?, Any?>) {
        parkId = (map["parkId"] as String?)!!;
        parkName = map["parkName"] as String;
        parkLocation = map["parkLocation"] as com.example.parkscout.data.Types.Location;
//        comments = map["comments"] as Comment;
//        ratings = map["ratings"] as Rating;
  //      sportTypes = map["sportTypes"] as MutableList<SportTypes>;
        chatId = map["chatId"] as String;
        facilities = map["facilities"] as String;
    }

    fun toMap(): Map<String, Any> {
        val result: HashMap<String, Any> = HashMap()
        result["parkId"] = parkId
        result["parkName"] = parkName
        result["parkLocation"] = parkLocation
//        result["comments"] = comments
//        result["ratings"] = ratings
    //    result["sportTypes"] = sportTypes
        result["chatId"] = chatId
        result["facilities"] = facilities

        return result
    }

    @JvmName("getParkId1")
    fun getParkId(): String {
        return parkId
    }

//    fun getTypeName(): String {
//        return type_name
//    }
//
//    fun setTypeId(typeId: String) {
//        type_id = typeId
//    }
}