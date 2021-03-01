package com.example.parkscout.Repository.ModelFirebase

import com.example.parkscout.Repository.*
import com.example.parkscout.Repository.ModelSQL.ChatModelSQL
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.*
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class ChatModelFireBase {
    private var modelChatSQL: ChatModelSQL = ChatModelSQL();
    var isFirstListener: AtomicBoolean = AtomicBoolean(true)

    companion object {
        val COLLECTION_NAME: String = "chat";
    }

    public fun getAllChats(userId: String, listener: (List<ChatWithAll>) -> Unit) {
        var message: ChatMessage = ChatMessage("", "", "", "", 0);
        var chat: Chat = Chat("", "");
        var db: FirebaseFirestore = FirebaseFirestore.getInstance();
        var ChatWithAlllist: LinkedList<ChatWithAll> = LinkedList<ChatWithAll>();
        var chats: LinkedList<Chat> = LinkedList<Chat>();
        var chatmessages: LinkedList<ChatMessage> = LinkedList<ChatMessage>();
        var users: LinkedList<User> = LinkedList<User>();
        var query: Query = db.collection(ChatModelFireBase.COLLECTION_NAME)
//            .whereArrayContains("users",FirebaseAuth.getInstance().currentUser?.uid);
        query.get()
            .addOnCompleteListener(OnCompleteListener {

                if (it.isSuccessful) {
                    ChatWithAlllist.clear();
                    chats.clear();
                    for (doc in it.result!!) {
                        chat.fromMap(doc.data)
                        chats.add(chat);
                        modelChatSQL.addChat(chat, {});
                        chatmessages.clear();

                        for (chatm in doc.data["chat_messages"] as ArrayList<*>) {
                            message = ChatMessage("", "", "", "", 0);
                            message.fromMap(chatm as Map<String?, Any?>);
                            chatmessages.add(message);
                            modelChatSQL.addChatMessage(message, {});
                        }

                        var chatwithchatmessages: ChatWithChatMessages = ChatWithChatMessages(
                            Chat = chat,
                            chatMessages = chatmessages
                        );
                        var userRef: DocumentReference;
                        var user: User = User("", "", "", 0);
                        for (chatUsers in doc.data["users"] as ArrayList<*>) {
                            userRef = chatUsers as DocumentReference;
                            userRef.get().addOnSuccessListener {
                                it.data?.let { it1 -> user.fromMap(it1) };
                                users.add(user);
                                var userChatCrossRef: UserChatCrossRef =
                                    UserChatCrossRef(user.uId, chat.chatId);
                                modelChatSQL.addUser(user, {});
                                modelChatSQL.addUserChatRelation(userChatCrossRef, {});
//                                listener(ChatWithAlllist)
                            }
                        }

                        var chatWithUsers: ChatWithUsers = ChatWithUsers(
                            Chat = chat,
                            Users = users
                        );

                        var chatWithAll: ChatWithAll =
                            ChatWithAll(chat, chatwithchatmessages, chatWithUsers);
                        ChatWithAlllist.add(chatWithAll);

                    }
                }

                listener(ChatWithAlllist);
            });

        query.addSnapshotListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
            if (!isFirstListener.get()) {
                ChatWithAlllist.clear();
                chats.clear();
                for (dc in value!!.documents) {
                    chat.fromMap(dc.data as Map<String?, Any?>)
                    chats.add(chat);
                    modelChatSQL.addChat(chat, {});
                    chatmessages = LinkedList<ChatMessage>();

                    for (chatm in (dc.data as Map<String?, Any?>)["chat_messages"] as ArrayList<*>) {
                        message = ChatMessage("", "", "", "", 0);
                        message.fromMap(chatm as Map<String?, Any?>);
                        chatmessages.add(message);
                        modelChatSQL.addChatMessage(message, {});
                    }

                    var chatwithchatmessages: ChatWithChatMessages = ChatWithChatMessages(
                        Chat = chat,
                        chatMessages = chatmessages
                    );

                    var userRef: DocumentReference;
                    var user: User = User("", "", "", 0);
                    for (chatUsers in (dc.data as Map<String?, Any?>)["users"] as ArrayList<*>) {
                        userRef = chatUsers as DocumentReference;
                        userRef.get().addOnSuccessListener {
                            it.data?.let { it1 -> user.fromMap(it1) };
                            users.add(user);
                            var userChatCrossRef: UserChatCrossRef =
                                UserChatCrossRef(user.uId, chat.chatId);
                            modelChatSQL.addUser(user, {});
                            modelChatSQL.addUserChatRelation(userChatCrossRef, {});
//                            listener(ChatWithAlllist)
                        }
                    }
                    var chatWithUsers: ChatWithUsers = ChatWithUsers(
                        Chat = chat,
                        Users = users
                    );

                    var chatWithAll: ChatWithAll =
                        ChatWithAll(chat, chatwithchatmessages, chatWithUsers);
                    ChatWithAlllist.add(chatWithAll);
                }

                listener(ChatWithAlllist);
            } else {
                isFirstListener.set(false);
            }
        }
    }

    fun addChat(chatWithAll: ChatWithAll, listener: () -> Unit) {
        var db: FirebaseFirestore = FirebaseFirestore.getInstance();
        var doc: DocumentReference = db.collection(ChatModelFireBase.COLLECTION_NAME)
            .document();
        var users: LinkedList<String> = LinkedList<String>();

        for (user in chatWithAll.chatWithUsers.Users) {
            users.add("/users/" + user.uId);
        }

        var chatCollectionFireStore = chatCollectionFireStore(
            doc.id,
            chatWithAll.chat.training_spot_id,
            chatWithAll.chatWithChatMessages.chatMessages,
            users
        )

        doc.set(chatCollectionFireStore.toMap())
            .addOnSuccessListener { listener(); }
            .addOnFailureListener { listener(); }
    }

    fun addMessage(chatId: String, message: ChatMessage, listener: () -> Unit) {
        var db: FirebaseFirestore = FirebaseFirestore.getInstance();
        db.collection(ChatModelFireBase.COLLECTION_NAME)
            .document(chatId)
            .update("chat_messages", FieldValue.arrayUnion(message.toMap()))
            .addOnSuccessListener { listener(); };
    }
}

data class chatCollectionFireStore(
    val id: String,
    val training_spot_id: String,
    val chat_messages: List<ChatMessage>,
    val users: List<String>
) {
    fun toMap(): Map<String, Any> {
        val result: HashMap<String, Any> = HashMap()
        result["id"] = id
        result["training_spot_id"] = training_spot_id;
        result["chat_messages"] = chat_messages;
        result["users"] = users;
        return result
    }
}