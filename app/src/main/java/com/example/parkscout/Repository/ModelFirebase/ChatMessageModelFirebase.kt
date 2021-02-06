package com.example.parkscout.Repository.ModelFirebase

import com.example.parkscout.Repository.ChatMessage
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Timestamp
import java.util.*

class ChatMessageModelFirebase {

    interface GetAllChatMessagesListener {
        fun onComplete(message: LinkedList<ChatMessage>);
    }

    public fun getAllMessages(lastUpdated: Long, listener: GetAllChatMessagesListener) {
        var db: FirebaseFirestore = FirebaseFirestore.getInstance();
        var ts: Timestamp = Timestamp(lastUpdated);

        db.collection("chat_message")
            .whereGreaterThanOrEqualTo("lastUpdated", ts)
            .get()
            .addOnCompleteListener(OnCompleteListener {
                var messages: LinkedList<ChatMessage> = LinkedList<ChatMessage>();

                if (it.isSuccessful) {
                    for (doc in it.result!!) {
                        var message: ChatMessage = ChatMessage("", "", "", "", 0);
                        message.fromMap(doc.data);
                        messages.add(message);
                    }
                }

                listener.onComplete(messages);
            });
    }
}