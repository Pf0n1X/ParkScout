package com.example.parkscout.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.parkscout.Repository.Chat
import com.example.parkscout.Repository.Model.ChatModel
import com.example.parkscout.Repository.Model.TrainingSpotModel
import com.example.parkscout.Repository.Model.UserModel
import com.example.parkscout.Repository.TrainingSpotWithAll
import com.example.parkscout.Repository.User
import com.example.parkscout.data.Types.TrainingSpotFirebase

class TrainingSpotViewModel: ViewModel() {
    // Data Members
    lateinit var parkList: LiveData<List<TrainingSpotWithAll>>
    lateinit var parkById: LiveData<TrainingSpotWithAll>;
    lateinit var parkByName: LiveData<List<TrainingSpotWithAll>>;
    lateinit var user: LiveData<User>
    lateinit var parkChat: LiveData<Chat>

        private set;

    // Constructors
    init {
        this.parkList = TrainingSpotModel.instance.getAllParks();
    }

    // Methods
    fun addPark(park: TrainingSpotWithAll,user: User, listener: () -> Unit) {
        TrainingSpotModel.instance.addTrainingSpot(park,user, listener);
    }
    fun getParks(): List<TrainingSpotWithAll>? {
        return parkList.value;
    }

    fun getParkById(parkId:String): LiveData<TrainingSpotWithAll> {
        this.parkById =  TrainingSpotModel.instance.getParkById(parkId);
        return this.parkById;
    }
    fun  getParkByName(parkName:String):List<TrainingSpotWithAll>?{
        this.parkByName = TrainingSpotModel.instance.getParkByName(parkName);
        return this.parkByName.value;
    }

    public fun getUserByID(uid: String) {
        this.user = UserModel.instance.getUserByID(uid);
    }

    fun addChat(chat: Chat,user: User, listener: () -> Unit){
        ChatModel.instance.addChat(chat,user,listener)
    }
}