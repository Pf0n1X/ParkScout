package com.example.parkscout.data.Types


import com.example.parkscout.Repository.*
import java.util.*

data class TrainingSpotFirebase(
    var parkId: String = UUID.randomUUID().toString(),
    var parkName: String,
    var parkLocation: Location,
    var chatId: String,
    var facilities: String,
    var comment: List<Comment>?,
    var ratings: List<Rating>?,
    var types: List<SportTypes>?,
    var images: List<Images>?
){
    fun fromMap(map: Map<String?, Any?>) {
        parkId = (map["parkId"] as String?)!!;
        parkName = map["parkName"] as String;
       var location = map.get("parkLocation") as Map<String, *>
        parkLocation = Location(location["xscale"] as Double, location["yscale"] as Double)
//        parkLocation =
//        parkLocation = map["parkLocation"] as Location;
        chatId = map["chatId"] as String;
        facilities = map["facilities"] as String;
        comment = map["comment"]  as List<Comment>?;
        ratings = map["ratings"]  as List<Rating>?;
        types = map["types"]  as List<SportTypes>?;
        images = map["images"]  as List<Images>?;
    }

    fun toMap(): Map<String, Any? > {
        val result: HashMap<String, Any?> = HashMap()
        result["parkId"] = parkId
        result["parkName"] = parkName
        result["parkLocation"] = parkLocation
        result["chatId"] = chatId
        result["facilities"] = facilities
        result["comment"] = comment as List<Comment?>?
        result["ratings"] = ratings as List<Rating?>?
        result["types"] = types as List<SportTypes?>?
        result["images"] = images as List<Images?>?

        return result
    }

}
