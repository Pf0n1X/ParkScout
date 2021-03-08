package com.example.parkscout.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.parkscout.R
import com.example.parkscout.Repository.Comment
import com.example.parkscout.Repository.Model.UserModel
import com.google.firebase.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CommentAdapter(val context: Context): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    // Data Members
    public var mComments: List<Comment>;
    private var mContext: Context;

    // Constructors
    init {
        this.mComments = LinkedList<Comment>();
        this.mContext = context;
    }

    // Methods
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return CommentAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.comment, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var comment = mComments.get(position)
        holder.mTVCommentText.text = comment.c_text;
        var user = UserModel.instance.getUserByID(comment.userId);
//        var curDate: Date = Timestamp(comment.time, 0).toDate();
//        var dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm")
//        holder.mTVTime.text = dateFormat.format(curDate);

        // TODO: Add the user to the comment object and get the data.
        // TODO: Show the user name and image.
    //        holder.mTVUserName.text = comment.
        holder.mTVUserName.text = user.value?.name;
        Glide.with(context).load(user.value?.profilePic).into(holder.mIVProfileImage);
    }

    override fun getItemCount(): Int {
        return if ( mComments == null) {
            0;
        } else {
            mComments.size;
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public var mTVCommentText: TextView;
        public var mIVProfileImage: ImageView;
        public var mTVUserName: TextView;
//        public var mTVTime: TextView;

        init {
            mTVCommentText = itemView.findViewById(R.id.comment_msg_content);
            mIVProfileImage = itemView.findViewById(R.id.comment_profile_image);
            mTVUserName = itemView.findViewById(R.id.comment_name);
//            mTVTime = itemView.findViewById(R.id.comment_time);
        }
    }
}