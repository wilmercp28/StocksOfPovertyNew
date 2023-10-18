package com.example.stocksofpoverty.module

import androidx.compose.runtime.MutableState
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.Stock
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.random.Random


fun update(
    stocks: MutableState<List<Stock>>,
    date: MutableState<Date>,
    player: MutableState<Player>
) {
    updateStockPrice(stocks)

}

fun updateStockPrice(stocks: MutableState<List<Stock>>) {
    val scale = 0.1
    for (stock in stocks.value) {
        val supplyDemandDif = stock.demand - stock.supply
        val randomPriceIncrease = Random.nextDouble(abs(supplyDemandDif).toDouble()) * scale
        if (supplyDemandDif > 0) {
            stock.price.value += randomPriceIncrease
        } else {
            stock.price.value -= randomPriceIncrease
        }
    }
}