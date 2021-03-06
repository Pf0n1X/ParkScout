package com.example.parkscout.Repository.ModelSQL.dao

import android.util.Log
import androidx.room.*
import com.example.parkscout.Repository.*

@Dao
interface TrainingSpotDao {

    @Query("SELECT * FROM training_spot")
       fun getAllParks(): List<TrainingSpot>?

    @Query("SELECT * FROM training_spot WHERE parkId = :parkId")
    fun getParkById(parkId:String): TrainingSpot

    @Query("SELECT * FROM training_spot WHERE parkId = :parkName")
    fun getParkByName(parkName:String): List<TrainingSpot>

    @Query("SELECT * FROM training_spot INNER JOIN comments ON  comments.trainingId = training_spot.parkId WHERE  comments.userId = :userId")
    fun getParksByUser(userId:String): List<TrainingSpot>

    @Insert( onConflict = OnConflictStrategy.REPLACE)
    fun insert(parks: TrainingSpot) ;

    @Insert(entity = SportTypes::class,onConflict = OnConflictStrategy.REPLACE)
    fun insertKinds(sportTypes:List<SportTypes>?);

    @Insert(entity = Rating::class,onConflict = OnConflictStrategy.REPLACE)
    fun insertRatings(ratings: List<Rating>?);

    @Insert(entity = Comment::class,onConflict = OnConflictStrategy.REPLACE)
    fun insertComments(comments: List<Comment>?);

    @Insert(entity = Images::class,onConflict = OnConflictStrategy.REPLACE)
    fun insertImages(images: List<Images>?);
}
