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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
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
    private lateinit var parkByNameSearch: TrainingSpotModel.ParkLiveDataByName;
    private lateinit var parkByUser: TrainingSpotModel.ParkLiveDataByUser;
    private lateinit var executor:Executor;
    // Constructors
    init {
        this.modelFirebase = TrainingSpotModelFirebase();
        this.modelSQL = TrainingSpotModelSQL();
        this.executor = Executors.newSingleThreadExecutor();
        this.parksList = ParkLiveData();
        }

    public fun getAllParks(): ParkLiveData {

        return this.parksList;
    }

    fun getUserParks(uId:String):MutableLiveData<List<TrainingSpotWithAll>>{
        this.parkByUser = ParkLiveDataByUser();
        this.parkByUser.GetParksByUser(uId);
        return  this.parkByUser;
    }

    public fun getParkById(parkId:String): LiveData<TrainingSpotWithAll> {
            this.parkToShow = ParkSelectedLiveData();
        if (parkId != ""){
            this.parkToShow.GetParkById(parkId);
            }
            return this.parkToShow


    }
    public fun getParkByName(parkName:String):MutableLiveData<List<TrainingSpotWithAll>>{
        this.parkByNameSearch = ParkLiveDataByName();
        if(!parkName.isEmpty()){
            this.parkByNameSearch.GetParksByName(parkName);
        }
        return this.parkByNameSearch;
    }

    public fun addTrainingSpot(park: TrainingSpotWithAll,user: User, listener: () -> Unit) {

        val list: MutableList<TrainingSpotWithAll> = this.parksList.value!!.toMutableList();
        list.add(park);
        this.parksList.value = list.toList();

        modelFirebase.addPark(park, {
            modelSQL.addPark(park,listener)
            val chat:Chat = Chat("0",park.trainingSpot.parkId)
            ChatModel.instance.addChat(chat,user,listener)
        });

    }
    fun updateTrainingSpot(park:String,chatId:String,listener: () -> Unit) {
        modelFirebase.updateParkChat(park, chatId,{
            listener();
        });

    }
    fun addComment(parkId: String, comment: Comment, listener: () -> Int) {
        modelFirebase.addComment(parkId, comment, {
            // TODO: Update the local DB.

            listener();
        });
    }

    fun addRating(parkId: String, rating: Rating, listener: () -> Int) {
        modelFirebase.addRating(parkId, rating, {
            // TODO: Update the local DB.

            listener();
        });
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
                if(parkById != null) {
                    postValue(parkById)
                }
            }

            var addParkListener = { park: TrainingSpotWithAll? ->

                if (park != null)
                    modelSQL.addPark(park, {
                        value = park;
                    });

                value = park;
            };

            modelFirebase.getTrainingSpotById(parkId, addParkListener);
        }
    }

    inner class ParkLiveDataByUser: MutableLiveData<List<TrainingSpotWithAll>>() {

        // Constructors
        init {
            value = LinkedList<TrainingSpotWithAll>();
        }

        fun GetParksByUser(userId: String){

            val sp: SharedPreferences = ParkScoutApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);

            executor.execute {
                val parksByUser = modelSQL.getParkByUser(userId)

                postValue(parksByUser)
            }

            modelFirebase.getTrainingSpotByUser(userId,{ parks: List<TrainingSpotWithAll> ->
                value = parks;

            });

        }
    }
    inner class ParkLiveDataByName: MutableLiveData<List<TrainingSpotWithAll>>() {

        // Constructors
        init {
            value = LinkedList<TrainingSpotWithAll>();
        }

        fun GetParksByName(parkName: String){

            val sp: SharedPreferences = ParkScoutApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);

            executor.execute {
                val parkByName = modelSQL.getParkByName(parkName)

                postValue(parkByName)
            }

            modelFirebase.getTrainingSpotByName(parkName,{ parks: List<TrainingSpotWithAll> ->
                value = parks;

            });

        }
    }
}