package com.example.stocksofpoverty.module

import androidx.compose.runtime.MutableState
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.Stock

fun buyStock(stock: Stock, shareCount: MutableState<Int>, player: MutableState<Player>) {
    val totalPrice = stock.price.value * shareCount.value
    if (totalPrice < player.value.balance.value && shareCount.value != 0) {
        player.value.balance.value -= totalPrice
        stock.averageBuyPrice.value = getAverageBuyPrice(stock, shareCount, totalPrice)
        stock.shares.value += shareCount.value
        shareCount.value = 0
    } else {
        val howManyCanAfford = player.value.balance.value / stock.price.value
        shareCount.value = howManyCanAfford.toInt()
    }
}

fun getAverageBuyPrice(stock: Stock, shareCount: MutableState<Int>, totalPrice: Double): Double {
    val totalInvested = stock.averageBuyPrice.value * stock.shares.value
    val newTotal = totalInvested + totalPrice
    val totalShares = stock.shares.value + shareCount.value
    return newTotal / totalShares
}

fun sellStock(stock: Stock, shareCount: MutableState<Int>, player: MutableState<Player>){



}

fun getProfitLosses(shareCount: MutableState<Int>, stock: Stock): Double {
    val totalIfSellOut = stock.shares.value * stock.price.value
    return totalIfSellOut / shareCount.value
}