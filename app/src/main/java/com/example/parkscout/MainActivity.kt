package com.example.parkscout


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.parkscout.Fragment.AddParkFragment
import com.example.parkscout.Fragment.ParkDetails
import com.example.parkscout.Repository.TrainingSpotWithAll
import com.example.parkscout.Repository.User
import com.example.parkscout.ViewModel.SearchLoctionViewModel
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.text.DecimalFormat
import java.util.*

class MainActivity :  AppCompatActivity() ,OnMapReadyCallback{

    companion object {
        private var CHAT_ACTIVITY_CODE: Int = 1;
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private val TAG = AddParkFragment::class.java.simpleName
        private const val DEFAULT_ZOOM = 7
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
    }

    private lateinit var mMap: GoogleMap

    private var park_fragment: Fragment? = null
    var marker: Marker? = null
    var bundle=Bundle()
    private lateinit var placesClient: PlacesClient
    private val viewModel: SearchLoctionViewModel by viewModels()
    private lateinit var viewModelTrainingSpot: TrainingSpotViewModel;
    private lateinit var  listPark: LinkedList<TrainingSpotWithAll>;
    private lateinit var  parkSelectedId: String;
    private lateinit var navController: NavController;
    private lateinit var mFrameLayout: FrameLayout;
    private var lastKnownLocation: Location? = null
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var locationPermissionGranted = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var cameraPosition: CameraPosition? = null
    private lateinit var mUserID: String;
    private  var mDistanceFromSetting:Int = 0;

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set map
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        Places.initialize(applicationContext, getString(R.string.map_key))
        placesClient = Places.createClient(this)
        mFrameLayout = findViewById(R.id.park_layout)
//        mFrameLayout.visibility = View.INVISIBLE;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            this
        )
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(MainActivity.KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(MainActivity.KEY_CAMERA_POSITION)
        }

        viewModelTrainingSpot = ViewModelProvider(this).get(TrainingSpotViewModel::class.java)

        // get user
        mUserID = FirebaseAuth.getInstance().currentUser?.uid ?: "";
        if (mUserID != null) {
            viewModelTrainingSpot.getUserByID(mUserID);
            viewModelTrainingSpot.user.observe(this, { user: User ->
                mDistanceFromSetting = user.distance
            });
        }
        // Setup the app and the bottom app bar UI.
        setupBaseDesign()

        // Navigation
        setupNavigation()
        viewModel.selectedItem.observe(this, Observer { item ->
            searchLocation(item)

        })
        listPark = LinkedList<TrainingSpotWithAll>();

        val parkListener: Observer<List<TrainingSpotWithAll>> = Observer { parks ->
            for (park in parks){
                        parkSelectedId = park.trainingSpot.parkId;
                        if (listPark.size == 0) {
                            viewModelTrainingSpot.getParks()?.let { listPark.addAll(it) };
                        }
                    }

        };
        viewModelTrainingSpot.parkList.observe(this, parkListener);

    }
    fun CalculationByDistance(StartP: LatLng, EndP: LatLng): Int {
        val Radius = 6371 // radius of earth in Km
        val lat1 = StartP.latitude
        val lat2 = EndP.latitude
        val lon1 = StartP.longitude
        val lon2 = EndP.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = (Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + (Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2)))
        val c = 2 * Math.asin(Math.sqrt(a))
        val valueResult = Radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec: Int = Integer.valueOf(newFormat.format(km))
        val meter = valueResult % 1000
        val meterInDec: Int = Integer.valueOf(newFormat.format(meter))
        Log.i(
            "Radius Value", "" + valueResult + "   KM  " + kmInDec
                    + " Meter   " + meterInDec
        )
        return kmInDec;
