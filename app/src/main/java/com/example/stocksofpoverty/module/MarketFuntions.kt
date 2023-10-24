package com.example.stocksofpoverty.module

import androidx.compose.runtime.MutableState
import com.example.stocksofpoverty.data.Bank
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.Stock
import com.example.stocksofpoverty.data.getDateToString
import java.text.DecimalFormat

fun buyStock(
    stock: Stock, shareCount: MutableState<Int>, player: MutableState<Player>,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>,
    format: DecimalFormat
) {
    val totalPrice = stock.price.value * shareCount.value
    if (totalPrice < player.value.balance.value && shareCount.value != 0) {
        player.value.balance.value -= totalPrice
        stock.averageBuyPrice.value = getAverageBuyPrice(stock, shareCount, totalPrice)
        stock.shares.value += shareCount.value
        val log = Logs(getDateToString(date.value),"Bought ${shareCount.value} share of\n${stock.name} at ${format.format(stock.price.value)} ")
        logs.value += log
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

fun sellStock(
    stock: Stock,
    shareCount: MutableState<Int>,
    player: MutableState<Player>,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>,
    format: DecimalFormat
) {
    if (shareCount.value != 0 && shareCount.value <= stock.shares.value) {
        player.value.yearProfit.value += getProfitLosses(shareCount, stock)
        player.value.totalProfit.value += getProfitLosses(shareCount, stock)
        player.value.balance.value += shareCount.value * stock.price.value
        stock.shares.value -= shareCount.value
        val log = Logs(getDateToString(date.value),"Sold ${shareCount.value} share of\n${stock.name} at ${format.format(stock.price.value)} ")
        logs.value += log
        shareCount.value = 0
    }


}

fun getProfitLosses(shareCount: MutableState<Int>, stock: Stock): Double {
    val totalInvested = shareCount.value * stock.averageBuyPrice.value
    val totalIfSellOut = shareCount.value * stock.price.value
    val difference = totalIfSellOut - totalInvested
    return difference * shareCount.value
}
fun getLoan(bank: MutableState<Bank>, player: MutableState<Player>) {
    player.value.balance.value += bank.value.creditLimit.value
    bank.value.loanBalance.value += bank.value.creditLimit.value
}
fun payoffLoan(bank: MutableState<Bank>, player: MutableState<Player>) {
    if (player.value.balance.value >= bank.value.loanBalance.value){
        player.value.balance.value -= bank.value.loanBalance.value
        bank.value.loanBalance.value = 0.0
    }
}