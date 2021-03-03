package com.example.parkscout.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.parkscout.Repository.Model.TrainingSpotModel
import com.example.parkscout.Repository.TrainingSpotWithAll
import com.example.parkscout.data.Types.TrainingSpotFirebase

class TrainingSpotViewModel: ViewModel() {
    // Data Members
    lateinit var parkList: LiveData<List<TrainingSpotWithAll>>
    lateinit var parkById: LiveData<TrainingSpotWithAll>;

    // Constructors
    init {
        this.parkList = TrainingSpotModel.instance.getAllParks();
//        this.parkById = TrainingSpotModel.instance.getParkById("");

    }

    // Methods
    fun addPark(park: TrainingSpotWithAll, listener: () -> Unit) {
        TrainingSpotModel.instance.addTrainingSpot(park, listener);
    }
    fun getParks(): List<TrainingSpotWithAll>? {
        return parkList.value;
    }
    fun getParkById(parkId:String): LiveData<TrainingSpotWithAll> {
        this.parkById =  TrainingSpotModel.instance.getParkById(parkId);
        return this.parkById;
    }


}