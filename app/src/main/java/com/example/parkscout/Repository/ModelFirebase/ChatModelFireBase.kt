package com.example.parkscout.Repository.ModelFirebase

import android.util.Log
import com.example.parkscout.Repository.*
import com.example.parkscout.Repository.Model.TrainingSpotModel
import com.example.parkscout.Repository.ModelSQL.ChatModelSQL
import com.example.parkscout.data.Types.Location
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.collections.HashMap

class ChatModelFireBase {
    private var modelChatSQL: ChatModelSQL = ChatModelSQL();
    private var trainingSpotModelFirebase = TrainingSpotModelFirebase();
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
        var currUserRef =
            FirebaseAuth.getInstance().currentUser?.uid?.let {
                db.collection("users").document(it)
            }!!;
        var query: Query = db.collection(ChatModelFireBase.COLLECTION_NAME)
            .whereArrayContains("users",currUserRef);
        query.get()
            .addOnCompleteListener(OnCompleteListener {

                if (it.isSuccessful) {
                    ChatWithAlllist.clear();
                    chats.clear();
                    for (doc in it.result!!) {
                        chat = Chat("", "");
                        chat.fromMap(doc.data)
                        chats.add(chat);
                        modelChatSQL.addChat(chat, {});
                        chatmessages = LinkedList<ChatMessage>();
                        chatmessages.clear();

                        if (doc.data["chat_messages"] != null) {
                            for (chatm in doc.data["chat_messages"] as ArrayList<*>) {
                                message = ChatMessage("", "", "", "", 0);
                                message.fromMap(chatm as Map<String?, Any?>);
                                chatmessages.add(message);
                                modelChatSQL.addChatMessage(message, {});
                            }
                        }

                        var chatwithchatmessages: ChatWithChatMessages = ChatWithChatMessages(
                            Chat = chat,
                            chatMessages = chatmessages
                        );
                        var userRef: DocumentReference;
                        var user: User = User("", "", "", 0, "");
                        for (chatUsers in doc.data["users"] as ArrayList<*>) {
                            userRef = chatUsers as DocumentReference;
                            userRef.get().addOnSuccessListener {
                                user = User("", "", "", 0, "");
                                it.data?.let { it1 -> user.fromMap(it1) };
                                users.add(user);
                                var userChatCrossRef: UserChatCrossRef =
                                    UserChatCrossRef(user.uId, chat.chatId);
                                modelChatSQL.addUser(user, {});
                                modelChatSQL.addUserChatRelation(userChatCrossRef, {});
                                listener(ChatWithAlllist)
                            }
                        }

                        var chatWithUsers: ChatWithUsers = ChatWithUsers(
                            Chat = chat,
                            Users = users
                        );

                        var training_spot_id = doc.data["training_spot_id"] as String;
                        var trainingSpot = TrainingSpot(
                            "",
                            "",
                            com.example.parkscout.data.Types.Location(0.0, 0.0),
                            "",
                            ""
                        );

                        var trainingSpotWithAll: TrainingSpotWithAll = TrainingSpotWithAll(
                            trainingSpot,
                            TrainingSpotsWithComments(trainingSpot, null),
                            TrainingSpotWithRating
                                (trainingSpot, null), TrainingSpotWithSportTypes
                                (trainingSpot, null), TrainingSpotWithImages
                                (trainingSpot, null)
                        );
                        var chatAndTrainingSpotWithAll: ChatAndTrainingSpotWithAll =
                            ChatAndTrainingSpotWithAll(
                                chat = chat,
                                trainingSpotWithAll = trainingSpotWithAll
                            )


                        trainingSpotModelFirebase.getTrainingSpotById(training_spot_id) { park: TrainingSpotWithAll? ->
                            if (park != null) {
                                trainingSpotWithAll.trainingSpot = park.trainingSpot;
                                trainingSpotWithAll.trainingSpotWithImages = park.trainingSpotWithImages;
                                trainingSpotWithAll.trainingSpotWithRating = park.trainingSpotWithRating;
                                trainingSpotWithAll.trainingSpotsWithComments = park.trainingSpotsWithComments;
                                trainingSpotWithAll.trainingSpotWithSportTypes = park.trainingSpotWithSportTypes;
                                listener(ChatWithAlllist)
                            }
                        }

                        var chatWithAll: ChatWithAll =
                            ChatWithAll(
                                chat,
                                chatwithchatmessages,
                                chatWithUsers,
                                chatAndTrainingSpotWithAll
                            );
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
                    chat = Chat("", "");
                    chat.fromMap(dc.data as Map<String?, Any?>)
                    chats.add(chat);
                    modelChatSQL.addChat(chat, {});
                    chatmessages = LinkedList<ChatMessage>();
                    var dataMap = (dc.data as Map<String?, Any?>);

                    if (dataMap["chat_messages"] != null) {
                        for (chatm in dataMap["chat_messages"] as ArrayList<*>) {
                            message = ChatMessage("", "", "", "", 0);
                            message.fromMap(chatm as Map<String?, Any?>);
                            chatmessages.add(message);
                            modelChatSQL.addChatMessage(message, {});
                        }
                    }

                    var chatwithchatmessages: ChatWithChatMessages = ChatWithChatMessages(
                        Chat = chat,
                        chatMessages = chatmessages
                    );

                    var userRef: DocumentReference;

                    for (chatUsers in (dc.data as Map<String?, Any?>)["users"] as ArrayList<*>) {
                        var user: User = User("", "", "", 0, "");
                        userRef = chatUsers as DocumentReference;
                        userRef.get().addOnSuccessListener {
                            it.data?.let { it1 -> user.fromMap(it1) };
                            users.add(user);
                            var userChatCrossRef: UserChatCrossRef =
                                UserChatCrossRef(user.uId, chat.chatId);
                            modelChatSQL.addUser(user, {});
                            modelChatSQL.addUserChatRelation(userChatCrossRef, {});
                            listener(ChatWithAlllist)
                        }
                    }
                    var chatWithUsers: ChatWithUsers = ChatWithUsers(
                        Chat = chat,
                        Users = users
                    );

                    var training_spot_id =
                        (dc.data as Map<String?, Any?>)["training_spot_id"] as String;
                    var trainingSpot = TrainingSpot(
                        "",
                        "",
                        com.example.parkscout.data.Types.Location(0.0, 0.0),
                        "",
                        ""
                    );

                    var trainingSpotWithAll: TrainingSpotWithAll = TrainingSpotWithAll(
                        trainingSpot,
                        TrainingSpotsWithComments(trainingSpot, null),
                        TrainingSpotWithRating
                            (trainingSpot, null), TrainingSpotWithSportTypes
                            (trainingSpot, null), TrainingSpotWithImages
                            (trainingSpot, null)
                    );

                    trainingSpotModelFirebase.getTrainingSpotById(training_spot_id) { park: TrainingSpotWithAll? ->
                        if (park != null) {
                            trainingSpotWithAll = park
//                            listener(ChatWithAlllist)
                        }
                    }

                    var chatAndTrainingSpotWithAll: ChatAndTrainingSpotWithAll =
                        ChatAndTrainingSpotWithAll(
                            chat = chat,
                            trainingSpotWithAll = trainingSpotWithAll
                        )

                    var chatWithAll: ChatWithAll =
                        ChatWithAll(
                            chat,
                            chatwithchatmessages,
                            chatWithUsers,
                            chatAndTrainingSpotWithAll
                        );
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

        var users: LinkedList<DocumentReference> = LinkedList<DocumentReference>();

        for (user in chatWithAll.chatWithUsers.Users) {
//            users.add("/users/" + user.uId);
            var ref = db.collection("users").document(user.uId);
            users.add(ref);
        }

        var chatCollectionFireStore = chatCollectionFireStore(
            doc.id,
            chatWithAll.chat.training_spot_id,
            chatWithAll.chatWithChatMessages.chatMessages,
            users
        )

        FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document(doc.id).set(chatCollectionFireStore.toMap())
            .addOnSuccessListener { listener();
                TrainingSpotModel.instance.updateTrainingSpot(chatWithAll.chat.training_spot_id,doc.id,{});

            }
            .addOnFailureListener { listener(); }
    }

    fun addMessage(chatId: String, message: ChatMessage, listener: () -> Unit) {
        var db: FirebaseFirestore = FirebaseFirestore.getInstance();
        db.collection(ChatModelFireBase.COLLECTION_NAME)
            .document(chatId)
            .update("chat_messages", FieldValue.arrayUnion(message.toMap()))
            .addOnSuccessListener { listener(); };
    }

    fun addUserToChat(chatId: String, uid: String, function: (ChatWithAll) -> Unit) {
        var db: FirebaseFirestore = FirebaseFirestore.getInstance();
        db.collection("chat")
            .document(chatId)
            .update("users", FieldValue.arrayUnion(db.collection("users").document(uid)))
            .addOnSuccessListener {
                Log.d("Tag", "Success");
            }
            .addOnFailureListener {
                Log.d("TAG", "Error");
            };
    }

    fun createChatBetweenTwoUsers(firstUserUID: String, secondUserUID: String, listener: (chat: Chat) -> Unit) {

        // Get the database.
        var db = FirebaseFirestore.getInstance();

        // Get the users' references.
        var firstUserRef = db.collection("users").document(firstUserUID);
        var secondUserRef = db.collection("users").document(secondUserUID);

        // Create a new chat reference.
        var chatDoc = db.collection(COLLECTION_NAME)
            .document();

        // Create a chat and use the reference's id.
        var chatID = chatDoc.id;

        // Insert both user references to the chat.
        var chat: HashMap<String?, Any?> = HashMap<String?, Any?>();
        var userList: LinkedList<DocumentReference> = LinkedList<DocumentReference>();
        userList.add(firstUserRef);
        userList.add(secondUserRef);
        chat["id"] = chatID;
        chat["users"] = userList;
        chat["training_spot_id"] = "";
        chat["chat_messages"] = arrayListOf<ChatMessage>();
        chatDoc.set(chat)
            .addOnCompleteListener {
                    listener(Chat(chatID, ""))
                };

        // Set the chat data.
    }

    fun deleteMessage(chat: ChatWithAll, chatMsg: ChatMessage, listener: (ChatMessage) -> Unit) {

        // Get the database.
        var db = FirebaseFirestore.getInstance();

        // Get the message data.
        var msgList = chat.chatWithChatMessages.chatMessages as LinkedList<ChatMessage>;
        msgList.remove(chatMsg);
        var mapList = msgList.map{ og: ChatMessage ->
            var map: HashMap<String, Any> = og.toMap() as HashMap<String, Any>;
            map["last_updated"] = Timestamp(og.last_updated, 0);

            map;
        }

        // Since there is no delete operation for inner arrays using indexes,
        // An update operation is needed win which the whole array will be re inserted.
        db.collection(COLLECTION_NAME)
            .document(chatMsg.chatId)
            .update("chat_messages", mapList)
            .addOnSuccessListener {
                listener(chatMsg);
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "ERROR");
            };
    }
}

data class chatCollectionFireStore(
    val id: String,
    val training_spot_id: String,
    val chat_messages: List<ChatMessage>,
    val users: List<DocumentReference>
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