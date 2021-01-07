package com.example.parkscout

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val attr = window.attributes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            attr.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        setContentView(R.layout.activity_main)

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

        // Navigation
        val navController = Navigation.findNavController(this, R.id.main_navhost_frag)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        // Handle FAB navigation
        fab.setOnClickListener{view ->
//            val navController = Navigation.findNavController(bottomNavigationView)
            navController.navigate(R.id.action_global_addParkFragment)
        }

        // Handle Bottom Menu Navigation
        bottomAppBar.setNavigationOnClickListener{view ->
            Log.d("Tag", "Navigating")
//            val navController = Navigation.findNavController(view)
            when (view.id) {
                R.id.nav_search -> {
                    Log.d("Tag", "Navigating to search fragment")
                    navController.navigate(R.id.action_global_searchFragment)

                    true
                }
                R.id.nav_chat -> {
                    navController.navigate(R.id.action_global_chatFragment)

                    true
                }
                R.id.nav_profile -> {
                    navController.navigate(R.id.action_global_profileFragment)

                    true
                }
                R.id.nav_settings -> {

                    navController.navigate(R.id.action_global_settingsFragment)

                    true
                }
                else -> false
            }
        }

        setSupportActionBar(bottomAppBar)
    }
}