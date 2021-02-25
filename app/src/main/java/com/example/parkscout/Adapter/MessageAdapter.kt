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
import com.google.firebase.auth.FirebaseUser
import java.util.*

class MessageAdapter(val context: Context, var chatMessages: LinkedList<ChatMessage>, val imageURL: String): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    // Data Members
    private var mContext: Context
    private var mChatMessages: List<ChatMessage>
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
        if ( mChatMessages == null) {
            return 0;
        } else {
            return mChatMessages.size;
        }
    }

    override fun onBindViewHolder(holder: MessageAdapter.ViewHolder, position: Int) {
        var msg = mChatMessages.get(position)
        holder.show_message.text = msg?.message

        if (imageURL.equals("default")) {
            holder.profile_image.setImageResource(R.drawable.profile_icon)
        } else {
            Glide.with(mContext).load(mImageURL).into(holder.profile_image)
        }
    }

    override fun getItemViewType(position: Int): Int {
        // TODO: Uncomment when firebase is ready
//        mFBUser = FirebaseAuth.getFInstance().currentUser!!

//        if (mChatMessages[position].sender.equals(mFBUser.uid)) {
//            return MSG_TYPE_RIGHT
//        } else {
//            return MSG_TYPE_LEFT
//        }

        if (mChatMessages?.get(position)?.sender == "Tomer") {
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