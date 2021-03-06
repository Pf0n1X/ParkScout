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
import com.bumptech.glide.Glide
import com.example.parkscout.R
import com.example.parkscout.Repository.ChatWithAll
import com.example.parkscout.Repository.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import java.sql.Time
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(val context: Context, var chats: LinkedList<ChatWithAll>): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    private lateinit var mContext: Context
    public var mChats: List<ChatWithAll> = chats

    init {
        mChats = chats
        mContext = context
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {
        var view: View;
        view = LayoutInflater.from(mContext).inflate(R.layout.existing_chat, parent, false);

        return ChatAdapter.ViewHolder(view);
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public var chat_name: TextView
        public var last_message: TextView
        public var time_last_message: TextView
        public var number_of_updates: TextView
        public var profile_image: ImageView
        public var chat_id: String = "";
        public var chat_index: Int = 0;

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
                    putString("CHAT_ID", chat_id);
                    putInt("CHAT_INDEX", chat_index);
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

        // If it is a personal chat show details about the other user.
        if (chat.chat.training_spot_id == "") {
            var otherUser: User? =
                chat.chatWithUsers.Users.find { user: User -> user.uId != FirebaseAuth.getInstance().currentUser?.uid }
            if (otherUser != null) {
                holder.chat_name.text = otherUser?.name;
                Glide.with(holder.itemView).load(otherUser.profilePic).into(holder.profile_image);
            }
        } else {
            var images = chat.chatAndTrainingSpot.trainingSpotWithAll?.trainingSpotWithImages?.images;
            if (images != null && !(images.isEmpty())) {
                var imgURL =
                    images.get(0)?.ImgUrl;

                if (imgURL != null) {
                    Glide.with(holder.itemView).load(imgURL).into(holder.profile_image);
                }
            }

            var parkName = chat.chatAndTrainingSpot.trainingSpotWithAll?.trainingSpot?.parkName;
            holder.chat_name.text = parkName;
        }

        holder.chat_id = chat.chatWithChatMessages.Chat.chatId;
        holder.chat_index = position;
        if (chat.chatWithChatMessages.chatMessages.size != 0) {
            var curDate: Date = Timestamp(
                chat.chatWithChatMessages.chatMessages[chat.chatWithChatMessages.chatMessages.size - 1].last_updated,
                0
            ).toDate();
            var dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm")
            holder.time_last_message.text = dateFormat.format(curDate);
            holder.last_message.text =
                chat.chatWithChatMessages.chatMessages[chat.chatWithChatMessages.chatMessages.lastIndex]?.message;
        } else {
            holder.last_message.text = "No messages yet.";
            holder.time_last_message.text = "";
        }
    }
}