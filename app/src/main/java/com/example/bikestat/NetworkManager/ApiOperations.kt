package com.example.bikestat.WearableDeviceAPI

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiOperations {
    val accessToken = "az4fvf7nzi1XPIsYiMEu"
    var Response = MeasurementResponse(
        data = MeasurementData(
            pulse = PulseData(
                min = 0,
                max = 0,
                avg = 0,
            )
        ),
        status = "notOk ))))"
    )
    fun getAPIRequest(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dt.miet.ru/ppo_it/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WatchApiSecvice::class.java)
        retrofit.getMeasurements(accessToken).enqueue(object : Callback<MeasurementResponse> {
            override fun onResponse(
                call: Call<MeasurementResponse>,
                response: Response<MeasurementResponse>
            ){
                if (response.isSuccessful){
                    response.body()?.let {
                        Log.d("MyLog", "${it.data.pulse.avg} ${it.status}")
                        Response = MeasurementResponse(
                            data = MeasurementData(
                                pulse = PulseData(
                                    min = it.data.pulse.min,
                                    max = it.data.pulse.max,
                                    avg = it.data.pulse.avg,
                                )
                            ),
                            status = it.status
                        )
                    }
                }
            }

            override fun onFailure(call: Call<MeasurementResponse>, t: Throwable) {
                Log.d("MyLog", "${t.message}")
            }
        })
    }
}