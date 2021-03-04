package com.example.parkscout.Fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkscout.Adapter.ImagesAdapter
import com.example.parkscout.R
import com.example.parkscout.Repository.*
import com.example.parkscout.ViewModel.TrainingSpotViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddParkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddParkFragment : Fragment() , OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101
    private var locationPermissionGranted = false
    private var map: GoogleMap? = null
    private var lastKnownLocation: Location? = null
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var cameraPosition: CameraPosition? = null
    private lateinit var placesClient: PlacesClient
    private lateinit var park_location: TextView
    private lateinit var trainModel: TrainingSpotViewModel;
    private  var latLng= defaultLocation;
    var selectedPhotoUri: Uri? = null;
    private lateinit var mImggRecyclerView: RecyclerView
    private lateinit var mAdapter: ImagesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_add_park, container, false)
        trainModel =TrainingSpotViewModel()

        var selectPhotoBtn =
            rootView.findViewById(R.id.uploadPhotobBtn) as Button

        selectPhotoBtn.setOnClickListener {
            selectPhoto()
        }

        val saveBtn = rootView.findViewById(R.id.SaveBtn) as Button
        val searchBtn = rootView.findViewById(R.id.SearchBtn) as Button
        park_location = rootView.findViewById(R.id.locationTXT) as TextView
        mImggRecyclerView = rootView.findViewById(R.id.ImagesUploaded)
        mImggRecyclerView.setHasFixedSize(true)
        mAdapter = ImagesAdapter(this.requireContext(), LinkedList<Uri>());
        mImggRecyclerView.adapter = mAdapter
        mAdapter.notifyDataSetChanged();

        var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity?.applicationContext,LinearLayoutManager.HORIZONTAL, false)

        mImggRecyclerView.layoutManager = linearLayoutManager
        searchBtn.setOnClickListener{
            searchLocation(it);
        }

        saveBtn.setOnClickListener {

            val park_name = rootView.findViewById(R.id.ParkName) as TextView
            val parkKind =  rootView.findViewById(R.id.park_kind) as ChipGroup
            val facilities = rootView.findViewById(R.id.facilities) as TextView
            val parkLocation = com.example.parkscout.data.Types.Location(
                latLng.latitude,
                latLng.longitude
            )
            val comment : Comment = Comment("", "", "", 0)

            val rating : Rating = Rating("", "", 0, null)

            val sportTypesList : MutableList<SportTypes>  =  mutableListOf();

            val ids: List<Int> = parkKind.getCheckedChipIds()
            for (id in ids) {
                val chip: Chip = parkKind.findViewById(id)

                sportTypesList.add(SportTypes(chip.hint.toString(), chip.text.toString(), "0"))
            }
            var errorMsg = ""
            if(park_name.text.isEmpty()){
                errorMsg = "enter park name";
            }else if( parkLocation == null ){
                errorMsg = "enter location";
            }else if(sportTypesList.size == 0){
                errorMsg ="choose park kind"
            }
            if(errorMsg != "") {
                Toast.makeText(
                    getActivity()?.getApplicationContext(),
                    "$errorMsg",
                    Toast.LENGTH_LONG
                ).show()
                false;
            } else {
                var trainingSpot = TrainingSpot(
                    "0",
                    park_name.text.toString(),
                    parkLocation,
                    "",
                    facilities.text.toString()
                );
                var images: LinkedList<Images> = LinkedList<Images>();
                for (uri in mAdapter.imagesURL!!) {
                    UplaodImage(uri.toString())
                    images.add(Images("0", uri.toString()));
                }

                var park: TrainingSpotWithAll = TrainingSpotWithAll(
                    (trainingSpot),
                    TrainingSpotsWithComments(trainingSpot, null),
                    TrainingSpotWithRating(trainingSpot, null),
                    TrainingSpotWithSportTypes(trainingSpot, sportTypesList),
                    TrainingSpotWithImages(trainingSpot, images)
                )

                trainModel.addPark(park) {
                    Log.d("TAG", "Success when trying to save");
                }

                val savedMsg = "Saved"
                // TODO : initiate successful logged in experience
                Toast.makeText(
                    getActivity()?.getApplicationContext(),
                    "$savedMsg",
                    Toast.LENGTH_LONG
                ).show()

                true;
            }
        }
        // *******************************************************************************************
        val mapFragment =
            childFragmentManager.fragments[0] as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            requireActivity()
        )
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
        Places.initialize(requireContext(), getString(R.string.map_key))
       // *******************************************************************************************
        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddParkFragment.
         */
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private val TAG = AddParkFragment::class.java.simpleName
        private const val DEFAULT_ZOOM = 10
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddParkFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    public fun UplaodImage(uriString: String) {

        if (uriString == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/park_images/$filename")
        val myUri = Uri.parse(uriString)
        ref.putFile(myUri)
            .addOnSuccessListener {
                Log.d("Main", "Successfully uploaded image: ${it.metadata?.path}")

            }
            .addOnFailureListener {
                Toast.makeText(
                    getActivity(),
                    "Upload image failed: ${it.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val image = ImageView(context)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data

            selectedPhotoUri?.let { mAdapter.imagesURL?.add(it) }
            mAdapter.notifyDataSetChanged();
            mImggRecyclerView.adapter = mAdapter
        }
    }
    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }
    fun searchLocation(view: View) {
        lateinit var location: String
        location = park_location.text.toString()
        var addressList: List<Address>? = null

        if (location == null || location == "") {
            Toast.makeText(
                getActivity()?.getApplicationContext(),
                "provide location",
                Toast.LENGTH_SHORT
            ).show()
        }
        else{
            val geoCoder = Geocoder(requireContext())
            try {
                addressList = geoCoder.getFromLocationName(location, 1)

            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (addressList != null) {
                if (addressList.size > 0) {
                    val address = addressList!![0]
                     latLng = LatLng(address.latitude, address.longitude)
                    map!!.addMarker(MarkerOptions().position(latLng).title(location))
                    map!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                    Toast.makeText(
                        requireContext(),
                        address.latitude.toString() + " " + address.longitude,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this.requireContext() as Activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                }
            }
        }
        updateLocationUI()
    }

    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                    requireActivity()
                )
                val locationResult = fusedLocationProviderClient.lastLocation

                locationResult.addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }

            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!
        getLocationPermission()
        updateLocationUI()
        getDeviceLocation()

        map?.setOnMapClickListener {
            map!!.clear()
            map!!.addMarker(MarkerOptions().position(it))
            latLng = it;
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("Not yet implemented")

    }
}