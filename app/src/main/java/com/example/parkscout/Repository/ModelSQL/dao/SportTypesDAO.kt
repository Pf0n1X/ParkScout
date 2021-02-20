package com.example.parkscout.Repository.ModelSQL.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.parkscout.Repository.SportTypes

@Dao
interface SportTypesDAO {
    @Query("SELECT * FROM sport_types")
    fun getSportTypes(): LiveData<List<SportTypes>>;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSportType(sportTypes: SportTypes)
}