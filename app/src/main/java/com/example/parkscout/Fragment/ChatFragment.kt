package com.example.parkscout.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkscout.Adapter.MessageAdapter
import com.example.parkscout.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.parkscout.MainActivity
import com.example.parkscout.Repository.*
import com.example.parkscout.ViewModel.ExistingChatsFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_chat.*
import java.util.*

class ChatFragment : Fragment() {
    private lateinit var mAdapter: MessageAdapter
    private lateinit var mChatMessages: List<ChatMessage>
    private lateinit var mMsgRecyclerView: RecyclerView
    private lateinit var viewModel: ExistingChatsFragmentViewModel;
    private lateinit var mTVUserName: TextView;
    private lateinit var mIBUserImage: ImageButton;
    private var mChat: ChatWithAll? = null;
    private var chatIndex: Int = 0;
    private var chatId: String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        arguments?.let {
            chatIndex = it.getInt("CHAT_INDEX");
            chatId = it.getString("CHAT_ID", "");
        }

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
     }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val fragmentView = inflater.inflate(R.layout.fragment_chat, container, false)
        mChatMessages = LinkedList<ChatMessage>();
        mChat = null;
        mMsgRecyclerView = fragmentView.findViewById(R.id.chat_rvMessages)
        mTVUserName = fragmentView.findViewById<TextView>(R.id.chat_tvName);
        mIBUserImage = fragmentView.findViewById<ImageButton>(R.id.chat_ibUserImage);
        mMsgRecyclerView.setHasFixedSize(true)
        var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity?.applicationContext)
        mMsgRecyclerView.layoutManager = linearLayoutManager
        viewModel = ViewModelProvider(this).get(ExistingChatsFragmentViewModel::class.java);

        // TODO: Remove parameters.
        readMessages("Tom", "Eden", "https://mymodernmet.com/wp/wp-content/uploads/2019/09/100k-ai-faces-5.jpg")

        // TODO: Handle user profile navigation.
        fragmentView.findViewById<ImageButton>(R.id.chat_ibUserImage)
                .setOnClickListener{ view ->
//            val navController = Navigation
//                    .findNavController(activity as Activity, R.id.chat_navhost_frag)
//
//            NavigationUI.setupWithNavController((activity as Activity)
//                    .findViewById<BottomNavigationView>(R.id.bottomNavigationView), navController)
//
//            navController.navigate(R.id.action_global_profileFragment)
//                    var intent: Intent = Intent(context, MainActivity::class.java);
//                    startActivity(intent)

                    if (activity != null) {
//                        (activity as Activity).onBackPressed();
//                        (activity as Activity).onBackPressed();
                        var resultIntent: Intent = Intent();
                        var otherUserUID: String? = mChat?.chatWithUsers?.Users?.find{user: User -> user.uId != FirebaseAuth.getInstance().currentUser?.uid}?.uId

                        if (otherUserUID != null) {
                            resultIntent.putExtra("user_id", otherUserUID);
                            (activity as Activity).setResult(Activity.RESULT_OK, resultIntent);
                        }

                        (activity as Activity).finish();
                    }

        }

        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Setup the "Send" button operation.
        chat_btnSendMessage.setOnClickListener{event  ->
            var uid: String ? = FirebaseAuth.getInstance().currentUser?.uid;

            if (uid != null) {
                var msg: ChatMessage = ChatMessage(
                    "" + mChatMessages.size,
                    chatId,
                    uid,
                    chat_messageInput.text.toString(),
                    System.currentTimeMillis()
                );
                viewModel.addMessage(chatId, msg, {
                    Log.d("TAG", "Success when trying to save");
                });
            }

            chat_messageInput.setText("");
        };
    }

    fun readMessages(myId: String, userId: String, imageURL: String) {
        viewModel.chatList.observe(viewLifecycleOwner, { chats: List<ChatWithAll> ->
            mChatMessages = chats[chatIndex].chatWithChatMessages.chatMessages;
            mChat = chats[chatIndex];

            if (mChat!!.chat.training_spot_id == "") {
                // TODO: Show the other user's image and name.
                var otherUser: User? = mChat!!.chatWithUsers.Users.find { user: User -> user.uId != FirebaseAuth.getInstance().currentUser?.uid };
                var otherUserName: String? = otherUser?.name;

                if (otherUserName != null) {
                    mTVUserName.text = otherUserName
                }

                if (otherUser != null) {
                    Glide.with(requireContext()).load(otherUser.profilePic).into(mIBUserImage);
                }
            } else {
                // TODO: Show the park image or a generic image and the park's name.
            }
            mAdapter.mChat = mChat;
            mAdapter.mChatMessages = mChatMessages;
            mAdapter.notifyDataSetChanged();
            mMsgRecyclerView.scrollToPosition(mChatMessages.size - 1);
        })

        // TODO: Att a valueEventListener to the db reference
        // TODO: Remove imageURL parameter.
         mAdapter = MessageAdapter(this.requireContext(), mChatMessages, imageURL, mChat);
        mMsgRecyclerView.adapter = mAdapter
    }
}