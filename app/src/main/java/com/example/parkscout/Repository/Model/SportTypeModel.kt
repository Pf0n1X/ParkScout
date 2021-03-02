package com.example.parkscout.Repository.Model

import com.example.parkscout.Repository.ModelFirebase.SportTypeModelFirebase
import com.example.parkscout.Repository.ModelFirebase.TrainingSpotModelFirebase
import com.example.parkscout.Repository.ModelSQL.SportTypesModelSQL
import com.example.parkscout.Repository.ModelSQL.TrainingSpotModelSQL
import com.example.parkscout.Repository.SportTypes

class SportTypeModel {
    companion object {
        public var instance: SportTypeModel = SportTypeModel()
            public get
            private set;
    }
    // Data Members
    var modelFirebase: SportTypeModelFirebase;
    private var modelSQL: SportTypesModelSQL;
    // Constructors
    init {
        this.modelFirebase = SportTypeModelFirebase();
        this.modelSQL = SportTypesModelSQL();

    }

    public fun addKind(kinds: List<SportTypes>, listener: () -> Unit) {
        var refreshListener: () -> Unit = {
            listener();
        };

        var addListener: () -> Unit = {
            for (kind in kinds) {
                modelSQL.addKind(kind, refreshListener)
            }

            //    refreshAllMessages(refreshListener);
        };
        for (kind in kinds) {
            modelFirebase.addKinds(kind, addListener);
        }
    }
}