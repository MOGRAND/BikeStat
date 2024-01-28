package com.example.bikestat.SideOperations

object StringOperations {
    fun cutTheString(string: String, sign: Int): String{
        if (string.length > sign){
            val cuttedString = "${string.substring(0,sign)}..."
            return cuttedString
        }
        return string
    }
}