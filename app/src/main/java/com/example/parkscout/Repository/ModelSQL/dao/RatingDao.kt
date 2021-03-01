package com.example.parkscout.Repository.ModelSQL.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.parkscout.Repository.Rating

@Dao
interface RatingDao {
    @Query("SELECT * FROM rating WHERE trainingSpotId = :trainingSpotId")
    fun getParkRating( trainingSpotId : String): List<Rating>;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRating(rating: Rating)
}