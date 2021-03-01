package com.example.parkscout.Repository.ModelSQL

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.example.parkscout.Repository.SportTypes
import com.example.parkscout.Repository.TrainingSpot

class SportTypesModelSQL {
//    fun getAllKinds(): LiveData<List<SportTypes>> {
////        return AppLocalDb.getInstance().sportTypesDAO().getSportTypesByPark();
//    }

    public interface AddSportKind {
        fun onComplete();
    }

    public fun addKind(kind:SportTypes, listener: (() -> Unit)?) {
        class MyAsyncTask: AsyncTask<SportTypes, Void, SportTypes>() {
            override fun doInBackground(vararg params: SportTypes?): SportTypes? {
                 //   AppLocalDb.getInstance().sportTypesDAO().insertSportType(kind);
                return kind;
            }

            override fun onPostExecute(result: SportTypes?) {
                super.onPostExecute(result);

                if (listener != null) {
                    listener();
                }
            }
        }

        var task: MyAsyncTask = MyAsyncTask();
        task.execute();
    }
}