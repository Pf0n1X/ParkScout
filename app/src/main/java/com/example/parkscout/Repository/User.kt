package com.example.parkscout.Repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey
    @ColumnInfo(name = "uid")
    var uId: String,
    var name: String,
    var profilePic: String,
    var distance: Int
){

    // Constructors
    constructor() : this("", "", "", 0);

    // Methods
    fun fromMap(map: Map<String?, Any?>) {
        uId = (map["uid"] as String?)!!;
        name = map["name"] as String;
        profilePic = map["profilePic"] as String;
        distance = map["distance"] as Int;
    }

    fun toMap(): Map<String, Any> {
        val result: HashMap<String, Any> = HashMap()
        result["uid"] = uId
        result["name"] = name;
        result["profilePic"] = profilePic;
        result["distance"] = distance;
        return result
    }
}
