package com.example.parkscout.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.parkscout.Repository.ChatMessage
import com.example.parkscout.R
import com.example.parkscout.Repository.ChatWithAll
import com.example.parkscout.Repository.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

class MessageAdapter(val context: Context, var chatMessages: List<ChatMessage>, val imageURL: String, var chat: ChatWithAll?): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    // Data Members
    private var mContext: Context
    public var mChatMessages: List<ChatMessage>
    public var mChat: ChatWithAll?;
    private var mImageURL: String
    private lateinit var mFBUser: FirebaseUser

    companion object {
        private const val MSG_TYPE_LEFT = 0
        private const val MSG_TYPE_RIGHT = 1
    }

    init {
        mContext = context
        mChatMessages = chatMessages
        mImageURL = imageURL
        mChat = chat;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View;

        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_msg_right, parent, false)
            return MessageAdapter.ViewHolder(view)
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_msg_left, parent, false)
            return MessageAdapter.ViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return if ( mChatMessages == null) {
            0;
        } else {
            mChatMessages.size;
        }
    }

    override fun onBindViewHolder(holder: MessageAdapter.ViewHolder, position: Int) {
        var msg = mChatMessages.get(position)
        holder.show_message.text = msg?.message

        if (imageURL.equals("default") || mChat == null) {
            holder.profile_image.setImageResource(R.drawable.profile_icon)
        } else {
            var user: User? = mChat!!.chatWithUsers.Users.find { user: User -> user.uId == msg.sender  };
            if (user != null) {
                Glide.with(mContext).load(user.profilePic).into(holder.profile_image)
            } else {
                holder.profile_image.setImageResource(R.drawable.profile_icon)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        // If the sender is the connected user, make the message appear on the right.
        // Otherwise, show it on the left.
        if (mChatMessages?.get(position)?.sender == FirebaseAuth.getInstance().currentUser?.uid) {
            return MSG_TYPE_RIGHT
        } else {
            return MSG_TYPE_LEFT
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public var show_message: TextView
        public var profile_image: ImageView

        init {
            show_message = itemView.findViewById(R.id.msg_show_message)
            profile_image = itemView.findViewById(R.id.msg_profile_image)
        }
    }
}