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
import com.daimajia.swipe.SwipeLayout
import com.example.parkscout.Repository.ChatMessage
import com.example.parkscout.R
import com.example.parkscout.Repository.ChatWithAll
import com.example.parkscout.Repository.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

class MessageAdapter(val context: Context, var chatMessages: List<ChatMessage>, val imageURL: String, var chat: ChatWithAll?, var onMessageDelete: (ChatMessage) -> Unit): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    // Data Members
    private var mContext: Context
    public var mChatMessages: List<ChatMessage>
    public var mChat: ChatWithAll?;
    private var mImageURL: String
    private lateinit var mFBUser: FirebaseUser
    public var mOnMessageDelete: (ChatMessage) -> Unit;

    companion object {
        private const val MSG_TYPE_LEFT = 0
        private const val MSG_TYPE_RIGHT = 1
    }

    init {
        mContext = context
        mChatMessages = chatMessages
        mImageURL = imageURL
        mChat = chat;
        mOnMessageDelete = onMessageDelete;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View;

        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_msg_right, parent, false)
            var swipelayout: SwipeLayout = view.findViewById(R.id.msg_swipelayout);
            swipelayout.setShowMode(SwipeLayout.ShowMode.PullOut)
            var holder = MessageAdapter.ViewHolder(view);
            holder.isRight = true;

            return holder;
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_msg_left, parent, false)

            var holder =  MessageAdapter.ViewHolder(view)
            holder.isRight = false;

            return holder;
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

        if (holder.isRight) {
            holder.setupDeleteButton{
                onMessageDelete(msg);
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
        public var show_message: TextView;
        public var profile_image: ImageView;
        private var delete_image: ImageView? = null;
        public var isRight: Boolean = false;

        init {
            show_message = itemView.findViewById(R.id.msg_show_message)
            profile_image = itemView.findViewById(R.id.msg_profile_image)
        }

        public fun setupDeleteButton(onMessageDelete: () -> Unit) {
            delete_image = itemView.findViewById(R.id.msgRight_iv_delete);

            delete_image?.setOnClickListener {
                onMessageDelete();
            }
        }
    }
}