package com.example.parkscout.Repository.ModelSQL

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkscout.Repository.*
import java.util.*


class TrainingSpotModelSQL {

    fun getAllParks(): MutableList<TrainingSpotWithAll>? {
        var parkWithAllList : MutableList<TrainingSpotWithAll> =  arrayListOf();
        var parkWithAll : TrainingSpotWithAll;
        var trainingSpotList : List<TrainingSpot>;
        var comments : List<Comment>
        var rating : List<Rating>
        var types : List<SportTypes>
        var images : List<Images>
        trainingSpotList = AppLocalDb.getInstance().trainingSpotDao().getAllParks()!!;
        for (park in trainingSpotList){
            comments = AppLocalDb.getInstance().commentDao().getAllCommentsOfPark(park.getParkId());
            rating = AppLocalDb.getInstance().ratingDao().getParkRating(park.getParkId());
            types = AppLocalDb.getInstance().sportTypesDAO().getSportTypesByPark(park.getParkId());
            images = AppLocalDb.getInstance().imagesDao().getAllImgsOfPark(park.getParkId());
            parkWithAll = TrainingSpotWithAll(park,
                TrainingSpotsWithComments(park,comments), TrainingSpotWithRating(park,rating),
                TrainingSpotWithSportTypes(park,types),TrainingSpotWithImages(park,images))


            parkWithAllList.add(parkWithAll)
        }
        return parkWithAllList;
    }

    public interface AddParkListener {
        fun onComplete();
    }


        public fun addPark(trainingSpotWithAll: TrainingSpotWithAll, listener: (() -> Unit)?) {
        class MyAsyncTask: AsyncTask<TrainingSpotWithAll, Void, TrainingSpotWithAll>() {
            override fun doInBackground(vararg params: TrainingSpotWithAll?): TrainingSpotWithAll {
                AppLocalDb.getInstance().trainingSpotDao().insert(trainingSpotWithAll.getTariningSpot());
                AppLocalDb.getInstance().trainingSpotDao().insertKinds(trainingSpotWithAll.getSportTypes())
                AppLocalDb.getInstance().trainingSpotDao().insertComments(trainingSpotWithAll.getComments())
                AppLocalDb.getInstance().trainingSpotDao().insertRatings(trainingSpotWithAll.getRating())
                AppLocalDb.getInstance().trainingSpotDao().insertImages(trainingSpotWithAll.getImages())

                return trainingSpotWithAll;
            }

            override fun onPostExecute(result: TrainingSpotWithAll?) {
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
