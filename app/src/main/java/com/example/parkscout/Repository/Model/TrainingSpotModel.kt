package com.example.parkscout.Repository.Model

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.parkscout.ParkScoutApplication
import com.example.parkscout.Repository.ChatMessage
import com.example.parkscout.Repository.ChatWithAll
import com.example.parkscout.Repository.ModelFirebase.ChatModelFireBase
import com.example.parkscout.Repository.ModelFirebase.TrainingSpotModelFirebase
import com.example.parkscout.Repository.ModelSQL.TrainingSpotModelSQL
import com.example.parkscout.Repository.TrainingSpotWithAll
import com.example.parkscout.Repository.TrainingSpotWithSportTypes
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class TrainingSpotModel {
    companion object {
        public var instance: TrainingSpotModel = TrainingSpotModel()
            public get
            private set;
    }
    // Data Members
    var modelFirebase: TrainingSpotModelFirebase;
    private var modelSQL: TrainingSpotModelSQL;
    private lateinit var parksList: TrainingSpotModel.ParkLiveData;
    private lateinit var executor:Executor;
    // Constructors
    init {
        this.modelFirebase = TrainingSpotModelFirebase();
        this.modelSQL = TrainingSpotModelSQL();
        this.executor = Executors.newSingleThreadExecutor();
        }

    public fun getAllParks(): MutableLiveData<List<TrainingSpotWithAll>> {
        this.parksList = ParkLiveData();

//        this.parksList.value = modelSQL.getAllParks()

        var park : MutableList<TrainingSpotWithAll> = arrayListOf();
        modelFirebase.getAllTrainingSpot({ parks: MutableList<TrainingSpotWithAll> -> park = parks;

        });
        this.parksList.value = park;
        return this.parksList;
    }

    public fun addTrainingSpot(park: TrainingSpotWithAll, listener: () -> Unit) {
        var refreshListener: () -> Unit = {
            listener();
        };

        var addListener: () -> Unit = {
            modelSQL.addPark(park,refreshListener)

        };

        modelFirebase.addPark(park,addListener);

    }
    inner class ParkLiveData: MutableLiveData<List<TrainingSpotWithAll>>() {

        // Constructors
        init {
            value = LinkedList<TrainingSpotWithAll>();
        }

        // Methods
        override fun onActive() {
            super.onActive();

            val sp: SharedPreferences = ParkScoutApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);

            executor.execute {
                val parksList = modelSQL.getAllParks()

                postValue(parksList)
            }

            modelFirebase.getAllTrainingSpot({ parks: List<TrainingSpotWithAll> ->
                value = parks;

            });
        }
        override fun onInactive() {
            super.onInactive();

        }
    }

}