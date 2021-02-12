package com.example.parkscout.data.model

import com.google.type.DateTime

class Rating (
    val user_Id : String,
    val trainingSpotId : String,
    val rate : Int,
    val rate_dateTime :  DateTime
)