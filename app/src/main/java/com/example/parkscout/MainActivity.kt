package com.example.parkscout

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

////            window.setDecorFitsSystemWindows(false)
////            val controller = window.insetsController
////
////            if (controller != null) {
////                controller.hide(WindowInsets.Type.statusBars())
////                controller.hide(WindowInsets.Type.navigationBars())
////                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
////            }
//
        val attr = window.attributes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            window.statusBarColor = getColor(android.R.color.transparent)
            attr.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            Log.d("Tag", "Hello")
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
    }
}