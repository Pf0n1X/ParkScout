package com.example.parkscout.Adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.parkscout.Model.Message

class MessageAdapter(val context: Context, val messages: List<Message>): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    // Data Members
    private val mContext: Context
    private val mMessages: List<Message>

    init {
        mContext = context
        mMessages = messages
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}