//        return Radius * c
    }
    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                mMap?.isMyLocationEnabled = true
                mMap?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                mMap?.isMyLocationEnabled = false
                mMap?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                MainActivity.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
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
                    this
                )
                val locationResult = fusedLocationProviderClient.lastLocation

                locationResult.addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            mMap?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )
                            showParksByRadius();
                        }
                    } else {
                        mMap?.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, MainActivity.DEFAULT_ZOOM.toFloat())
                        )
                        mMap?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }

            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    fun showParksByRadius(){

        for (park in listPark) {
            if (lastKnownLocation != null) {
                // check point in radius
                val distance = CalculationByDistance(
                    LatLng(
                        lastKnownLocation!!.latitude,
                        lastKnownLocation!!.longitude
                    ),
                    LatLng(
                        park.trainingSpot.parkLocation.xscale,
                        park.trainingSpot.parkLocation.yscale
                    )
                )
                if (distance <= mDistanceFromSetting) {
                    mMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                park.trainingSpot.parkLocation.xscale,
                                park.trainingSpot.parkLocation.yscale
                            )
                        )
                            .title(park.trainingSpot.parkName)
                    )
                }
            }
        }
    }

    fun searchLocation(location: String) {
        var addressList: List<Address>? = null

        if (location == null || location == "") {
            Toast.makeText(
                this.getApplicationContext(),
                "provide location",
                Toast.LENGTH_SHORT
            ).show()
        }
        else{
            var parkToShow: List<TrainingSpotWithAll>? = viewModelTrainingSpot.getParkByName(
                location
            );
            val parkListener: Observer<List<TrainingSpotWithAll>> = Observer { parks ->
                if (parks.size != 0){
                    for(park in parks){
                        val latLng = LatLng(
                            park.trainingSpot.parkLocation.xscale,
                            park.trainingSpot.parkLocation.yscale
                        )
//                        mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
                        mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))

                    }
                }else   {
                    val geoCoder = Geocoder(this)
                    try {
                        addressList = geoCoder.getFromLocationName(location, 1)

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    if (addressList != null) {
                        if(addressList!!.size > 0) {
                            val address = addressList!![0]
                            val latLng = LatLng(address.latitude, address.longitude)
//                            mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
                            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                            Toast.makeText(
                                this,
                                address.latitude.toString() + " " + address.longitude,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                }
            viewModelTrainingSpot.parkByName.observe(this, parkListener);

        };


    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun setupNavigation() {

        // Bottom App Bar Navigation
        navController = Navigation.findNavController(this, R.id.chat_navhost_frag)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        val nav : BottomNavigationView = findViewById(R.id.bottomNavigationView)

        nav.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.searchFragment -> {
                    navController
                        .navigate(R.id.action_global_searchFragment)
                }
                R.id.settingsFragment -> {
                    navController
                        .navigate(R.id.action_global_settingsFragment)
                }
                R.id.chatFragment -> {
                    navController
                        .navigate(R.id.action_global_chatFragment)
                }
                R.id.profileFragment -> {
                    navController
                        .navigate(R.id.action_global_profileFragment)
                }
                R.id.addParkFragment -> {
                    navController
                        .navigate(R.id.action_global_addParkFragment)
                }
            }

//            var fl :FrameLayout = findViewById(R.id.park_layout)
//            fl.visibility = View.INVISIBLE
        true
    }

        // Handle FAB navigation
        fab.setOnClickListener{ view ->
            val chatIntent: Intent = Intent(this, ChatActivity::class.java);
            startActivityForResult(chatIntent, Companion.CHAT_ACTIVITY_CODE);
            overridePendingTransition(R.anim.slide_out_bottom, R.anim.nothing);
        }
    }

    fun setupBaseDesign() {
        val attr = window.attributes

        // Handle the cutout.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            attr.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        // Remove the weird shadow on the menu items.
        bottomNavigationView.background = null

        // Disable the middle placeholder button.
        bottomNavigationView.menu.getItem(2).isEnabled = false;

        // Set Rounded corners
        val radius: Float = resources.getDimension(R.dimen.default_corner_radius);
        val bottomBarBackground: MaterialShapeDrawable =
            bottomAppBar.background as MaterialShapeDrawable;

        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel.toBuilder()
            .setTopRightCorner(CornerFamily.ROUNDED, radius)
            .setTopLeftCorner(CornerFamily.ROUNDED, radius)
            .setBottomRightCorner(CornerFamily.ROUNDED, radius)
            .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getLocationPermission()
        updateLocationUI()
        getDeviceLocation()
//        googleMap?.animateCamera(CameraUpdateFactory.zoomTo(17F),200, null)
//        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(israel, 13f))

        mMap.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
            }
            var park_Id = parkSelectedId;

            var fl :FrameLayout = findViewById(R.id.park_layout)
            val fragment = ParkDetails()

            fl.setTransitionVisibility(View.VISIBLE)
            getSupportFragmentManager().findFragmentById(R.id.park_layout);
            bundle.putString("park_name", marker.title)
            bundle.putInt("star_num", 5)
            bundle.putString("parkId", park_Id)

            fragment.arguments = bundle

//            setFragment(fragment)
            showSelectedParkDetails(marker.title, 5, park_Id);

            true
        }

    }

    fun onMarkerClick(marker: Marker?): Boolean {
        return true
    }

    private fun showSelectedParkDetails(title: String, star_num: Int, park_Id: String) {
        var fragment: Fragment? = getSupportFragmentManager().findFragmentById(R.id.fragment2);

        if (fragment != null) {
            var detailsFrag: ParkDetails = fragment as ParkDetails;
            detailsFrag.setDetails(title, star_num, park_Id);
        }
    }

    protected fun setFragment(fragment: Fragment?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        if (fragment != null) {
//            var prevFrag: Fragment = fragment.findF
            fragmentTransaction.replace(R.id.fragment2, fragment)
            fragmentTransaction.commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        when (requestCode) {
            Companion.CHAT_ACTIVITY_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val returnValue = data?.getStringExtra("user_id");
                    var arguments = Bundle();
                    arguments.putString("user_id", returnValue);
                    navController
                        .navigate(R.id.action_global_profileFragment, arguments);
                }
            }
        }
    }
}