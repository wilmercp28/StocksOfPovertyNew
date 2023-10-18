package com.example.stocksofpoverty.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlin.random.Random

data class Stock(
    val name: String,
    val symbol: String,
    val price: MutableState<Double>,
    val demand: Int,
    val supply: Int,
    val shares: MutableState<Int>,
    val averageBuyPrice: MutableState<Int>
)

fun getInitialStockList(
): List<Stock> {
    val listOfNames = getStockNameList()
    val listOfStocks = mutableListOf<Stock>()
    for (stock in listOfNames) {

        val name = stock.substringBefore(":")
        val abbreviation = stock.substringAfter(":")
        val randomPrice = Random.nextDouble(100.0, 1000.0)
        val randomSupply = Random.nextInt(100) + 100
        val randomDemand = Random.nextInt(100) + 100

        val stock =
            Stock(
                name,
                abbreviation,
                mutableStateOf(randomPrice),
                randomDemand,
                randomSupply,
                mutableStateOf(5),
                mutableStateOf(0)
            )
        listOfStocks += stock
    }
    return listOfStocks
}
data class SaveGame(
    val stock: List<Stock>
)
data class Date(
    val day: MutableState<Int>,
    val month: MutableState<Int>,
    val year: MutableState<Int>
)

fun getInitialDate(): Date {
    return Date(
        mutableStateOf(1),
        mutableStateOf(1),
        mutableStateOf(1)
    )
}

data class Player(
    val name: String,
    val balance: MutableState<Double>,
)

fun getInitialPlayer(): Player {
    return Player(
        "Player",
        mutableStateOf(10000.00)
    )
}