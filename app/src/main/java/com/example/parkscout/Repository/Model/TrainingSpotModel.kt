package com.example.parkscout.Repository.Model

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.parkscout.ParkScoutApplication
import com.example.parkscout.Repository.*
import com.example.parkscout.Repository.ModelFirebase.TrainingSpotModelFirebase
import com.example.parkscout.Repository.ModelSQL.TrainingSpotModelSQL
import com.example.parkscout.data.Types.Location
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
    private lateinit var parkToShow: TrainingSpotModel.ParkSelectedLiveData;
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

    public fun getParkById(parkId:String): LiveData<TrainingSpotWithAll> {
            this.parkToShow = ParkSelectedLiveData();
        if (parkId != ""){
            this.parkToShow.GetParkById(parkId);
            }
            return this.parkToShow


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
                for (park in parks) {
                    modelSQL.addPark(park){};
                }
            });
        }
        override fun onInactive() {
            super.onInactive();

        }
    }
    inner class ParkSelectedLiveData: LiveData<TrainingSpotWithAll>() {

        // Constructors
        init {
            var park : TrainingSpot = TrainingSpot("","", Location(0.0,0.0),"","");
            value = TrainingSpotWithAll(park, TrainingSpotsWithComments(park,null),
                TrainingSpotWithRating(park,null), TrainingSpotWithSportTypes(park,null),
                TrainingSpotWithImages(park,null)
            );
        }

        fun GetParkById(parkId: String){

            val sp: SharedPreferences = ParkScoutApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);

            executor.execute {
                val parkById = modelSQL.getParkById(parkId)

                postValue(parkById)
            }

            modelFirebase.getTrainingSpotById(parkId,{ park: TrainingSpotWithAll? ->
            value = park;
            });
        }
    }


}