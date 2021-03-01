package com.example.parkscout.Repository

import androidx.room.Entity

@Entity(primaryKeys = ["uid", "chatId"])
data class UserChatCrossRef(
    val uid: String,
    val chatId: String
){

}