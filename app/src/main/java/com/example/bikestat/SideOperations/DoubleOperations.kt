package com.example.bikestat.SideOperations

import android.util.Log

object DoubleOperations {
    fun roundDoubleToTwoDecimalPlaces(double: Double): String{
        val stringDouble = double.toString()
        Log.d("MyLog", stringDouble)
        if (stringDouble.substringAfter(".").length == 1){
            return stringDouble
        }else{
            return stringDouble.substringBefore(".") + "."+(stringDouble.substringAfter(".").substring(0,1))
        }
    }
}