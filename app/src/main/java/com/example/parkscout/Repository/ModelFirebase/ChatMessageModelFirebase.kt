package com.example.parkscout.Repository.ModelFirebase

import android.util.Log
import com.example.parkscout.Repository.ChatMessage
import com.example.parkscout.Repository.ChatWithAll
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.*
import java.sql.Timestamp
import java.util.*

// TODO: Delete this class
class ChatMessageModelFirebase {

    companion object {
        val COLLECTION_NAME: String = "chat_message";
    }

    public fun getAllMessages(lastUpdated: Long, listener: (List<ChatMessage>) -> Unit) {
        var db: FirebaseFirestore = FirebaseFirestore.getInstance();
        var ts: Timestamp = Timestamp(lastUpdated);
        var messages: LinkedList<ChatMessage> = LinkedList<ChatMessage>();

        var query: Query = db.collection(Companion.COLLECTION_NAME)
            .whereGreaterThanOrEqualTo("lastUpdated", ts);
            query.get()
            .addOnCompleteListener(OnCompleteListener {


                if (it.isSuccessful) {
                    for (doc in it.result!!) {
                        var message: ChatMessage = ChatMessage("", "" , "", "", 0);

                        message.fromMap(doc.data);
                        messages.add(message);
                    }
                }

                listener(messages);
            });

        query.addSnapshotListener({ value: QuerySnapshot?, error: FirebaseFirestoreException? ->
            for (dc in value!!.documentChanges) {
                var msg: ChatMessage = ChatMessage("", "", "", "", 0);
                msg.fromMap(dc.document.data);
                (messages as LinkedList<ChatMessage>).add(msg);
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

    fun addUserToChat(chatId: String, uid: String, function: (ChatWithAll) -> Unit) {
        var db: FirebaseFirestore = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_NAME)
            .document(chatId)
            .update("users", FieldValue.arrayUnion(db.collection("users").document(uid)))
            .addOnSuccessListener {
                Log.d("Tag", "Success");
            }
            .addOnFailureListener {
                Log.d("TAG", "Error");
            };
    }
}