package com.example.parkscout.Repository.ModelSQL.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.parkscout.Repository.Images

@Dao
interface ImgDAO {
    @Query("SELECT * FROM images WHERE trainingSpotId = :trainingSpotId")
    fun getAllImgsOfPark( trainingSpotId : String): List<Images>;
}