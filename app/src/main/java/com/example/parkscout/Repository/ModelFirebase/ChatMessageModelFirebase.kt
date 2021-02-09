package com.example.parkscout.Repository.ModelFirebase

import com.example.parkscout.Repository.ChatMessage
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Timestamp
import java.util.*

class ChatMessageModelFirebase {

    companion object {
        val COLLECTION_NAME: String = "chat_message";
    }

    public fun getAllMessages(lastUpdated: Long, listener: (LinkedList<ChatMessage>) -> Unit) {
        var db: FirebaseFirestore = FirebaseFirestore.getInstance();
        var ts: Timestamp = Timestamp(lastUpdated);

        db.collection(Companion.COLLECTION_NAME)
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

                listener(messages);
            });
    }

    fun addMessage(msg: ChatMessage, listener: () -> Unit) {
        var db: FirebaseFirestore = FirebaseFirestore.getInstance();
        var doc: DocumentReference = db.collection(Companion.COLLECTION_NAME)
            .document();

        msg.id = doc.id;

        doc.set(msg.toMap())
            .addOnSuccessListener { listener(); }
            .addOnFailureListener { listener(); }
    }
}