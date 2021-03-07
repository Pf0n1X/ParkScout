package com.example.parkscout.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.parkscout.Repository.Chat
import com.example.parkscout.Repository.Model.ChatModel
import com.example.parkscout.Repository.Model.TrainingSpotModel
import com.example.parkscout.Repository.Model.UserModel
import com.example.parkscout.Repository.TrainingSpotWithAll
import com.example.parkscout.Repository.User

class ProfileViewModel: ViewModel() {

    // Data Members
    lateinit var user_parkList: LiveData<List<TrainingSpotWithAll>>
    public lateinit var user: LiveData<User>
        private set;

    // Constructors
    init {
        this.user = UserModel.instance.getUser();
    }

    // Methods
    public fun getUserByID(uid: String) {
        this.user = UserModel.instance.getUserByID(uid);
    }

    public fun createChatBetweenTwoUsers(firstUserUID: String, secondUserUID: String, listener: (chat: Chat) -> Unit) {
        ChatModel.instance.createChatBetweenTwoUsers(firstUserUID, secondUserUID, listener);
    }
    fun  getParksByUser(uId:String):List<TrainingSpotWithAll>?{
        this.user_parkList = TrainingSpotModel.instance.getUserParks(uId);
        return this.user_parkList.value;

    }

}