package com.example.parkscout.Repository

import androidx.room.Entity

@Entity(primaryKeys = ["uId", "ChatId"])
data class UserChatCrossRef(
    val userId: String,
    val chatId: String
)