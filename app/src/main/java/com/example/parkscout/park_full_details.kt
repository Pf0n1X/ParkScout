package com.example.parkscout

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.parkscout.Repository.TrainingSpotWithAll
import com.example.parkscout.ViewModel.TrainingSpotViewModel
import androidx.lifecycle.Observer


import java.util.*

class park_full_details : AppCompatActivity() {
    private lateinit var viewModelTrainingSpot: TrainingSpotViewModel;
    private lateinit var  listPark: LinkedList<TrainingSpotWithAll>
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
    }
}