package com.example.parkscout.Repository.Model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.parkscout.ParkScoutApplication
import com.example.parkscout.Repository.*
import com.example.parkscout.Repository.ModelFirebase.ChatMessageModelFirebase
import com.example.parkscout.Repository.ModelFirebase.ChatModelFireBase
import com.example.parkscout.Repository.ModelSQL.ChatMessageModelSQL
import com.example.parkscout.Repository.ModelSQL.ChatModelSQL
import com.example.parkscout.Repository.ModelSQL.TrainingSpotModelSQL
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

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
    private var trainingSpotModelSQL: TrainingSpotModelSQL;
    private var messageList: ChatMessageLiveData;
    private var chatList: ChatLiveData;
    var modelChatFirebase: ChatModelFireBase;
    private var executor: Executor;

    // Constructors
    init {
        this.modelFirebase = ChatMessageModelFirebase();
        this.modelSQL = ChatMessageModelSQL();
        this.modelChatSQL = ChatModelSQL();
        this.trainingSpotModelSQL = TrainingSpotModelSQL();
        this.messageList = ChatMessageLiveData();
        this.chatList = ChatLiveData();
        this.modelChatFirebase = ChatModelFireBase();
        this.executor = Executors.newSingleThreadExecutor();
    }

    // Methods
    public fun getAllMessages(): LiveData<List<ChatMessage>> {
        return this.messageList;
    }

    public fun getAllChats(): LiveData<List<ChatWithAll>> {
        return this.chatList;
    }

    public fun refreshAllMessages(refListener: (() -> Unit)?) {

        // Get the last local update date.
        val sp: SharedPreferences =
            ParkScoutApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        var lastUpdated: Long = sp.getLong("lastUpdated", 0);

        // Get all the updated record from firebase, since the last retrieval.
        val listener = { messages: List<ChatMessage> ->

            // Find the last message date
            var maxLastU: Long = 0;

            // Insert the data to the Room DB.
            for (msg in messages) {
                modelSQL.addChatMessage(msg, null);

                if (msg.last_updated > maxLastU) {
                    maxLastU = msg.last_updated;
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

    fun addChat(chat: Chat,user: User,listener: () -> Unit){
        val userList:MutableList<User> = mutableListOf();
        userList.add(user);
        val chatWithAll : ChatWithAll;
        val chatAndTrainingSpotWithAll: ChatAndTrainingSpotWithAll = ChatAndTrainingSpotWithAll(chat,null);
        val msgList : MutableList<ChatMessage> = mutableListOf();
        chatWithAll = ChatWithAll(chat,ChatWithChatMessages(chat,msgList),
            ChatWithUsers(chat,userList),chatAndTrainingSpotWithAll);
        modelChatFirebase.addChat(chatWithAll, {
            modelChatSQL.addChat(chat,{})

            // TODO: Update the local DB.

            listener();
        });
    }


    public fun addMessage(chatId: String, msg: ChatMessage, listener: () -> Unit) {
//        var refreshListener: () -> Unit = {
//            listener();
//        };
//
//        var addListener: () -> Unit = {
//            refreshAllMessages(refreshListener);
//        };
//
//        modelFirebase.addMessage(msg, addListener);
        modelChatFirebase.addMessage(chatId, msg, {
            // TODO: Update the local DB.

            listener();
        });
    }

    fun addUserToChat(chatId: String, uid: String) {
        modelChatFirebase.addUserToChat(chatId, uid, { chat: ChatWithAll ->

        });
    }

    fun createChatBetweenTwoUsers(firstUserUID: String, secondUserUID: String, listener: (chat: Chat) -> Unit) {
        modelChatFirebase.createChatBetweenTwoUsers(firstUserUID, secondUserUID, listener);
    }

    inner class ChatLiveData: MutableLiveData<List<ChatWithAll>>() {

        // Constructors
        init {
            value = LinkedList<ChatWithAll>();
        }

        // Methods
        override fun onActive() {
            super.onActive();

            val sp: SharedPreferences =
                ParkScoutApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);

            // Select
            executor.execute {
                val chats = modelChatSQL.getAllChats();
                val chatsWithChatMessages = modelChatSQL.getAllChatsWithChatMessages();
                val chatsWithUsers = modelChatSQL.getAllChatsWithUsers();
                val chatsAndTrainingSpots = modelChatSQL.getAllChatsAndTrainingSpots();
                val chatWithAllList: LinkedList<ChatWithAll> = LinkedList();
                for (chat in chats) {
                    var chatwithchatmessages: ChatWithChatMessages =
                        chatsWithChatMessages.find { it.Chat == chat }?.chatMessages?.let {
                            ChatWithChatMessages(
                                Chat = chat,
                                chatMessages = it
                            )
                        }!!
                    var chatWithUsers: ChatWithUsers =
                        chatsWithUsers.find { it.Chat == chat }?.Users?.let {
                            ChatWithUsers(
                                Chat = chat,
                                Users = it
                            )
                        }!!
                    var chatAndTrainingSpot =
                        chatsAndTrainingSpots.find { it.chat == chat }?.trainingSpot?.let {
                            ChatAndTrainingSpot(
                                chat = chat,
                                trainingSpot = it
                            )
                        };
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
                    if (chatAndTrainingSpot != null) {
                        trainingSpotWithAll =
                            chatAndTrainingSpot.trainingSpot?.parkId?.let {
                                trainingSpotModelSQL.getParkById(
                                    it
                                )
                            }!!
                    };

                    var ChatAndTrainingSpotWithAll = ChatAndTrainingSpotWithAll(
                        chat = chat,
                        trainingSpotWithAll = trainingSpotWithAll
                    )

                    var chatWithAll = chatAndTrainingSpot?.let {
                        ChatWithAll(
                            chat, chatwithchatmessages, chatWithUsers,
                            ChatAndTrainingSpotWithAll
                        )
                    };
                    if (chatWithAll != null) {
                        chatWithAllList.add(chatWithAll)
                    };
                }
                postValue(chatWithAllList);
            }

            // TODO: User the user ID to get the specific user's chats.
            modelChatFirebase.getAllChats("", { chats: List<ChatWithAll> ->
                value = chats;
            });
        }

        override fun onInactive() {
            super.onInactive();
        }
    }

    inner class ChatMessageLiveData : MutableLiveData<List<ChatMessage>>() {

        // Constructors
        init {
            value = LinkedList<ChatMessage>();
        }

        // Methods
        override fun onActive() {
            super.onActive();

            val sp: SharedPreferences =
                ParkScoutApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
            var lastUpdated: Long = sp.getLong("lastUpdated", 0);

            modelFirebase.getAllMessages(lastUpdated) { messages: List<ChatMessage> ->
                value = messages;
                Log.d("BP", "The messages were received: " + messages.size);

                for (msg in messages) {
                    modelSQL.addChatMessage(msg) {
                    };
                }
            };
        }

        override fun onInactive() {
            super.onInactive();
        }
    }
}