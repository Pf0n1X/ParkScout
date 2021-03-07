package com.example.parkscout.Fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.parkscout.R
import com.example.parkscout.Repository.Chat
import com.example.parkscout.Repository.TrainingSpotWithAll
import com.example.parkscout.Repository.User
import com.example.parkscout.ViewModel.ProfileViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() , OnMapReadyCallback {

    // Data Members
    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: ProfileViewModel;
    private lateinit var mUserImageView: ImageView;
    private lateinit var mUserName: TextView;
    private lateinit var mUserDesc: TextView;
    private lateinit var mUserID: String;
    private lateinit var mBtnChat: MaterialButton;

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
        mBtnChat = rootview.findViewById(R.id.profile_chat_button);

        if (mUserID != null) {
            viewModel.getUserByID(mUserID);
            viewModel.user.observe(viewLifecycleOwner, { user: User ->
                Glide.with(activity as Activity).load(user.profilePic).into(mUserImageView);
                mUserName.text = user.name;
                mUserDesc.text = user.description;
            });
        }

        // Show or hide the button according to whether the shown user is the logged in one.
        if (mUserID == FirebaseAuth.getInstance().currentUser?.uid) {
            mBtnChat.isEnabled = false;
            mBtnChat.isVisible = false;
        } else {
            mBtnChat.isEnabled = true;
            mBtnChat.isVisible = true;
        }

        // Handle the join chat button event.
        mBtnChat.setOnClickListener{
            var loggedInUserID = FirebaseAuth.getInstance().currentUser?.uid;

            if (loggedInUserID != null) {
                viewModel.createChatBetweenTwoUsers(mUserID, loggedInUserID, { chat: Chat ->
                    Toast.makeText(context, "A chat was created successfuly.", Toast.LENGTH_SHORT)
                        .show();
                    mBtnChat.isEnabled = false;
                    mBtnChat.isVisible = false;
                });
            }
        };

        return rootview
    }
    
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney = LatLng(27.2046, 77.4977)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(sydney, 16f)
        mMap.animateCamera(cameraUpdate, 2000, null)
        val builder = LatLngBounds.Builder()

        if (mUserID != null) {
            viewModel.getParksByUser(mUserID);
            viewModel.user_parkList.observe(
                viewLifecycleOwner,
                Observer { parks: List<TrainingSpotWithAll> ->
                    if(parks.size != 0) {
                        for (park in parks) {
                            mMap.addMarker(
                                MarkerOptions().position(
                                    LatLng(
                                        park.trainingSpot.parkLocation.xscale,
                                        park.trainingSpot.parkLocation.yscale
                                    )
                                )
                                    .title(park.trainingSpot.parkName)
                            )
                            builder.include(
                                LatLng(
                                    park.trainingSpot.parkLocation.xscale,
                                    park.trainingSpot.parkLocation.yscale
                                )
                            )
                        }
                        val bounds = builder.build()
                        val padding = 0 // offset from edges of the map in pixels

                        val cu: CameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                        googleMap.moveCamera(cu);
                        googleMap.animateCamera(cu);
                    }
                });
        }

    }
}