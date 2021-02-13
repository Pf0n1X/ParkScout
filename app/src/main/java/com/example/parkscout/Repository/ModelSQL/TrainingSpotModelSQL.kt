package com.example.parkscout.Repository.ModelSQL

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.example.parkscout.data.model.TrainingSpot

class TrainingSpotModelSQL {
//    fun getAllParks(): LiveData<List<TrainingSpot>> {
//        return AppLocalDb.getInstance().trainingSpotDao().getAllParks();
//    }

    public interface AddParkListener {
        fun onComplete();
    }

    public fun addPark(park: TrainingSpot, listener: AddParkListener) {
//        class MyAsyncTask: AsyncTask<TrainingSpot, Void, TrainingSpot>() {
//            override fun doInBackground(vararg params: TrainingSpot?): TrainingSpot {
//                AppLocalDb.getInstance().trainingSpotDao().insertPark(park);
//
//                return park;
//            }
//
//            override fun onPostExecute(result: TrainingSpot?) {
//                super.onPostExecute(result);
//
//                if (listener != null) {
//                    listener.onComplete();
//                }
//            }
//        }
//
//        var task: MyAsyncTask = MyAsyncTask();
//        task.execute();
    }
}
