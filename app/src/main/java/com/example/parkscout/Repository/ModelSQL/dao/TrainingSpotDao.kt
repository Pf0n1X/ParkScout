package com.example.parkscout.Repository.ModelSQL.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.parkscout.data.model.TrainingSpot

@Dao
interface TrainingSpotDao {

//    @Query("SELECT * FROM training_spot")
//    fun getAllParks(): LiveData<List<TrainingSpot>>;

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertPark(parks: TrainingSpot)
}
