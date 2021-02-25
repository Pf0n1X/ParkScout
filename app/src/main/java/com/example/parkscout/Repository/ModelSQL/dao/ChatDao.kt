package com.example.parkscout.Repository.ModelSQL.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.parkscout.Repository.Chat

@Dao
interface ChatDao {

    @Transaction
    @Query("SELECT * FROM chat")
    fun getAllChats(): LiveData<List<Chat>>;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(Chats: List<Chat>);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chat: Chat);

    @Delete
    fun delete(chat: Chat);
}