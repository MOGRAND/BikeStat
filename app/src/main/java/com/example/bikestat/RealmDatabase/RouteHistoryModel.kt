package com.example.bikestat.RealmDatabase

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class RouteModel (
    @PrimaryKey
    var id: ObjectId = ObjectId.get(),

    var title: String = "Новая поездка",

    var distanceInKM: Double = 0.0,
    var timeInSec: Int = 0,
    var averageSpeedInKMH: Double = 0.0,
    var maximumSpeedInKMH: Double = 0.0,

    var minPulse: Int = 0,
    var avgPulse: Int = 0,
    var maxPulse: Int = 0,

    var estimatedDifficulty: String = "Легко",
    var realDifficulty: String = "Легко",

    var spentCalories: Double = 0.0,
    var date: String = "",

    var isPlaning: Boolean = false,

    var planingDistanceInKM: Double = 0.0,
    var planingTimeInSec: Double = 0.0,
    var planingAverageSpeedInKMH: Double = 0.0,


): RealmObject()