package com.example.parkscout.Fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkscout.Adapter.MessageAdapter
import com.example.parkscout.R
import com.example.parkscout.ViewModel.ChatFragmentViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.parkscout.Repository.*
import com.example.parkscout.ViewModel.ExistingChatsFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_chat.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
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
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
        arguments?.let {
            chatIndex = it.getInt("CHAT_INDEX");
            chatId = it.getString("CHAT_ID", "");
        }

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
     }

    override fun onResume() {
        super.onResume()
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
            val navController = Navigation
                    .findNavController(activity as Activity, R.id.chat_navhost_frag)

            NavigationUI.setupWithNavController((activity as Activity)
                    .findViewById<BottomNavigationView>(R.id.bottomNavigationView), navController)

            navController.navigate(R.id.action_global_profileFragment)
        }

//        val messageListener: Observer<List<ChatMessage>> = Observer { messages ->
//            mAdapter.chatMessages.clear();
//            mAdapter.chatMessages.addAll(messages);
//            mAdapter.notifyDataSetChanged();
//            mMsgRecyclerView.scrollToPosition(messages.size - 1);
//
//        };
//
//        viewModel.msgList.observe(viewLifecycleOwner, messageListener);

        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup the "Send" button operation.
        chat_btnSendMessage.setOnClickListener{event  ->
            var uid: String = FirebaseAuth.getInstance().currentUser?.uid!!;
            var msg: ChatMessage = ChatMessage("" + mChatMessages.size , chatId , uid, chat_messageInput.text.toString(), System.currentTimeMillis());
            viewModel.addMessage(chatId, msg, {
                Log.d("TAG", "Success when trying to save");
            });

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

                Glide.with(requireContext()).load(otherUser!!.profilePic).into(mIBUserImage);
            } else {
                // TODO: Show the park image or a generic image and the park's name.
            }
            mAdapter.mChat = mChat;
            mAdapter.mChatMessages = mChatMessages;
            mAdapter.notifyDataSetChanged()
        })
//        mChatMessages = viewModel.msgList;
//        mChatMessages = ArrayList<ChatMessage>()
//        mChatMessages += ChatMessage("1","Tom", "Eden", "Hello", 0)
//        mChatMessages += ChatMessage("2","Eden", "Tom", "How are you?", 0)
//        mChatMessages += ChatMessage("3","Tom", "Eden", "I'm fine thank you", 0)

//        reference = FirebaseDatabase.instance.getReference("Messages")

        // TODO: Att a valueEventListener to the db reference
        // TODO: Remove imageURL parameter.
         mAdapter = MessageAdapter(this.requireContext(), mChatMessages, imageURL, mChat);
        mMsgRecyclerView.adapter = mAdapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}