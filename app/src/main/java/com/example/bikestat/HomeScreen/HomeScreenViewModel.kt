package com.example.bikestat.HomeScreen

import android.util.Log
import com.example.bikestat.RealmDatabase.RealmOperations
import com.example.bikestat.RealmDatabase.RouteModel
import com.example.bikestat.SideOperations.CaloriesOperations
import com.example.bikestat.SideOperations.DifficultyOperations
import com.example.bikestat.SideOperations.TimeConv
import com.example.bikestat.WearableDeviceAPI.ApiOperations
import org.mongodb.kbson.ObjectId
import java.util.Calendar

object HomeScreenViewModel {

    var mainedRoute = RouteModel()

    fun insertOrUpdateRouteToRealm(
        isPlaning: Boolean,
        title: String,
        distanceInKM: Double,
        timeInSec: Int,
        averageSpeedInKMH: Double,
        maximumSpeedInKMH: Double,
        id: org.bson.types.ObjectId

    ){
        val calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

        val realDate = "${TimeConv.formatToTwoCharacters(day)}/${
            TimeConv.formatToTwoCharacters(month + 1)
        }/$year"

        ApiOperations.getAPIRequest()
        val pulseData = ApiOperations.Response.data.pulse
        Log.d("MyLog", "TEST --- > ${pulseData.avg}")
        val spentCalories = CaloriesOperations.getKilocaloriesByTimeInSecAndPulse(timeInSec, pulseData.avg)
        val realDifficulty = DifficultyOperations.getRealDifficultyByAvgSpeedAndAvgPulse(
            averageSpeedInKMH,
            pulseData.avg
        )
        val localTitle = if (title == "") "Новая поездка" else title
        val routeModel = RouteModel(
            title = localTitle,
            distanceInKM = distanceInKM,
            timeInSec = timeInSec,
            averageSpeedInKMH = averageSpeedInKMH,
            maximumSpeedInKMH = maximumSpeedInKMH,
            minPulse = pulseData.min,
            avgPulse = pulseData.avg,
            maxPulse = pulseData.max,
            spentCalories = spentCalories,
            realDifficulty = realDifficulty,
            date = realDate,
            isPlaning = isPlaning
        )
        if (isPlaning){
            Log.d("MyLog", "EDITING!!!")
            Log.d("MyLog", routeModel.isPlaning.toString())
            routeModel.id = id
            RealmOperations.editRouteForSaveHistoryByPlan(routeModel)
        }else{
            RealmOperations.insertRouteHistoryToRealm(routeModel)
        }
    }
}