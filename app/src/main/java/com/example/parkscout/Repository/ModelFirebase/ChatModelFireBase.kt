    package com.example.parkscout.Repository.ModelFirebase

import com.example.parkscout.Repository.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*


class ChatModelFireBase {
    companion object {
        val COLLECTION_NAME: String = "chat";
    }

    public fun getAllChats(userId: String, listener: (List<ChatWithAll>) -> Unit) {
        var db: FirebaseFirestore = FirebaseFirestore.getInstance();
        var ChatWithAlllist: LinkedList<ChatWithAll> = LinkedList<ChatWithAll>();
        var chats: LinkedList<Chat> = LinkedList<Chat>();
        var chatmessages: LinkedList<ChatMessage> = LinkedList<ChatMessage>();
        var chatswithchatmessages: LinkedList<ChatWithChatMessages> = LinkedList<ChatWithChatMessages>();
        var users: LinkedList<User> = LinkedList<User>();
            var query: Query = db.collection(ChatModelFireBase.COLLECTION_NAME);
//            .whereArrayContains("users", userId);

        query.get()
            .addOnCompleteListener(OnCompleteListener {

                if (it.isSuccessful) {
                    for (doc in it.result!!) {
                        var message: ChatMessage = ChatMessage("", "" , "", "", 0);
                        var chat: Chat = Chat("", "" );
                        chat.fromMap(doc.data)
                        chats.add(chat);

                        for (chatm in doc.data["chat_messages"] as ArrayList<*>) {
                            message.fromMap(chatm as Map<String?, Any?>);
                            chatmessages.add(message);
                        }

                        var chatwithchatmessages: ChatWithChatMessages = ChatWithChatMessages(Chat = doc.data["id"] as String,
                            chatMessages = chatmessages
                        );
                        chatswithchatmessages.add(chatwithchatmessages);

                        var userRef: DocumentReference ;
                        var user: User = User("","","");
                        for (chatUsers in doc.data["users"] as ArrayList<*>) {
                            userRef = chatUsers as DocumentReference;
                                userRef.get().addOnSuccessListener {
                                    it.data?.let { it1 -> user.fromMap(it1) };
                                    listener(ChatWithAlllist)
                                }
                        }
                        users.add(user);

                        var chatWithUsers: ChatWithUsers = ChatWithUsers(Chat = doc.data["id"] as String,
                        Users = users);

                        var chatWithAll: ChatWithAll = ChatWithAll(chat,chatwithchatmessages,chatWithUsers);
                        ChatWithAlllist.add(chatWithAll);

                    }
                }

                listener(ChatWithAlllist);
            });

//        query.addSnapshotListener({ value: QuerySnapshot?, error: FirebaseFirestoreException? ->
//            for (dc in value!!.documentChanges) {
//                var msg: ChatMessage = ChatMessage("", "", "", "", "", 0);
//                msg.fromMap(dc.document.data);
//                (messages as LinkedList<ChatMessage>).add(msg);
//            }
//
//            listener(messages);
//        });
    }

}