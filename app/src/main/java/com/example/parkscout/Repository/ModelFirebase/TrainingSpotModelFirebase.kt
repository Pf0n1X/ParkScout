package com.example.parkscout.Repository.ModelFirebase

import android.util.Log
import com.example.parkscout.Repository.*
import com.example.parkscout.data.Types.TrainingSpotFirebase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.lang.Exception
import java.util.*

class TrainingSpotModelFirebase {

    companion object {
        val COLLECTION_NAME: String = "training_spot";
    }
    public fun getTrainingSpotById(park_id:String, listener: (TrainingSpotWithAll?) -> Unit) {
        var db: FirebaseFirestore = FirebaseFirestore.getInstance();
        var trainingSpot =  TrainingSpot("","", com.example.parkscout.data.Types.Location(0.0,0.0),"","");
        var parkWithAll : TrainingSpotWithAll = TrainingSpotWithAll(trainingSpot,
            TrainingSpotsWithComments(trainingSpot,null),
        TrainingSpotWithRating
        (trainingSpot,null), TrainingSpotWithSportTypes
        (trainingSpot,null),TrainingSpotWithImages
        (trainingSpot,null));
//        var parkWithAll : TrainingSpotWithAll =TrainingSpotWithAll(trainingSpot,null,null,null,null) ;

        var query: Query = db.collection(TrainingSpotModelFirebase.COLLECTION_NAME).
        whereEqualTo("parkId",park_id);
        query.get()
            .addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful) {
                    val document = it.result
                    for (doc in it.result!!) {
                        var park: TrainingSpotFirebase = TrainingSpotFirebase(
                            "",
                            "",
                            com.example.parkscout.data.Types.Location(0.0, 0.0),
                            "",
                            "",
                            null,
                            null,
                            null,
                            null
                        );

                        park.fromMap(doc.data)

                        trainingSpot = TrainingSpot(
                            park.parkId,
                            park.parkName,
                            park.parkLocation,
                            park.chatId,
                            park.facilities
                        )

                        parkWithAll = TrainingSpotWithAll(
                            trainingSpot,
                            TrainingSpotsWithComments(trainingSpot, park.comment),
                            TrainingSpotWithRating(trainingSpot, park.ratings),
                            TrainingSpotWithSportTypes(trainingSpot, park.types),
                            TrainingSpotWithImages(trainingSpot, park.images)
                        )


                    }
                    listener(parkWithAll);

                }
            });

    }
    public fun getTrainingSpotByName(park_name:String, listener: (LinkedList<TrainingSpotWithAll>) -> Unit) {
        var db: FirebaseFirestore = FirebaseFirestore.getInstance();
        var trainingSpotWithAlllist: LinkedList<TrainingSpotWithAll> = LinkedList<TrainingSpotWithAll>();
        var parkWithAll : TrainingSpotWithAll;
        var trainingSpot =  TrainingSpot("","", com.example.parkscout.data.Types.Location(0.0,0.0),"","");
        var query: Query = db.collection(TrainingSpotModelFirebase.COLLECTION_NAME).
        whereEqualTo("parkName",park_name);
        query.get()
            .addOnCompleteListener(OnCompleteListener {

                if (it.isSuccessful) {
                    val document = it.result
                    for (doc in it.result!!) {
                        var park : TrainingSpotFirebase  = TrainingSpotFirebase("","",com.example.parkscout.data.Types.Location(0.0,0.0),"","",null,null,null,null);

                        park.fromMap(doc.data)

                        trainingSpot = TrainingSpot(park.parkId,park.parkName,park.parkLocation,park.chatId,park.facilities)

                        parkWithAll = TrainingSpotWithAll(trainingSpot,TrainingSpotsWithComments(trainingSpot,park.comment)
                            ,TrainingSpotWithRating(trainingSpot,park.ratings),TrainingSpotWithSportTypes(trainingSpot,park.types)
                            ,TrainingSpotWithImages(trainingSpot,park.images))

                        trainingSpotWithAlllist.add(parkWithAll);

                    }
                    listener(trainingSpotWithAlllist);

                }

            });

    }
        public fun getAllTrainingSpot( listener: (LinkedList<TrainingSpotWithAll>) -> Unit) {
        var db: FirebaseFirestore = FirebaseFirestore.getInstance();
        var trainingSpotWithAlllist: LinkedList<TrainingSpotWithAll> = LinkedList<TrainingSpotWithAll>();
        var parkWithAll : TrainingSpotWithAll;
        var trainingSpot =  TrainingSpot("","", com.example.parkscout.data.Types.Location(0.0,0.0),"","");
        var query: Query = db.collection(TrainingSpotModelFirebase.COLLECTION_NAME);

        query.get()
            .addOnCompleteListener(OnCompleteListener {

                if (it.isSuccessful) {
                    val document = it.result
                    for (doc in it.result!!) {
                        var park : TrainingSpotFirebase  = TrainingSpotFirebase("","",com.example.parkscout.data.Types.Location(0.0,0.0),"","",null,null,null,null);

                        park.fromMap(doc.data)

                        trainingSpot = TrainingSpot(park.parkId,park.parkName,park.parkLocation,park.chatId,park.facilities)

                        parkWithAll = TrainingSpotWithAll(trainingSpot,TrainingSpotsWithComments(trainingSpot,park.comment)
                            ,TrainingSpotWithRating(trainingSpot,park.ratings),TrainingSpotWithSportTypes(trainingSpot,park.types)
                            ,TrainingSpotWithImages(trainingSpot,park.images))

                        trainingSpotWithAlllist.add(parkWithAll);

                    }
                    listener(trainingSpotWithAlllist);

                }

            });
    }
    fun addPark(trainingSpotWithAll: TrainingSpotWithAll, listener: () -> Unit) {
        var db: FirebaseFirestore = FirebaseFirestore.getInstance();
        var doc: DocumentReference = db.collection(Companion.COLLECTION_NAME)
            .document();
        var trainingSpot:TrainingSpot = trainingSpotWithAll.getTariningSpot();
        trainingSpot.parkId = doc.id;
        var trainingSpotFireBase : TrainingSpotFirebase;
        var comments : List<Comment>? = trainingSpotWithAll.getComments();
        var rating : List<Rating>? = trainingSpotWithAll.getRating();
        var types : List<SportTypes>? = trainingSpotWithAll.getSportTypes();
        var imsges : List<Images>? = trainingSpotWithAll.getImages();

        if (types != null) {
            for(type in types){
                type.setParkId(trainingSpot.parkId);

            }
        }
        if (imsges != null){
            for (img in imsges){
                img.setParkId(trainingSpot.parkId);
            }
        }
        trainingSpotFireBase =
            TrainingSpotFirebase(
                trainingSpot.getParkId(), trainingSpot.parkName,
                trainingSpot.parkLocation, trainingSpot.chatId,
                trainingSpot.facilities, comments, rating, types, imsges
            )

        doc.set(trainingSpotFireBase.toMap())
            .addOnSuccessListener { listener(); }
            .addOnFailureListener { listener(); }
    }

    fun addComment(parkId: String, comment: Comment, listener: () -> Int) {
        var db: FirebaseFirestore = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_NAME)
            .document(parkId)
            .update("comment", FieldValue.arrayUnion(comment.toMap()))
            .addOnSuccessListener { listener(); }
            .addOnFailureListener { exception: Exception ->
                Log.d("TAG", "ERROR: " + exception.toString())
            };
    }
}