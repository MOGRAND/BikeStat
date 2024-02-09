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
            if (month.toInt()-1 == 0) {
                currentDay = (31 - abs(dayMinus)).toString()
                currentMonth = "12"
                currentYear = (year.toInt()-1).toString()
            } else if (month.toInt()-1 == 1 || month.toInt()-1 == 4 || month.toInt()-1 == 6 || month.toInt()-1 == 7 || month.toInt()-1 == 9 || month.toInt()-1 == 11) {
                currentDay = (31 - abs(dayMinus)).toString()
                currentMonth = (month.toInt()-1).toString()
                currentYear = year
            } else if (month.toInt()-1 == 3 || month.toInt()-1 == 5 || month.toInt()-1 == 8 || month.toInt()-1 == 10) {
                currentDay = (30 - abs(dayMinus)).toString()
                currentMonth = (month.toInt()-1).toString()
                currentYear = year
            } else {
                if (year.toInt() % 4 == 0 && year.toInt() % 100 != 0) {
                    currentDay = (29 - abs(dayMinus)).toString()
                    currentMonth = (month.toInt()-1).toString()
                    currentYear = year
                } else {
                    currentDay = (28 - abs(dayMinus)).toString()
                    currentMonth = (month.toInt()-1).toString()
                    currentYear = year
                }
            }
        }else{
            currentDay = (day.toInt() - index).toString()
            currentMonth = month
            currentYear = year
        }
        return "${formatToTwoCharacters(currentDay.toInt())}/${formatToTwoCharacters(currentMonth.toInt())}/$currentYear"
    }
}