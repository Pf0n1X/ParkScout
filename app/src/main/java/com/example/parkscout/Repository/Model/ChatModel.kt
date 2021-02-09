package com.example.parkscout.Repository.Model

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.parkscout.ParkScoutApplication
import com.example.parkscout.Repository.ChatMessage
import com.example.parkscout.Repository.ModelFirebase.ChatMessageModelFirebase
import com.example.parkscout.Repository.ModelSQL.ChatMessageModelSQL
import java.util.*

class ChatModel {

    companion object {
        public var instance: ChatModel = ChatModel()
            public get
            private set;
    }

    // Data Members
    private var modelFirebase: ChatMessageModelFirebase;
    private var modelSQL: ChatMessageModelSQL;
    private var messageList: LiveData<List<ChatMessage>>;

    // Constructors
    init {
        this.modelFirebase = ChatMessageModelFirebase();
        this.modelSQL = ChatMessageModelSQL();
        this.messageList = modelSQL.getAllMessages();
    }

    // Methods
    public fun getAllMessages(): LiveData<List<ChatMessage>> {
        if (this.messageList.value == null) {
            this.messageList = modelSQL.getAllMessages();
            refreshAllMessages(null);
        }

        return this.messageList;
    }

    public fun refreshAllMessages(refListener: (() -> Unit)?) {

        // Get the last local update date.
        val sp: SharedPreferences = ParkScoutApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        var lastUpdated: Long = sp.getLong("lastUpdated", 0);

        // Get all the updated record from firebase, since the last retrieval.
        val listener = { messages: LinkedList<ChatMessage> ->

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
        modelFirebase.addMessage(msg, listener);
    }
}