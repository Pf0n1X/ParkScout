package com.example.parkscout.Repository

data class ChatWithAll (
    var chat: Chat,
    var chatWithChatMessages: ChatWithChatMessages,
    var chatWithUsers: ChatWithUsers,
    var chatAndTrainingSpot: ChatAndTrainingSpotWithAll
)