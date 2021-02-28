package com.example.parkscout.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

        init {
            chat_name = itemView.findViewById(R.id.Chat_Name)
            last_message = itemView.findViewById(R.id.Last_Message)
            time_last_message = itemView.findViewById(R.id.time_last_message)
            number_of_updates = itemView.findViewById(R.id.number_of_updates)
            profile_image = itemView.findViewById(R.id.chat_pic)
        }
    }

    override fun getItemCount(): Int {
        return mChats.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var chat = mChats.get(position)
        var rightNow = Calendar.getInstance().timeInMillis;
        var chatTime = chat.chatWithChatMessages.chatMessages[chat.chatWithChatMessages.chatMessages.lastIndex]?.lastUpdated;
        holder.time_last_message.text = (((rightNow / 1000) - chatTime) / 60).toString() + "m";
        holder.last_message.text = chat.chatWithChatMessages.chatMessages[chat.chatWithChatMessages.chatMessages.lastIndex]?.message;
    }
}