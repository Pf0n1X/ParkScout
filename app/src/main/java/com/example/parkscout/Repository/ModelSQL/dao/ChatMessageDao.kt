package com.example.parkscout.Repository.ModelSQL.dao

import androidx.lifecycle.LiveData
import androidx.room.*;
import com.example.parkscout.Repository.ChatMessage

@Dao
interface ChatMessageDao {

    @Query("SELECT * FROM chat_message")
    fun getAllMessages(): LiveData<List<ChatMessage>>;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(messages: List<ChatMessage>);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: ChatMessage);

    @Delete
    fun delete(message: ChatMessage);
}