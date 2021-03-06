package com.example.parkscout.Repository.ModelSQL

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkscout.Repository.*
import java.util.*
import kotlin.collections.HashMap


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



    fun getParkByName(parkName:String): MutableList<TrainingSpotWithAll>? {
        var parkWithAll : TrainingSpotWithAll;
        var parkWithAllList : MutableList<TrainingSpotWithAll> =  arrayListOf();
        var trainingSpotList : List<TrainingSpot>;
        var comments : List<Comment>
        var rating : List<Rating>
        var types : List<SportTypes>
        var images : List<Images>
        trainingSpotList = AppLocalDb.getInstance().trainingSpotDao().getParkByName(parkName);
        for (park in trainingSpotList) {
            comments = AppLocalDb.getInstance().commentDao()
                .getAllCommentsOfPark(park.getParkId());
            rating = AppLocalDb.getInstance().ratingDao().getParkRating(park.getParkId());
            types = AppLocalDb.getInstance().sportTypesDAO()
                .getSportTypesByPark(park.getParkId());
            images =
                AppLocalDb.getInstance().imagesDao().getAllImgsOfPark(park.getParkId());
            parkWithAll = TrainingSpotWithAll(
                park,
                TrainingSpotsWithComments(park, comments),
                TrainingSpotWithRating(park, rating),
                TrainingSpotWithSportTypes(park, types),
                TrainingSpotWithImages(park, images)
            )

            parkWithAllList.add(parkWithAll);
        }
        return parkWithAllList;
    }

    fun getParkById(parkId:String): TrainingSpotWithAll? {
        var parkWithAll : TrainingSpotWithAll;
        var trainingSpot : TrainingSpot;
        var comments : MutableList<Comment> = mutableListOf();
        var rating : MutableList<Rating> = mutableListOf();
        var types : MutableList<SportTypes> = mutableListOf();
        var images : MutableList<Images> = mutableListOf();
        trainingSpot = AppLocalDb.getInstance().trainingSpotDao().getParkById(parkId);
        if(trainingSpot != null) {
            comments = AppLocalDb.getInstance().commentDao()
                .getAllCommentsOfPark(trainingSpot.getParkId()) as MutableList<Comment>;
            rating =
                AppLocalDb.getInstance().ratingDao()
                    .getParkRating(trainingSpot.getParkId()) as MutableList<Rating>;
            types = AppLocalDb.getInstance().sportTypesDAO()
                .getSportTypesByPark(trainingSpot.getParkId()) as MutableList<SportTypes>;
            images =
                AppLocalDb.getInstance().imagesDao()
                    .getAllImgsOfPark(trainingSpot.getParkId()) as MutableList<Images>;

            parkWithAll = TrainingSpotWithAll(
                trainingSpot,
                TrainingSpotsWithComments(trainingSpot, comments),
                TrainingSpotWithRating(trainingSpot, rating),
                TrainingSpotWithSportTypes(trainingSpot, types),
                TrainingSpotWithImages(trainingSpot, images)
            )


            return parkWithAll;
        }
        return null;
    }

        public fun addPark(trainingSpotWithAll: TrainingSpotWithAll, listener: (() -> Unit)?) {
        class MyAsyncTask: AsyncTask<TrainingSpotWithAll, Void, TrainingSpotWithAll>() {
            override fun doInBackground(vararg params: TrainingSpotWithAll?): TrainingSpotWithAll {
                  AppLocalDb.getInstance().trainingSpotDao().insert(trainingSpotWithAll.getTariningSpot());

                if(trainingSpotWithAll.getSportTypes() != null) {
                    AppLocalDb.getInstance().trainingSpotDao()
                        .insertKinds(trainingSpotWithAll.getSportTypes());
                }

                var commentList = trainingSpotWithAll.getComments();
                if(commentList != null) {

                    AppLocalDb.getInstance().trainingSpotDao()
                        .insertComments(commentList)
                }

                if(trainingSpotWithAll.getRating() != null) {
                    AppLocalDb.getInstance().trainingSpotDao()
                        .insertRatings(trainingSpotWithAll.getRating())
                }

                if (trainingSpotWithAll.getImages() != null) {
                    AppLocalDb.getInstance().trainingSpotDao()
                        .insertImages(trainingSpotWithAll.getImages())
                }

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
