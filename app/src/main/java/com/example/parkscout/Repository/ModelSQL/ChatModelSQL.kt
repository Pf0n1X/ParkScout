package com.example.parkscout.Repository.ModelSQL

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.example.parkscout.Repository.Chat
import com.example.parkscout.Repository.ChatMessage

class ChatModelSQL {
    fun getAllChats(): LiveData<List<Chat>> {
        return AppLocalDb.getInstance().chatDao().getAllChats();
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
    }