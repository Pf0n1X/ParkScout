package com.example.parkscout.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.parkscout.Repository.Model.UserModel
import com.example.parkscout.Repository.User

class ProfileViewModel: ViewModel() {

    // Data Members
    public lateinit var user: LiveData<User>
        private set;

    // Constructors
    init {
//        this.user =
    }

    // Methods
    public fun getUserByID(uid: String) {
        this.user = UserModel.instance.getUserByID(uid);
    }
}