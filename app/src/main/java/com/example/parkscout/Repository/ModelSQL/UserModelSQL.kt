package com.example.parkscout.Repository.ModelSQL

import androidx.lifecycle.LiveData
import com.example.parkscout.Repository.User
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class UserModelSQL {

    // Data Members
    private var executor: Executor;

    // Initialization
    init {
        this.executor = Executors.newSingleThreadExecutor();
    }

    // Methods
    fun getUser(): User {
        return AppLocalDb.getInstance().userDao().getUser();
    }

    fun setUser(user: User, listener: (() -> Unit)?) {
        executor.execute{
            AppLocalDb.getInstance().userDao().insert(user);

            if (listener != null) {
                listener();
            }
        }
    }
}