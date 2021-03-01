package com.example.parkscout.Repository.ModelSQL.dao

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.parkscout.Repository.*
import java.util.*


@Dao
interface TrainingSpotDao {
    @Query("SELECT * FROM training_spot")
       fun getAllParks(): List<TrainingSpot>?
//       fun getAllParks(): LiveData<List<TrainingSpot>?>?;
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(parks: TrainingSpot){
        Log.d("TAG", "Success when trying to save park");
    }

    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert(entity = SportTypes::class)
    fun insertKinds(sportTypes:List<SportTypes>?){
        Log.d("TAG", "Success when trying to save types");
    }
    @Insert(entity = Rating::class)
    fun insertRatings(ratings: List<Rating>?){
        Log.d("TAG", "Success when trying to save ratings");
    }
    @Insert(entity = Comment::class)
    fun insertComments(comments: List<Comment>?){
        Log.d("TAG", "Success when trying to save comments");
    }
    @Insert(entity = Images::class)
    fun insertImages(images: List<Images>?){
        Log.d("TAG", "Success when trying to save images");
    }
}
