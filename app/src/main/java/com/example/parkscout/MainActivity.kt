package com.example.parkscout

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.parkscout.ui.login.ChatActivity
import com.example.parkscout.Fragment.ParkDetails
import com.example.parkscout.Repository.TrainingSpotWithAll
import com.example.parkscout.ViewModel.SearchLoctionViewModel
import com.example.parkscout.ViewModel.TrainingSpotViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import java.io.IOException
import java.util.*

class MainActivity :  AppCompatActivity() ,OnMapReadyCallback{
    private lateinit var mMap: GoogleMap

    private var park_fragment: Fragment? = null
    var marker: Marker? = null
    var bundle=Bundle()
    private lateinit var placesClient: PlacesClient
    private val viewModel: SearchLoctionViewModel by viewModels()
    private lateinit var viewModelTrainingSpot: TrainingSpotViewModel;
    private lateinit var  listPark: LinkedList<TrainingSpotWithAll>;
    private lateinit var  parkSelectedId: String;


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        Places.initialize(applicationContext, getString(R.string.map_key))
        placesClient = Places.createClient(this)
      var fl :FrameLayout = findViewById(R.id.park_layout)
        fl.setTransitionVisibility(View.INVISIBLE)


        viewModelTrainingSpot = ViewModelProvider(this).get(TrainingSpotViewModel::class.java)

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
//                 Add a markers to map
                mMap.addMarker(MarkerOptions().position(LatLng(
                park.trainingSpot.parkLocation.xscale,
                park.trainingSpot.parkLocation.yscale))
                .title(park.trainingSpot.parkName))
                parkSelectedId = park.trainingSpot.parkId;
                if(listPark.size == 0) {
                    viewModelTrainingSpot.getParks()?.let { listPark.addAll(it) };
                }

            }

        };
        viewModelTrainingSpot.parkList.observe(this, parkListener);

    }
    fun searchLocation(location:String) {
        var addressList: List<Address>? = null

        if (location == null || location == "") {
            Toast.makeText(
                this.getApplicationContext(),
                "provide location",
                Toast.LENGTH_SHORT
            ).show()
        }
        else{
            val geoCoder = Geocoder(this)
            try {
                addressList = geoCoder.getFromLocationName(location, 1)

            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (addressList != null) {
                if(addressList.size > 0) {
                    val address = addressList!![0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
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

    @RequiresApi(Build.VERSION_CODES.Q)
    fun setupNavigation() {

        // Bottom App Bar Navigation
        val navController = Navigation.findNavController(this, R.id.chat_navhost_frag)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        val nav : BottomNavigationView = findViewById(R.id.bottomNavigationView)

        nav.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.searchFragment-> {
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

            var fl :FrameLayout = findViewById(R.id.park_layout)
            fl.setTransitionVisibility(View.INVISIBLE)
        true
    }

        // Handle FAB navigation
        fab.setOnClickListener{view ->
            val chatIntent: Intent = Intent(this, ChatActivity::class.java);
            startActivity(chatIntent);
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
            bundle.putString("park_name",marker.title)
            bundle.putInt("star_num",5)
            bundle.putString("parkId",park_Id)

            fragment.arguments = bundle

            setFragment(fragment)

            true
        }

    }

    fun onMarkerClick(marker: Marker?): Boolean {

        return true
    }
    protected fun setFragment(fragment: Fragment?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        if (fragment != null) {
            fragmentTransaction.add(R.id.fragment2, fragment)
            fragmentTransaction.commit()
        }
    }
}