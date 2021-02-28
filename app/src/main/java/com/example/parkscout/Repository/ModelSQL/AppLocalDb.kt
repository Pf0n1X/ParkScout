package com.example.parkscout.Repository.ModelSQL

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.parkscout.ParkScoutApplication
import com.example.parkscout.Repository.*
import com.example.parkscout.Repository.ModelSQL.dao.*

@Database(entities = [ChatMessage::class , TrainingSpot:: class,Comment:: class,Rating::class,SportTypes::class, Chat::class, User::class, UserChatCrossRef::class], version = 4)
abstract class AppLocalDb: RoomDatabase() {

    abstract fun chatMessageDao(): ChatMessageDao;
    abstract fun trainingSpotDao(): TrainingSpotDao;
    abstract fun commentDao(): CommentsDao;
    abstract fun ratingDao(): RatingDao;
    abstract fun sportTypesDAO(): SportTypesDAO;
    abstract fun chatDao(): ChatDao;
    abstract fun userDao(): UserDao;

    companion object {
        const val DB_NAME: String = "ParkScoutDB";

        @Volatile private var instance: AppLocalDb? = null;

        fun getInstance(): AppLocalDb {
            return instance?: synchronized(this) {
                instance?: Room.databaseBuilder(
                    ParkScoutApplication.context,
                    AppLocalDb::class.java, DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
    }
}