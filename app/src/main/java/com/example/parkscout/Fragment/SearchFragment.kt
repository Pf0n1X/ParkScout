package com.example.parkscout.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.parkscout.R
import com.example.parkscout.ViewModel.SearchLoctionViewModel

class SearchFragment : Fragment() {

    // Data Members
    private val viewModel: SearchLoctionViewModel by activityViewModels()

    // Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_search, container, false)
        val searchText = rootView.findViewById(R.id.searchText) as  TextView
        val searchBtn = rootView.findViewById(R.id.searchBtnPark) as ImageView
        searchBtn.setOnClickListener {
            viewModel.selectItem(searchText.text.toString())
        }

        return rootView;
    }
}