package com.example.parkscout.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkscout.Adapter.ChatAdapter
import com.example.parkscout.Adapter.MessageAdapter
import com.example.parkscout.R
import com.example.parkscout.Repository.Chat
import com.example.parkscout.Repository.ChatMessage
import com.example.parkscout.Repository.ChatWithAll
import com.example.parkscout.Repository.User
import com.example.parkscout.ViewModel.ChatFragmentViewModel
import com.example.parkscout.ViewModel.ExistingChatsFragmentViewModel
import java.util.*

class Existing_chats : Fragment() {

    // Data Members
    private lateinit var mAdapter: ChatAdapter;
    private lateinit var mChats: LiveData<List<ChatWithAll>>
    private lateinit var mMsgRecyclerView: RecyclerView
    private lateinit var viewModel: ExistingChatsFragmentViewModel;
    private lateinit var mSearchInput: EditText;

    // Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentView = inflater.inflate(R.layout.fragment_existing_chats, container, false);
        var linearLayoutManager: LinearLayoutManager =
            LinearLayoutManager(activity?.applicationContext)
        viewModel = ViewModelProvider(this).get(ExistingChatsFragmentViewModel::class.java);
        mChats = viewModel.chatList;
        mSearchInput = fragmentView.findViewById(R.id.chat_search_input);
        mMsgRecyclerView = fragmentView.findViewById(R.id.Existing_chats_chats)
        mMsgRecyclerView.setHasFixedSize(true)
        mMsgRecyclerView.layoutManager = linearLayoutManager
        var list: LinkedList<ChatWithAll> = LinkedList<ChatWithAll>();
        list.addAll(mChats.value!!);
        mAdapter = ChatAdapter(this.requireContext(), list);
        mMsgRecyclerView.adapter = mAdapter;
        mAdapter.notifyDataSetChanged();

        mMsgRecyclerView.scrollToPosition(mChats.value!!.size - 1);

        val chatListener: Observer<List<ChatWithAll>> = Observer { chats ->
            mAdapter.chats.clear();
            mAdapter.chats.addAll(chats);
            mAdapter.notifyDataSetChanged();
//            mMsgRecyclerView.scrollToPosition(chats.size - 1);
        };

        viewModel.chatList.observe(viewLifecycleOwner, chatListener);

        mSearchInput.doAfterTextChanged { text ->
            filterChats(text.toString().toLowerCase());
        }

        return fragmentView
    }

    private fun filterChats(searchQuery: String) {
        var filteredList: List<ChatWithAll>? = mChats.value?.toMutableList();

        if (filteredList == null)
            return;

        filteredList = filteredList.filter { it: ChatWithAll ->
            var foundUser: User? = it.chatWithUsers.Users.find { user: User -> user.name.toLowerCase().contains(searchQuery) }

            if (foundUser != null) {
                true;
            } else {
                false;
            }
        }

        mAdapter.chats.clear();
        mAdapter.chats.addAll(filteredList);
        mAdapter.notifyDataSetChanged();
    }
}