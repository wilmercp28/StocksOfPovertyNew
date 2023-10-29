package com.example.stocksofpoverty.data

fun formatNumberToK(number: Double): String {
    return when {
        number >= 1000 -> "${(number / 1000).toInt()}K"
        else -> number.toString()
    }
}


