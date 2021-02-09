package com.example.parkscout.Repository.ModelSQL

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.example.parkscout.Repository.ChatMessage

class ChatMessageModelSQL {
    fun getAllMessages(): LiveData<List<ChatMessage>> {
        return AppLocalDb.getInstance().chatMessageDao().getAllMessages();
    }

//    public interface AddChatMessageListener {
//        fun onComplete();
//    }

    public fun addChatMessage(message: ChatMessage, listener: (() -> Unit)?) {
        class MyAsyncTask: AsyncTask<ChatMessage, Void, ChatMessage>() {
            override fun doInBackground(vararg params: ChatMessage?): ChatMessage {
                AppLocalDb.getInstance().chatMessageDao().insert(message);

                return message;
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
}