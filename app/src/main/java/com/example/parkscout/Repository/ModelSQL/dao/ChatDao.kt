package com.example.parkscout.Repository.ModelSQL.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.parkscout.Repository.*

@Dao
interface ChatDao {

    @Transaction
    @Query("SELECT * FROM chat")
    fun getAllChats(): List<Chat>;

    @Transaction
    @Query("SELECT * FROM chat")
    fun getAllChatsWithChatMessages(): List<ChatWithChatMessages>;

    @Transaction
    @Query("SELECT * FROM chat")
    fun getAllChatsWithUsers(): List<ChatWithUsers>;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(Chats: List<Chat>);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chat: Chat);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chatMessage: ChatMessage);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userChatCrossRef: UserChatCrossRef)

    @Delete
    fun delete(chat: Chat);
}