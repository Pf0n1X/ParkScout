package com.example.parkscout.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkscout.Adapter.CommentAdapter
import com.example.parkscout.R
import com.example.parkscout.Repository.Comment
import com.example.parkscout.Repository.TrainingSpotWithAll
import com.example.parkscout.ViewModel.ExistingChatsFragmentViewModel
import com.example.parkscout.ViewModel.ParkDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_park_details.*
import java.util.*

private const val PARK_NAME = "park name"
private const val  STAR_RATE = 1

class ParkDetails : Fragment()   {

    // Data Members
    private lateinit var mBtnExpand: Button;
    private lateinit var mContainer: LinearLayout;
    private lateinit var mCommentsRecyclerView: RecyclerView;
    private lateinit var viewModel: ParkDetailsViewModel;
    private lateinit var mCommentAdapter: CommentAdapter

    // Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_park_details, container, false)

        mContainer = rootView.findViewById(R.id.park_details_container);
        mCommentsRecyclerView = rootView.findViewById(R.id.park_details_rv_comments);
        setupCommentsRecyclerView();
        mContainer.isVisible = false;
        var bottomSheetBehavior: BottomSheetBehavior<LinearLayout> = BottomSheetBehavior.from(mContainer);
        viewModel = ViewModelProvider(this).get(ParkDetailsViewModel::class.java);

        // Inflate the layout for this fragment
        return rootView
    }

    fun setDetails(title: String, starNum: Int, parkId: String) {
        park_details_park_name.text = title;
        park_details_rating.numStars = starNum;
        mContainer.isVisible = true;



        var listener = { spot: TrainingSpotWithAll? ->
            mCommentAdapter.mComments = spot?.getComments()!!;
            mCommentAdapter.notifyDataSetChanged();
        }

        if (viewModel.trainingSpot.value != null) {
            listener(viewModel.trainingSpot.value);
        }

        viewModel.getTrainingSpotByID(parkId).observe(viewLifecycleOwner, listener);
    }

    fun setupCommentsRecyclerView() {
        mCommentsRecyclerView.setHasFixedSize(true)
        var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity?.applicationContext)
        mCommentsRecyclerView.layoutManager = linearLayoutManager
        this.mCommentAdapter = CommentAdapter(requireContext());
        mCommentsRecyclerView.adapter = mCommentAdapter;
    }
}