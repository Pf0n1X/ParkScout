package com.example.parkscout.data.model

import com.google.type.DateTime

class Comment (
    val trainingSpotId : String,
    val userId : String,
    val c_text: String,
    val c_dateTime : DateTime
)