package com.example.bikestat.SideOperations

object CaloriesOperations {

    const val AVERAGE_HUMAN_MASS = 78.15

    fun getKilocaloriesByTimeInSecAndPulse(
        timeInSec: Int,
        averagePulse: Int,
    ): Double{
        val timeInMinutes = timeInSec.toDouble() / 60
        val energyInKKAL = 0.014 * AVERAGE_HUMAN_MASS * timeInMinutes * (0.12 * averagePulse - 7)
        return energyInKKAL
    }


}