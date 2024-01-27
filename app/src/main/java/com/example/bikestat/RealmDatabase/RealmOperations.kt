package com.example.bikestat.RealmDatabase

import android.util.Log
import com.example.bikestat.HomeScreen.HomeScreenViewModel
import io.realm.Realm
import io.realm.RealmConfiguration

object RealmOperations {

    val config = RealmConfiguration.Builder()
        .name("BikeStatRealmDB")
        .schemaVersion(1)
        .deleteRealmIfMigrationNeeded()
        .allowQueriesOnUiThread(true)
        .allowWritesOnUiThread(true)
        .compactOnLaunch()
        .build()
    val realm = Realm.getInstance(config)


    //insert RouteHistoryModel to DB
    fun insertRouteHistoryToRealm(routeHistory: RouteModel){
        realm.executeTransaction{
            it.insert(routeHistory)
        }
    }

    //read RouteHistoryModel list from DB
    fun queryListOfRouteHistoryFromRealm():List<RouteModel>{
        val listOfRouteHistory: List<RouteModel> =realm.where(RouteModel::class.java).equalTo("isPlaning", false).findAll()
        return listOfRouteHistory
    }

    //insert RoutePlan to DB
    fun insertRoutePlanToRealm(routeHistory: RouteModel){
        realm.executeTransaction{
            it.insert(routeHistory)
        }
    }

    //read Route plan list from DB
    fun queryListOfRoutePlanFromRealm():List<RouteModel>{
        val listOfRouteHistory: List<RouteModel> =realm.where(RouteModel::class.java).equalTo("isPlaning", true).findAll()
        return listOfRouteHistory
    }

    //edit route
    fun editRouteForSaveHistoryByPlan(route: RouteModel){
        val editingRoute = realm.where(RouteModel::class.java).equalTo("id", route.id).findFirst()
        Log.d("MyLog", editingRoute?.title.toString())
        realm.executeTransaction{
            editingRoute?.title = route.title
            editingRoute?.distanceInKM = route.distanceInKM
            editingRoute?.timeInSec = route.timeInSec
            editingRoute?.averageSpeedInKMH = route.averageSpeedInKMH
            editingRoute?.maximumSpeedInKMH = route.maximumSpeedInKMH
            editingRoute?.minPulse = route.minPulse
            editingRoute?.avgPulse = route.avgPulse
            editingRoute?.maxPulse = route.maxPulse
            editingRoute?.spentCalories = route.spentCalories
            editingRoute?.realDifficulty = route.realDifficulty
            editingRoute?.date = route.date
            editingRoute?.isPlaning = false
        }
        HomeScreenViewModel.mainedRoute = RouteModel()
    }

    // ger a list of 5 days for Chart
    fun getAListOfFiveDaysDistance(listOfDates: List<String>): List<Int>{
        val listOfDistance = mutableListOf<Int>()
        listOfDates.forEach{
            val listOfDateRoute = realm.where(RouteModel::class.java).equalTo("date", it).equalTo("isPlaning", false).findAll()
            if (listOfDateRoute.size == 0){
                listOfDistance += 0
            }else{
                var distanceSumm = 0
                listOfDateRoute.forEach{
                    distanceSumm += it.distanceInKM.toInt()
                }
                listOfDistance += distanceSumm
            }
        }
        return listOfDistance
    }
}