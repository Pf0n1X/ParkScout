package com.example.parkscout.Repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

////@Entity(tableName = "sport_types" , primaryKeys = arrayOf("type_id","park_Id"))
@Entity(tableName = "sport_types" )
data class SportTypes(
    @ColumnInfo(name = "type_id")
    @PrimaryKey
    var type_id: String ,
    var type_name: String,
    @ForeignKey
        (entity = TrainingSpot::class,
            parentColumns = ["parkId"],
        childColumns = ["park_Id"])
    var park_Id :String
){

    fun getTypeId(): String {
        return type_id
    }

    fun getTypeName(): String {
        return type_name
    }
    fun getParkId(): String {
        return park_Id
    }
    fun setTypeId(typeId:String){
        type_id = typeId
    }
    fun setParkId(parkId:String){
        park_Id = parkId
    }
    fun fromMap(map: Map<String?, Any?>) {
        type_id = map["type_id"] as String;
        type_name = map["type_name"] as String;
        park_Id = map["park_Id"] as String;
    }

    fun toMap(): Map<String, Any> {
        val result: HashMap<String, Any> = HashMap()
        result["type_id"] = type_id
        result["type_name"] = type_name
        result["park_Id"] = park_Id

        return result
    }
}