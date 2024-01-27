package com.example.bikestattest.StatisticsScreen

import android.util.Log


object StatisticsScreenViewModel {
    fun insertRouteToRealm(
        title: String,
        planingDistance: String,
        planingTimeInMinutes: String,
        planingDate: String,
    ) {
        val localTitle = if (title == "") "Новый план" else title
        val localTimeInSec =
            if (planingTimeInMinutes == "") 3600.0 else planingTimeInMinutes.toDouble() * 60
        val localDistance = if (planingDistance == "") 1.0 else planingDistance.toDouble()
        val averageSpeedInKMH = (localDistance * 3600.0) / localTimeInSec
        val estimatedDifficulty =
            DifficultyOperations.getEstimatedDifficultyByAvgSpeed(averageSpeedInKMH)
        Log.d("MyLog", planingDate)
        val routePlan = RouteModel(
            title = localTitle,
            planingDistanceInKM = localDistance,
            planingTimeInSec = localTimeInSec,
            date = planingDate,
            isPlaning = true,
            estimatedDifficulty = estimatedDifficulty,
            planingAverageSpeedInKMH = averageSpeedInKMH,
        )
        RealmOperations.insertRoutePlanToRealm(routePlan)
    }

    fun getARecommendedDifficulty(): String {
        val routeHistoryList = RealmOperations.queryListOfRouteHistoryFromRealm()
        val listOfDifficultyFactor = mutableListOf<Double>()
        routeHistoryList.forEach { routeHistory ->
            val difficultyFactor = DifficultyOperations.getDifficultyFactorByAvgSpeedAndAvgPulse(
                routeHistory.averageSpeedInKMH,
                routeHistory.avgPulse
            )
            listOfDifficultyFactor += difficultyFactor
        }
        val averageDifficultyFactor = listOfDifficultyFactor.sum() / listOfDifficultyFactor.size
        var intAvgDifFactor = DifficultyOperations.getIntDifByDifficulty(averageDifficultyFactor)
        val intLastDifFactor =
            DifficultyOperations.getIntDifByDifficulty((routeHistoryList.reversed())[0].averageSpeedInKMH * (routeHistoryList.reversed())[0].avgPulse)
        if (intLastDifFactor > intAvgDifFactor){
            intAvgDifFactor += 1
        }else if (intLastDifFactor < intAvgDifFactor){
            intAvgDifFactor -= 1
        }

        val recomendedDifficulty = if (intAvgDifFactor == 2){
            "Средне"
        }else if (intLastDifFactor > 2){
            "Сложно"
        }else{
            "Легко"
        }

        return recomendedDifficulty
    }
}