package com.example.stocksofpoverty.module

import androidx.compose.runtime.MutableState
import com.example.stocksofpoverty.data.Bank
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.News
import com.example.stocksofpoverty.data.Perk
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.Stock
import com.example.stocksofpoverty.data.YearlySummary
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.random.Random


fun update(
    stocks: MutableState<List<Stock>>,
    date: MutableState<Date>,
    player: MutableState<Player>,
    news: MutableState<List<News>>,
    logs: MutableState<List<Logs>>,
    perks: MutableState<List<Perk>>,
    yearlySummary: MutableState<List<YearlySummary>>,
    banks: MutableState<List<Bank>>,
    format: DecimalFormat
) {
    updateStockPrice(stocks)
    updateDate(date)
    updateYearlySummary(yearlySummary,date,player)
    taxAndInterest(player,banks,date,perks,logs,format)
}

fun updateYearlySummary(
    yearlySummary: MutableState<List<YearlySummary>>,
    date: MutableState<Date>,
    player: MutableState<Player>
) {
    if (date.value.day.value == 1 && date.value.month.value == 1 && date.value.year.value != 1){
        val summary = YearlySummary(
            "Year ${date.value.year.value - 1}",
            player.value.balance.value,
            player.value.totalDebt.value,
            player.value.yearlyInterestPaid.value,
            player.value.yearProfit.value,
            player.value.expectedIncomeTax.value
        )
        yearlySummary.value += summary
    }
}

fun updateStockPrice(stocks: MutableState<List<Stock>>) {
    val priceSensitivity = 0.01
    val supplySensitivity = 0.5
    for (stock in stocks.value) {
        val supplyDemandDif = stock.demand - stock.supply
        val randomPriceIncrease = Random.nextDouble() * supplyDemandDif
        val randomFluctuations = (Random.nextDouble() * 0.4) - 0.2
        if (supplyDemandDif > 0) {
            stock.price.value += randomPriceIncrease * priceSensitivity + randomFluctuations
            stock.supply += randomPriceIncrease * supplySensitivity + randomFluctuations
        } else if (supplyDemandDif < 0) {
            stock.price.value -= abs(randomPriceIncrease * priceSensitivity) + randomFluctuations
            stock.supply -= abs(randomPriceIncrease * supplySensitivity) + randomFluctuations
        }
    }
}

fun updateDate(date: MutableState<Date>) {
    date.value.day.value++
    if (date.value.day.value > 30){
        date.value.day.value = 1
        date.value.month.value++
        if (date.value.month.value > 12){
            date.value.month.value = 1
            date.value.year.value++
        }
    }
}





