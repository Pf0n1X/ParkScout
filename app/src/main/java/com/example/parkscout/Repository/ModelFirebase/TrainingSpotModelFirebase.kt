package com.example.parkscout.Repository.ModelFirebase

import androidx.lifecycle.MutableLiveData
import com.example.parkscout.Repository.*
import com.example.parkscout.data.Types.TrainingSpotFirebase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class TrainingSpotModelFirebase {

    companion object {
        val COLLECTION_NAME: String = "training_spot";
    }
    public fun getAllTrainingSpot( listener: (LinkedList<TrainingSpotWithAll>) -> Unit) {
        var db: FirebaseFirestore = FirebaseFirestore.getInstance();
        var trainingSpotWithAlllist: LinkedList<TrainingSpotWithAll> = LinkedList<TrainingSpotWithAll>();
        var parkWithAll : TrainingSpotWithAll;
        var trainingSpot =  TrainingSpot("","", com.example.parkscout.data.Types.Location(0.0,0.0),"","");
        var query: Query = db.collection(TrainingSpotModelFirebase.COLLECTION_NAME);
//            .whereArrayContains("users", userId);

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

//
//                        for (chatm in doc.data["chat_messages"] as ArrayList<*>) {
//                            message.fromMap(chatm as Map<String?, Any?>);
//                            chatmessages.add(message);
//                        }
//
//                        var chatwithchatmessages: ChatWithChatMessages = ChatWithChatMessages(Chat = doc.data["id"] as String,
//                            chatMessages = chatmessages
//                        );
//                        chatswithchatmessages.add(chatwithchatmessages);
//
//                        var userRef: DocumentReference ;
//                        var user: User = User("","","");
//                        for (chatUsers in doc.data["users"] as ArrayList<*>) {
//                            userRef = chatUsers as DocumentReference;
//                            userRef.get().addOnSuccessListener {
//                                it.data?.let { it1 -> user.fromMap(it1) };
//                                listener(ChatWithAlllist)
//                            }
//                        }
//                        users.add(user);
//
//                        var chatWithUsers: ChatWithUsers = ChatWithUsers(Chat = doc.data["id"] as String,
//                            Users = users);
//
//                        var chatWithAll: ChatWithAll = ChatWithAll(chat,chatwithchatmessages,chatWithUsers);
//                        ChatWithAlllist.add(chatWithAll);

                    }
                    listener(trainingSpotWithAlllist);

                }

//                listener(ChatWithAlllist);
            });

//        query.addSnapshotListener({ value: QuerySnapshot?, error: FirebaseFirestoreException? ->
//            for (dc in value!!.documentChanges) {
//                var msg: ChatMessage = ChatMessage("", "", "", "", "", 0);
//                msg.fromMap(dc.document.data);
//                (messages as LinkedList<ChatMessage>).add(msg);
//            }
//
//            listener(messages);
//        });
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
}