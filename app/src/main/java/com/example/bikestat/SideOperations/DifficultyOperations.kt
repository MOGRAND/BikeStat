package com.example.bikestat.SideOperations

object DifficultyOperations {

    fun getEstimatedDifficultyByAvgSpeed(avgSpeed: Double): String{
        val difficultyFactor = avgSpeed * 150
        return if (difficultyFactor >= 0.0 && difficultyFactor <= 1500.0){
            "Легко"
        }else if (difficultyFactor > 1500.0 && difficultyFactor <= 3000.0){
            "Средне"
        }else{
            "Сложно"
        }
    }

    fun getRealDifficultyByAvgSpeedAndAvgPulse(avgSpeed: Double, avgPulse: Int): String{
        val difficultyFactor = avgSpeed * avgPulse
        return if (difficultyFactor <= 1300.0){
            "Легко"
        }else if (difficultyFactor > 1300.0 && difficultyFactor <= 3400.0){
            "Средне"
        }else{
            "Сложно"
        }
    }

    fun getDifficultyFactorByAvgSpeedAndAvgPulse(avgSpeed: Double, avgPulse: Int): Double{
        val difficultyFactor = avgSpeed * avgPulse
        return difficultyFactor
    }

    fun getIntDifByDifficulty(difficulty: Double): Int{
        val avgDifFactorInt = if (difficulty <= 1300){ 1
        }else if (difficulty > 1300.0 && difficulty <= 3400.0){ 2
        }else{ 3
        }
        return avgDifFactorInt
    }
}