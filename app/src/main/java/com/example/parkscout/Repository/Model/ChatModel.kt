package com.example.parkscout.Repository.Model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.parkscout.ParkScoutApplication
import com.example.parkscout.Repository.Chat
import com.example.parkscout.Repository.ChatMessage
import com.example.parkscout.Repository.ChatWithAll
import com.example.parkscout.Repository.ChatWithChatMessages
import com.example.parkscout.Repository.ModelFirebase.ChatMessageModelFirebase
import com.example.parkscout.Repository.ModelFirebase.ChatModelFireBase
import com.example.parkscout.Repository.ModelSQL.ChatMessageModelSQL
import com.example.parkscout.Repository.ModelSQL.ChatModelSQL
import java.util.*

class ChatModel {

    companion object {
        public var instance: ChatModel = ChatModel()
            public get
            private set;
    }

    // Data Members
    var modelFirebase: ChatMessageModelFirebase;
    private var modelSQL: ChatMessageModelSQL;
    private var modelChatSQL: ChatModelSQL;
    private var messageList: ChatMessageLiveData;
    private var chatList: ChatLiveData;
    var modelChatFirebase: ChatModelFireBase;

    // Constructors
    init {
        this.modelFirebase = ChatMessageModelFirebase();
        this.modelSQL = ChatMessageModelSQL();
        this.modelChatSQL = ChatModelSQL();
        this.messageList = ChatMessageLiveData();
        this.chatList = ChatLiveData();
        this.modelChatFirebase = ChatModelFireBase();
    }

    // Methods
    public fun getAllMessages(): LiveData<List<ChatMessage>> {
//        if (this.messageList.value == null) {
//            this.messageList = modelSQL.getAllMessages();
//            refreshAllMessages(null);
//        }
//
        return this.messageList;
    }

    public fun getAllChats(): LiveData<List<ChatWithAll>> {
        return this.chatList;
    }

    public fun refreshAllMessages(refListener: (() -> Unit)?) {

        // Get the last local update date.
        val sp: SharedPreferences = ParkScoutApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        var lastUpdated: Long = sp.getLong("lastUpdated", 0);

        // Get all the updated record from firebase, since the last retrieval.
        val listener = { messages: List<ChatMessage> ->

            // Find the last message date
            var maxLastU: Long = 0;

            // Insert the data to the Room DB.
            for (msg in messages) {
                modelSQL.addChatMessage(msg, null);

                if (msg.lastUpdated > maxLastU) {
                    maxLastU = msg.lastUpdated;
                }

            }

            // Update the local last update date.
            sp.edit().putLong("lastUpdated", maxLastU).commit();

            // Return the updated data to the listeners.
            if (refListener != null) {
                refListener();
            }
        }

        modelFirebase.getAllMessages(lastUpdated, listener);
    }

    public fun addMessage(msg: ChatMessage, listener: () -> Unit) {
        var refreshListener: () -> Unit = {
            listener();
        };

        var addListener: () -> Unit = {
            refreshAllMessages(refreshListener);
        };

        modelFirebase.addMessage(msg, addListener);
    }

    inner class ChatLiveData: MutableLiveData<List<ChatWithAll>>() {

        // Constructors
        init {
            value = LinkedList<ChatWithAll>();
        }

        // Methods
        override fun onActive() {
            super.onActive();

            val sp: SharedPreferences = ParkScoutApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);

            // Select

            modelChatFirebase.getAllChats("", { chats: List<ChatWithAll> ->
                value = chats;
////                for (msg in messages) {
////                    modelSQL.addChatMessage(msg, {
//////                        (value as LinkedList).add(msg);
//                    });
//                }
            });
        }
        override fun onInactive() {
            super.onInactive();

//            modelFirebase.cancelGetAllMessages();
        }


    }

    inner class ChatMessageLiveData: MutableLiveData<List<ChatMessage>>() {

        // Constructors
        init {
            value = LinkedList<ChatMessage>();
        }

        // Methods
        override fun onActive() {
            super.onActive();

            val sp: SharedPreferences = ParkScoutApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
            var lastUpdated: Long = sp.getLong("lastUpdated", 0);

            modelFirebase.getAllMessages(lastUpdated, { messages: List<ChatMessage> ->
                value = messages;
                Log.d("BP", "The messages were received: " + messages.size);

                for (msg in messages) {
                    modelSQL.addChatMessage(msg, {
//                        (value as LinkedList).add(msg);
                    });
                }
            });
        }

        override fun onInactive() {
            super.onInactive();

//            modelFirebase.cancelGetAllMessages();
        }
    }
}