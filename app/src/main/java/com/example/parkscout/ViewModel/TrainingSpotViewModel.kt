package com.example.parkscout.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.parkscout.Repository.Model.TrainingSpotModel
import com.example.parkscout.Repository.TrainingSpotWithAll

class TrainingSpotViewModel: ViewModel() {
    // Data Members
    lateinit var parkList: LiveData<List<TrainingSpotWithAll>>
//    private lateinit var trainModel: TrainingSpotModel;

    // Constructors
    init {
        this.parkList = TrainingSpotModel.instance.getAllParks();
    }

    // Methods
    fun addPark(park: TrainingSpotWithAll, listener: () -> Unit) {
        TrainingSpotModel.instance.addTrainingSpot(park, listener);
    }
    fun getParks(parks:TrainingSpotWithAll,listener: () -> Unit){

    }



}