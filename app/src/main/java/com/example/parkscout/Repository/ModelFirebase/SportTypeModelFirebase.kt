package com.example.parkscout.Repository.ModelFirebase

import com.example.parkscout.Repository.SportTypes
import com.example.parkscout.Repository.TrainingSpot
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SportTypeModelFirebase {
    companion object {
        val COLLECTION_NAME: String = "sport_types";
    }

    fun addKinds(kind:SportTypes, listener: () -> Unit) {
        var db: FirebaseFirestore = FirebaseFirestore.getInstance();
        var doc: DocumentReference = db.collection(Companion.COLLECTION_NAME)
            .document();

        kind.type_id = doc.id;

        doc.set(kind.toMap())
            .addOnSuccessListener { listener(); }
            .addOnFailureListener { listener(); }
    }
}