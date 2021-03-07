package com.example.parkscout.Repository.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.parkscout.Repository.ModelFirebase.UserModelFirebase
import com.example.parkscout.Repository.ModelSQL.UserModelSQL
import com.example.parkscout.Repository.User
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class UserModel {

    companion object {
        var instance: UserModel = UserModel()
            public get
            private set;
    }

    // Data Members
    private var modelFirebase: UserModelFirebase;
    private var modelSQL: UserModelSQL;
    private var executor: Executor;
    private var user: UserLiveData;

    // Initialization
    init {
        this.executor = Executors.newSingleThreadExecutor();
        this.modelSQL = UserModelSQL();
        this.modelFirebase = UserModelFirebase();
        this.user = UserLiveData();
    }

    // Methods
    public fun getUser(): UserLiveData {
        return this.user;
    }

    fun setUser(user: User, listener: () -> Unit) {
        this.user.value = user;
        modelFirebase.setUser(user, {
            modelSQL.setUser(user, listener)
        });
    }

    fun addUser(user: User, listener: () -> Unit) {
        this.user.value = user;
        modelFirebase.addUser(user, {
            modelSQL.setUser(user, listener)
        });
    }

    fun getUserByID(uid: String): LiveData<User> {
        executor.execute {
            val userByid = modelSQL.getUserByID(uid);
            if(userByid != null) {
                user.postValue(userByid)
            }
        }
        modelFirebase.getUserByID(uid, { fbUser: User ->
            modelSQL.setUser(fbUser, {
                user.postValue(fbUser);
            });
        });

        return user;
    }

    inner class UserLiveData: MutableLiveData<User>() {

        // Methods
        override fun onActive() {
            super.onActive();

            executor.execute{
                var sqlUser = modelSQL.getUser();

                if (sqlUser != null)
                    postValue(sqlUser);
            };

            var listener = { user: User ->
                modelSQL.setUser(user, null);
                postValue(user);
            };

            modelFirebase.getUser(listener);
        }

        override fun onInactive() {
            super.onInactive();
        }
    }
}