package com.example.parkscout.ViewModel

import androidx.lifecycle.ViewModel
import com.example.parkscout.Repository.ChatMessage
import androidx.lifecycle.LiveData
import com.example.parkscout.Repository.Model.ChatModel

class ChatFragmentViewModel: ViewModel() {

    // Data Members
    lateinit var msgList: LiveData<List<ChatMessage>>
        private set;

    // Constructors
    init {
        this.msgList = ChatModel.instance.getAllMessages();
    }

    // Methods
    fun addMessage(msg: ChatMessage, listener: () -> Unit) {
        ChatModel.instance.addMessage(msg, listener);
    }
}