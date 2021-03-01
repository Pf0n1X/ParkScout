package com.example.parkscout.Repository

import androidx.room.Embedded
import androidx.room.Relation
import com.google.firebase.Timestamp

data class ChatWithChatMessages(
    @Embedded val Chat: Chat,
    @Relation(
        parentColumn = "chatId",
        entityColumn = "chatId"
    )
    val chatMessages: List<ChatMessage>
) {
}