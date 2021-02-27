package com.example.parkscout.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.parkscout.Repository.Model.UserModel
import com.example.parkscout.Repository.User

class SettingsFragmentViewModel: ViewModel() {

    // Data Members
    var user: LiveData<User>
        private set;

    // Constructors
    init {
        this.user = UserModel.instance.getUser();
    }

    // Methods
    fun setUser(user: User, listener: () -> Unit) {
        UserModel.instance.setUser(user, listener);
    }
}