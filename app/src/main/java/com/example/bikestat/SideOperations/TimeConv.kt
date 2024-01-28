package com.example.bikestat.SideOperations

import kotlin.math.abs

object TimeConv {
    fun formatTime(timeInSeconds: Int): String{
        val hours = formatToTwoCharacters(timeInSeconds / 3600)
        val minutes = formatToTwoCharacters((timeInSeconds % 3600) / 60)
        val seconds = formatToTwoCharacters(timeInSeconds % 60)
        return "$hours:$minutes:$seconds"
    }

    fun formatToTwoCharacters(time: Int): String{
        val stringTime = time.toString()
        if (stringTime == "0"){
            return "00"
        }else if(stringTime.length == 1){
            return "0${stringTime}"
        }else{
            return stringTime
        }
    }

    fun getFiveDaysDateList(currentDate: String): List<String>{
        val dateList = mutableListOf<String>()
        for (index in 0..4){
            dateList += getDateByCurrentDate(currentDate, index)
        }
        return dateList
    }
    //input format day/month/year
    fun getDateByCurrentDate(currentDate: String, index: Int): String {
        if (index == 0) {
            return currentDate
        }
        val day = currentDate.substringBefore("/")
        val month = currentDate.substringAfter("/").substringBefore("/")
        val year = currentDate.substringAfter("/").substringAfter("/")

        var currentDay = ""
        var currentMonth = ""
        var currentYear = ""


        val dayMinus = (day.toInt() - index)

        if (dayMinus <= 0) {
            if (month == "01") {
                currentDay = (31 - abs(dayMinus)).toString()
                currentMonth = "12"
                currentYear = (year.toInt()-1).toString()
            } else if (month == "03" || month == "05" || month == "07" || month == "08" || month == "10" || month == "12") {
                currentDay = (31 - dayMinus).toString()
                currentMonth = (month.toInt()-1).toString()
            } else if (month == "04" || month == "06" || month == "09" || month == "11") {
                currentDay = (30 - dayMinus).toString()
                currentMonth = (month.toInt()-1).toString()
            } else {
                if (year.toInt() % 4 == 0 && year.toInt() % 100 != 0) {
                    currentDay = (29 - dayMinus).toString()
                    currentMonth = (month.toInt()-1).toString()
                } else {
                    currentDay = (28 - dayMinus).toString()
                    currentMonth = (month.toInt()-1).toString()
                }
            }
        }else{
            currentDay = (day.toInt() - index).toString()
            currentMonth = month
            currentYear = year
        }
        return "$currentDay/$currentMonth/$currentYear"
    }
}