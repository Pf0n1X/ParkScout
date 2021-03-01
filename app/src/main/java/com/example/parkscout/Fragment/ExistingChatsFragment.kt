package com.example.parkscout.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.parkscout.ViewModel.ChatFragmentViewModel
import com.example.parkscout.ViewModel.ExistingChatsFragmentViewModel
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [Existing_chats.newInstance] factory method to
 * create an instance of this fragment.
 */
class Existing_chats : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mAdapter: ChatAdapter;
    private lateinit var mChats: LiveData<List<ChatWithAll>>
    private lateinit var mMsgRecyclerView: RecyclerView
    private lateinit var viewModel: ExistingChatsFragmentViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
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
            mMsgRecyclerView.scrollToPosition(chats.size - 1);
        };

        viewModel.chatList.observe(viewLifecycleOwner, chatListener);

        return fragmentView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Existing_chats.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Existing_chats().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}