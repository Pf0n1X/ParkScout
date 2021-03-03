package com.example.parkscout

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.parkscout.Repository.TrainingSpotWithAll
import com.example.parkscout.ViewModel.TrainingSpotViewModel
import androidx.lifecycle.Observer
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class ParkDetailsActivity : AppCompatActivity() {

    // Data Members
    private lateinit var viewModelTrainingSpot: TrainingSpotViewModel;
    private lateinit var  listPark: LinkedList<TrainingSpotWithAll>

    // Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_park_full_details)
        val parkName = findViewById<TextView>(R.id.park_name_v)
        val facilities = findViewById<TextView>(R.id.equipped_with_details_v)
        val kinds = findViewById<TextView>(R.id.fits_info)
        val sportKinds : StringBuilder = StringBuilder();
        val park_id: String? = intent.getStringExtra("parkId")
        viewModelTrainingSpot = ViewModelProvider(this).get(TrainingSpotViewModel::class.java)

        listPark = LinkedList<TrainingSpotWithAll>();

        var parkToShow: TrainingSpotWithAll? =
            park_id?.let { viewModelTrainingSpot.getParkById(it).value }
        val parkListener: Observer<TrainingSpotWithAll> = Observer { parkById ->
            parkName.setText(parkById.trainingSpot.parkName);
            facilities.setText(parkById.trainingSpot.facilities);

            if(parkById.trainingSpotWithSportTypes.sport_types != null &&
                    sportKinds.isEmpty()) {
                for (kind in parkById.trainingSpotWithSportTypes.sport_types!!) {
                    sportKinds.append(kind.type_name);
                }
            }

            kinds.setText(sportKinds);
        };

        viewModelTrainingSpot.parkById.observe(this, parkListener);

        // Setup the activity's border's and cutout behaviour.
        setupBaseDesign();
    }

    fun setupBaseDesign() {
        val attr = window.attributes

        // Handle the cutout.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            attr.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        // Remove the opaque status bar.
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // Make the keyboard resize the screen upon being opened.
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
}