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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() , OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: ProfileViewModel;
    private lateinit var mUserImageView: ImageView;
    private lateinit var mUserName: TextView;
    private lateinit var mUserDesc: TextView;
    private lateinit var mUserID: String;

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
            });
        }

        return rootview
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney = LatLng(27.2046, 77.4977)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(sydney, 16f)
        mMap.animateCamera(cameraUpdate, 2000, null)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
    }
}