package com.example.parkscout

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.parkscout.ui.login.ChatActivity
import com.example.parkscout.Fragment.ParkDetails
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


class MainActivity :  AppCompatActivity() ,OnMapReadyCallback{
    private lateinit var mMap: GoogleMap

    private var park_fragment: Fragment? = null
    var marker: Marker? = null
    var bundle=Bundle()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

      var fl :FrameLayout = findViewById(R.id.park_layout)
        fl.setTransitionVisibility(View.INVISIBLE)
        // Setup the app and the bottom app bar UI.
        setupBaseDesign()

        // Navigation
        setupNavigation()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun setupNavigation() {

        // Bottom App Bar Navigation
        val navController = Navigation.findNavController(this, R.id.main_navhost_frag)
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

        // Add a marker in Sydney and move the camera
        val israel = LatLng(32.087621357223206, 34.791755821340146)
        val israel2 = LatLng(32.10449058653846, 34.80548873117373)
        val israel3 = LatLng(32.07365826022543, 34.77321639306481)
        val israel4 = LatLng(32.08383989573791, 34.80274214920701)
        mMap.addMarker(MarkerOptions().position(israel).title("park1"))
        mMap.addMarker(MarkerOptions().position(israel2).title("park2"))
        mMap.addMarker(MarkerOptions().position(israel3).title("park3"))
        mMap.addMarker(MarkerOptions().position(israel4).title("park4"))
      //  mMap.moveCamera(CameraUpdateFactory.newLatLng(israel))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(israel, 13f))

        mMap.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
            }
            var fl :FrameLayout = findViewById(R.id.park_layout)
            val fragment = ParkDetails()

            fl.setTransitionVisibility(View.VISIBLE)
            getSupportFragmentManager().findFragmentById(R.id.park_layout);
            bundle.putString("park_name",marker.title)
            bundle.putInt("star_num",5)
            fragment.arguments = bundle

            setFragment(fragment)

//            val args = ParkDetailsArgs.Builder(marker.title).build().toBundle()
//            val navController = Navigation.findNavController(this, R.id.park_details)
//            navController.navigate(R.id.action_global_parkDetails2, args)
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