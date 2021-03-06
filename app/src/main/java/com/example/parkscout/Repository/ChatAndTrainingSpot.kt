package com.example.parkscout.Repository

import androidx.room.Embedded
import androidx.room.Relation

data class ChatAndTrainingSpot(
    @Embedded val chat: Chat,
    @Relation(
        parentColumn = "chatId",
        entityColumn = "chatId"
    )

    val trainingSpot: TrainingSpot? = null
)