package com.example.bikestat.WearableDeviceAPI

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface WatchApiSecvice{
    @GET("watch")
    fun getMeasurements(@Header("x-access-tokens") accessToken: String): Call<MeasurementResponse>
}
data class MeasurementResponse(
    val data: MeasurementData,
    val status: String
)

data class MeasurementData(
    val pulse: PulseData
)

data class PulseData(
    val min: Int,
    val max: Int,
    val avg: Int
)
