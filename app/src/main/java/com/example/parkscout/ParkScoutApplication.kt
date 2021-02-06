package com.example.parkscout

import android.app.Application
import android.content.Context

class ParkScoutApplication: Application() {

    // Data Members
    companion object {
        public lateinit var context: Context;
    }

    @Override
    public override fun onCreate() {
        super.onCreate();
        ParkScoutApplication.Companion.context = applicationContext;
    }
}