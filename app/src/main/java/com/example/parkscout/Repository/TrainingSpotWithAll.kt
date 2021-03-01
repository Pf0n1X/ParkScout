package com.example.parkscout.Repository

data class TrainingSpotWithAll(
    var trainingSpot: TrainingSpot,
    var trainingSpotsWithComments: TrainingSpotsWithComments,
    var trainingSpotWithRating: TrainingSpotWithRating,
    var trainingSpotWithSportTypes: TrainingSpotWithSportTypes,
    var trainingSpotWithImages: TrainingSpotWithImages
){
    fun getTariningSpot():TrainingSpot{
        return trainingSpot
    }
    fun getComments(): List<Comment>? {
    return trainingSpotsWithComments.getComments()
    }
    fun getRating(): List<Rating>? {
        return trainingSpotWithRating.getRating()
    }
    fun getSportTypes(): List<SportTypes>? {
        return trainingSpotWithSportTypes.getTypes()
    }
    fun getImages(): List<Images>? {
        return trainingSpotWithImages.getImages()
    }
}
