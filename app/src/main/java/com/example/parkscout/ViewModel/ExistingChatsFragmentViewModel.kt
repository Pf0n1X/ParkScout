package com.example.parkscout.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.parkscout.Repository.Chat
import com.example.parkscout.Repository.ChatMessage
import com.example.parkscout.Repository.ChatWithAll
import com.example.parkscout.Repository.Model.ChatModel

class ExistingChatsFragmentViewModel: ViewModel() {
    // Data Members
    lateinit var chatList: LiveData<List<ChatWithAll>>
        private set;

    // Constructors
    init {
        this.chatList = ChatModel.instance.getAllChats();
    }

    // Methods
    fun addMessage(chatId: String, msg: ChatMessage, listener: () -> Unit) {
        ChatModel.instance.addMessage(chatId, msg, listener);
    }

    fun deleteMessage(chat: ChatWithAll, chatMsg: ChatMessage, listener: (ChatMessage) -> Unit) {
        ChatModel.instance.deleteMessage(chat, chatMsg, listener);
    }
}