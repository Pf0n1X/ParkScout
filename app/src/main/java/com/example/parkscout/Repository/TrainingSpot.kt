package com.example.parkscout.Repository

import androidx.room.*
import com.example.parkscout.data.Types.Location
import java.util.*

@Entity(tableName = "training_spot")
data class TrainingSpot(
    @PrimaryKey
    @ColumnInfo(name = "parkId")
    var parkId: String = UUID.randomUUID().toString(),
    var parkName: String,
    @Embedded var parkLocation: Location,
    var chatId: String,
    var facilities : String
){

    @JvmName("getParkId1")
    fun getParkId(): String {
        return parkId
    }
}