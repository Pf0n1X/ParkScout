package com.example.parkscout.Fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkscout.Adapter.MessageAdapter
import com.example.parkscout.Model.ChatMessage
import com.example.parkscout.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_chat.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }

     }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val fragmentView = inflater.inflate(R.layout.fragment_chat, container, false)

        mMsgRecyclerView = fragmentView.findViewById(R.id.chat_rvMessages)
        mMsgRecyclerView.setHasFixedSize(true)
        var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity?.applicationContext)
        mMsgRecyclerView.layoutManager = linearLayoutManager

        readMessages("Tom", "Eden", "https://mymodernmet.com/wp/wp-content/uploads/2019/09/100k-ai-faces-5.jpg")

        fragmentView.findViewById<ImageButton>(R.id.chat_ibUserImage)
                .setOnClickListener{ view ->
            val navController = Navigation
                    .findNavController(activity as Activity, R.id.main_navhost_frag)

            NavigationUI.setupWithNavController((activity as Activity)
                    .findViewById<BottomNavigationView>(R.id.bottomNavigationView), navController)

            navController.navigate(R.id.action_global_profileFragment)
        }

        return fragmentView
    }

    fun readMessages(myId: String, userId: String, imageURL: String) {
        mChatMessages = ArrayList<ChatMessage>()
        mChatMessages += ChatMessage("Tom", "Eden", "Hello")
        mChatMessages += ChatMessage("Eden", "Tom", "How are you?")
        mChatMessages += ChatMessage("Tom", "Eden", "I'm fine thank you")

//        reference = FirebaseDatabase.instance.getReference("Messages")

        // TODO: Att a valueEventListener to the db reference
        mAdapter = MessageAdapter(this.requireContext(), mChatMessages, imageURL)
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