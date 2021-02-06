package com.example.parkscout.Repository.ModelSQL

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.parkscout.ParkScoutApplication
import com.example.parkscout.Repository.ChatMessage
import com.example.parkscout.Repository.ModelSQL.dao.ChatMessageDao

@Database(entities = [ChatMessage::class], version = 1)
abstract class AppLocalDb: RoomDatabase() {

    abstract fun chatMessageDao(): ChatMessageDao;

    companion object {
        const val DB_NAME: String = "ParkScoutDB";

        @Volatile private var instance: AppLocalDb? = null;

        fun getInstance(): AppLocalDb {
            return instance?: synchronized(this) {
                instance?: Room.databaseBuilder(
                    ParkScoutApplication.context,
                    AppLocalDb::class.java, DB_NAME
                ).build()
            }
        }
    }
}