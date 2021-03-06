package com.example.parkscout.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkscout.Repository.Comment
import com.example.parkscout.Repository.Model.ChatModel
import com.example.parkscout.Repository.Model.TrainingSpotModel
import com.example.parkscout.Repository.Model.UserModel
import com.example.parkscout.Repository.Rating
import com.example.parkscout.Repository.TrainingSpotWithAll
import com.example.parkscout.Repository.User
import com.google.firebase.auth.FirebaseAuth

class ParkDetailsViewModel: ViewModel() {
    // Data Members
    lateinit var trainingSpot: LiveData<TrainingSpotWithAll>
        private set;
    lateinit var userList: LiveData<List<User>>
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

    fun joinChat(listener: (TrainingSpotWithAll?) -> Unit) {
        var uid: String? = FirebaseAuth.getInstance().currentUser?.uid;
        var chatId: String? = trainingSpot.value?.trainingSpot?.chatId;

        if (uid != null && chatId != null && chatId != "") {
            ChatModel.instance.addUserToChat(chatId, uid);
        }
    }

    fun addRating(parkId: String, rating: Rating, listener: () -> Int) {
        TrainingSpotModel.instance.addRating(parkId, rating, listener);
    }

    fun getAllUsers(): LiveData<List<User>> {
        this.userList = UserModel.instance.getAllUsers()

        return this.userList;
    }
}