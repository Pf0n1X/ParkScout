package com.example.parkscout.Repository

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ChatWithUsers (
    @Embedded val Chat: String,
    @Relation(
        parentColumn = "ChatId",
        entityColumn = "uId",
        associateBy = Junction(UserChatCrossRef::class)
    )
    val Users: List<User>
){
}