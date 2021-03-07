package com.example.parkscout.Repository.ModelSQL

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.example.parkscout.Repository.*

class ChatModelSQL {
    fun getAllChats(): List<Chat> {
        return AppLocalDb.getInstance().chatDao().getAllChats();
    }
    fun getAllChatsWithChatMessages(): List<ChatWithChatMessages> {
        return AppLocalDb.getInstance().chatDao().getAllChatsWithChatMessages();
    }
    fun getAllChatsWithUsers(): List<ChatWithUsers> {
        return  AppLocalDb.getInstance().chatDao().getAllChatsWithUsers();
    }

    fun getAllChatsAndTrainingSpots(): List<ChatAndTrainingSpot> {
        return AppLocalDb.getInstance().chatDao().getAllChatsAndTrainingSpots();
    }

    public fun addChat(chat: Chat, listener: (() -> Unit)?) {
        class MyAsyncTask: AsyncTask<Chat, Void, Chat>() {
            override fun doInBackground(vararg params: Chat?): Chat {
                AppLocalDb.getInstance().chatDao().insert(chat);

                return chat;
            }
            override fun onPostExecute(result: Chat?) {
                super.onPostExecute(result);

                if (listener != null) {
                    listener();
                }
            }
        }

        var task: MyAsyncTask = MyAsyncTask();
        task.execute();
        }

    public fun addChatMessage(chatMessage: ChatMessage, listener: (() -> Unit)?) {
        class MyAsyncTask: AsyncTask<ChatMessage, Void, ChatMessage>() {
            override fun doInBackground(vararg params: ChatMessage?): ChatMessage {
                AppLocalDb.getInstance().chatDao().insert(chatMessage);

                return chatMessage;
            }
            override fun onPostExecute(result: ChatMessage?) {
                super.onPostExecute(result);

                if (listener != null) {
                    listener();
                }
            }
        }

        var task: MyAsyncTask = MyAsyncTask();
        task.execute();
    }

    public fun addUser(user: User, listener: (() -> Unit)?) {
        class MyAsyncTask: AsyncTask<User, Void, User>() {
            override fun doInBackground(vararg params: User?): User {
                AppLocalDb.getInstance().chatDao().insert(user);

                return user;
            }
            override fun onPostExecute(result: User?) {
                super.onPostExecute(result);

                if (listener != null) {
                    listener();
                }
            }
        }

        var task: MyAsyncTask = MyAsyncTask();
        task.execute();
    }
    public fun addUserChatRelation(userChatCrossRef: UserChatCrossRef, listener: (() -> Unit)?) {
        class MyAsyncTask: AsyncTask<UserChatCrossRef, Void, UserChatCrossRef>() {
            override fun doInBackground(vararg params: UserChatCrossRef?): UserChatCrossRef {
                AppLocalDb.getInstance().chatDao().insert(userChatCrossRef);

                return userChatCrossRef;
            }
            override fun onPostExecute(result: UserChatCrossRef?) {
                super.onPostExecute(result);

                if (listener != null) {
                    listener();
                }
            }
        }

        var task: MyAsyncTask = MyAsyncTask();
        task.execute();
    }



}