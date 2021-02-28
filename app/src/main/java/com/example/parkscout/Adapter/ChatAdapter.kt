package com.example.parkscout.Adapter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.parkscout.R
import com.example.parkscout.Repository.ChatWithAll
import java.util.*

class ChatAdapter(val context: Context, var chats: LinkedList<ChatWithAll>): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    private lateinit var mContext: Context
    private var mChats: List<ChatWithAll> = chats

    init {
        mChats = chats
        mContext = context
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {
        var view: View;
            view = LayoutInflater.from(mContext).inflate(R.layout.existing_chat, parent, false)
            return ChatAdapter.ViewHolder(view)
            }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public var chat_name: TextView
        public var last_message: TextView
        public var time_last_message: TextView
        public var number_of_updates: TextView
        public var profile_image: ImageView
        public var chat_id: Int = 0;

        init {
            chat_name = itemView.findViewById(R.id.Chat_Name)
            last_message = itemView.findViewById(R.id.Last_Message)
            time_last_message = itemView.findViewById(R.id.time_last_message)
            number_of_updates = itemView.findViewById(R.id.number_of_updates)
            profile_image = itemView.findViewById(R.id.chat_pic)

            itemView.findViewById<RelativeLayout>(R.id.chat_container).setOnClickListener{ view ->

                // Navigate to the chat activity.
                val navController = Navigation.findNavController(itemView.context as Activity, R.id.chat_navhost_frag);
                var arguments: Bundle = Bundle();
                arguments.apply {
                    putInt("CHAT_ID", chat_id);
                }
                navController.navigate(R.id.action_existing_chats2_to_chatFragment2, arguments);
            };
        }
    }

    override fun getItemCount(): Int {
        return mChats.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var chat = mChats.get(position)
        var rightNow = Calendar.getInstance().timeInMillis;
        var chatTime = chat.chatWithChatMessages.chatMessages[chat.chatWithChatMessages.chatMessages.lastIndex]?.lastUpdated;
        holder.chat_id = position;
        holder.time_last_message.text = (((rightNow / 1000) - chatTime) / 60).toString() + "m";
        holder.last_message.text = chat.chatWithChatMessages.chatMessages[chat.chatWithChatMessages.chatMessages.lastIndex]?.message;
    }
}