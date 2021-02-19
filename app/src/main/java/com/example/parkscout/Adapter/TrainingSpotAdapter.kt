package com.example.parkscout.Adapter

import android.content.Context
import com.example.parkscout.Repository.TrainingSpot
import com.google.firebase.auth.FirebaseUser

class TrainingSpotAdapter(val context: Context,val trainingSpot: List<TrainingSpot>) {
    // Data Members
    private  var mContext: Context
    private  var mTrainingSpot: List<TrainingSpot>
    private lateinit var mFBUser: FirebaseUser


    init {
        mContext = context
        mTrainingSpot = trainingSpot
    }

}