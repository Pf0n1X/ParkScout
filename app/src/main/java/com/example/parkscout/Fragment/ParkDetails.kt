package com.example.parkscout.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.parkscout.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_park_details.*

private const val PARK_NAME = "park name"
private const val  STAR_RATE = 1

class ParkDetails : Fragment()   {

    // Data Members
    private lateinit var mBtnExpand: Button;
    private lateinit var mContainer: LinearLayout;
    
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
        mContainer.isVisible = false;
        var bottomSheetBehavior: BottomSheetBehavior<LinearLayout> = BottomSheetBehavior.from(mContainer);

        // Inflate the layout for this fragment
        return rootView
    }

    fun setDetails(title: String, starNum: Int, parkId: String) {
        park_details_park_name.text = title;
        park_details_rating.numStars = starNum;
        mContainer.isVisible = true;
    }
}