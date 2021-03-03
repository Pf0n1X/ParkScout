package com.example.parkscout.Fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.parkscout.R
import com.example.parkscout.ViewModel.ExistingChatsFragmentViewModel
import com.example.parkscout.ViewModel.ProfileViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.example.parkscout.Repository.User

class ProfileFragment : Fragment() , OnMapReadyCallback {

    // Data Members
    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: ProfileViewModel;
    private lateinit var mUserImageView: ImageView;
    private lateinit var mUserName: TextView;
    private lateinit var mUserDesc: TextView;
    private lateinit var mUserID: String;

    // Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUserID = FirebaseAuth.getInstance().currentUser?.uid ?: "";
        arguments?.let {
            mUserID = it.getString("user_id", "");
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
           val rootview: View = inflater.inflate(R.layout.fragment_profile, container, false)
        try {
            val mapFragment = getChildFragmentManager().findFragmentById(R.id.mainMap) as SupportMapFragment?
            mapFragment!!.getMapAsync(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java);
        mUserImageView = rootview.findViewById<ImageView>(R.id.profile_user_image);
        mUserDesc = rootview.findViewById<TextView>(R.id.profile_user_desc);
        mUserName = rootview.findViewById<TextView>(R.id.profile_user_name);

        if (mUserID != null) {
            viewModel.getUserByID(mUserID);
            viewModel.user.observe(viewLifecycleOwner, { user: User ->
                Glide.with(activity as Activity).load(user.profilePic).into(mUserImageView);
                mUserName.text = user.name;
                mUserDesc.text = user.description;
            });
        }

        return rootview
    }
    
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney = LatLng(27.2046, 77.4977)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(sydney, 16f)
        mMap.animateCamera(cameraUpdate, 2000, null)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
    }
}