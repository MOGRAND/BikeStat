package com.example.bikestat.SideOperations

import com.google.android.gms.maps.model.LatLng
import kotlin.math.sqrt

object VectorOperations {

    const val ONE_DEGREE_IN_KM = 111

    fun getVectorLengthInKMByTwoCord(firstCord: LatLng, secCord: LatLng): Double{
        val latsqrt = (firstCord.latitude - secCord.latitude) * (firstCord.latitude - secCord.latitude)
        val lonsqrt = (firstCord.longitude - secCord.longitude) * (firstCord.longitude - secCord.longitude)
        val vectorLenghtInDegrees = sqrt((latsqrt + lonsqrt))
        return vectorLenghtInDegrees * ONE_DEGREE_IN_KM
    }
}