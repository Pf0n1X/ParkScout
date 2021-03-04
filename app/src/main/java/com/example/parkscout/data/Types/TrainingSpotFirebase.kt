package com.example.parkscout.data.Types


import com.example.parkscout.Repository.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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
        chatId = map["chatId"] as String;
        facilities = map["facilities"] as String;
        comment = map["comment"]  as List<Comment>?;
        ratings = map["ratings"]  as List<Rating>?;

        //types
        if(map["types"] != null) {
            var type: SportTypes = SportTypes("", "", "");
            var typesList = LinkedList<SportTypes>();
            var arr_type: ArrayList<HashMap<String, Object>> =
                map["types"] as ArrayList<HashMap<String, Object>>;
            arr_type.forEachIndexed { index, hashMap ->
                type.type_name = hashMap["type_name"] as String;
                type.type_id = hashMap["type_id"] as String;
                type.park_Id = hashMap["park_Id"] as String;


                typesList?.add(type);
            }
            types = typesList.toList();
        }
        // images
        if(map["images"] != null) {
            var img: Images = Images("", "");
            var imgList = LinkedList<Images>();
            var arr_img: ArrayList<HashMap<String, Object>> =
                map["images"] as ArrayList<HashMap<String, Object>>;
            arr_img.forEachIndexed { index, hashMap ->
                img.trainingSpotId = hashMap["trainingSpotId"] as String;
                img.ImgUrl = hashMap["imgUrl"] as String;


                imgList?.add(img);
            }
            images = imgList.toList();
        }
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
        result["types"] = types as  List<SportTypes?>?
        result["images"] = images as List<Images?>?

        return result
    }

}
