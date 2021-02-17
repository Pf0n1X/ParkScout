package com.example.parkscout.Repository

import androidx.room.*
import com.example.parkscout.data.model.Location
import com.example.parkscout.data.model.Rating
import com.example.parkscout.data.model.SportType
import java.util.*

@Entity(tableName = "training_spot")
data class TrainingSpot(
    @PrimaryKey
    @ColumnInfo(name = "parkId")
    val parkId: String = UUID.randomUUID().toString(),
    val parkName : String,
    @Embedded val parkLocation: Location,
    @Embedded val comments : Comment,
    @Embedded val ratings : Rating,
    @Embedded val sportTypes : SportType,
    val chatId : String
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TrainingSpot

        if (parkId != other.parkId) return false
        if (parkName != other.parkName) return false
        if (parkLocation != other.parkLocation) return false
        if (comments != other.comments) return false
        if (ratings != other.ratings) return false
        if (sportTypes != other.sportTypes) return false
        if (chatId != other.chatId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = parkId.hashCode()
        result = 31 * result + parkName.hashCode()
        result = 31 * result + parkLocation.hashCode()
        result = 31 * result + comments.hashCode()
        result = 31 * result + ratings.hashCode()
        result = 31 * result + sportTypes.hashCode()
        result = 31 * result + chatId.hashCode()
        return result
    }
}