package com.example.parkscout

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.parkscout.Fragment.ParkDetails
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() ,OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var park_fragment: Fragment? = null

    var marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        // Setup the app and the bottom app bar UI.
        setupBaseDesign()

        // Navigation
        setupNavigation()


    }

    fun setupNavigation() {

        // Bottom App Bar Navigation
        val navController = Navigation.findNavController(this, R.id.main_navhost_frag)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        // Handle FAB navigation
        fab.setOnClickListener{ view ->
            navController.navigate(R.id.action_global_addParkFragment)
        }
    }

    fun setupBaseDesign() {
        val attr = window.attributes

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            attr.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
            }


            true
        }

    }

    fun onMarkerClick(marker: Marker?): Boolean {
//        park_fragment = ParkDetails.getInstance();
//        park_fragment?.let {
//            getSupportFragmentManager()
//                    .beginTransaction()
//              .add(R.id.frame_layout,it)
//                    .commit()
//        };
        return true
    }
}
