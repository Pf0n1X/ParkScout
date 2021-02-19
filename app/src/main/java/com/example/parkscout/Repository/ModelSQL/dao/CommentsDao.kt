package com.example.parkscout.Repository.ModelSQL.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.parkscout.Repository.Comment

@Dao
interface CommentsDao {
    @Query("SELECT * FROM comments WHERE trainingId = :trainingSpotId")
    fun getAllCommentsOfPark( trainingSpotId : String): LiveData<List<Comment>>;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComment(comment: Comment)
}