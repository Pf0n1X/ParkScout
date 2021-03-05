package com.example.parkscout.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkscout.Repository.Comment
import com.example.parkscout.Repository.Model.ChatModel
import com.example.parkscout.Repository.Model.TrainingSpotModel
import com.example.parkscout.Repository.Rating
import com.example.parkscout.Repository.TrainingSpotWithAll

class ParkDetailsViewModel: ViewModel() {
    // Data Members
    lateinit var trainingSpot: LiveData<TrainingSpotWithAll>
        private set;

    // Constructors
    init {
        this.trainingSpot = MutableLiveData<TrainingSpotWithAll>();
    }

    // Methods
    fun getTrainingSpotByID(trainingSpotId: String): LiveData<TrainingSpotWithAll> {
        this.trainingSpot = TrainingSpotModel.instance.getParkById(trainingSpotId);

        return this.trainingSpot;
    }

    fun addComment(parkId: String, comment: Comment, listener: () -> Int) {
        TrainingSpotModel.instance.addComment(parkId, comment, listener);
    }

    fun addRating(parkId: String, rating: Rating, listener: () -> Int) {
        TrainingSpotModel.instance.addRating(parkId, rating, listener);
    }
}