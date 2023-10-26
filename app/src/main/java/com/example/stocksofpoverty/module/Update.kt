package com.example.stocksofpoverty.module

import android.util.Log
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
    updateStockPrice(stocks, date, format)
    updateDate(date)
    updateYearlySummary(yearlySummary, date, player)
    taxAndInterest(player, banks, date, perks, logs, format)
    if (date.value.month.value % 4 == 0 && date.value.day.value == 1) {
        updateNews(news, player, date, stocks)
    }
    applyMonthlyDemandChanges(stocks, date)
}

fun applyMonthlyDemandChanges(stocks: MutableState<List<Stock>>, date: MutableState<Date>) {
    if (date.value.day.value == 1) {
        for (stock in stocks.value) {
            if (stock.inEvent && stock.demandChangesList.isNotEmpty()) {
                stock.demand += stock.demandChangesList.first()
                Log.d(
                    stock.name,
                    "List ${stock.demandChangesList} Apply and Remove ${stock.demandChangesList.first()}"
                )
                stock.demandChangesList.remove(stock.demandChangesList.first())
                if (stock.demandChangesList.isEmpty()) stock.inEvent = false
            }
        }
    }
}

fun updateNews(
    news: MutableState<List<News>>,
    player: MutableState<Player>,
    date: MutableState<Date>,
    stocks: MutableState<List<Stock>>
) {
    val maxNewsPossible = 3
    var currentNewsGenerated = 0
    for (stock in stocks.value) {
        val random = Random.nextDouble()
        if (random <= 0.10 && currentNewsGenerated < maxNewsPossible && !stock.inEvent) {
            currentNewsGenerated += 1
            generateNews(news, date, stock)
            Log.d("News", "News Generated")
        }
    }
}

fun updateYearlySummary(
    yearlySummary: MutableState<List<YearlySummary>>,
    date: MutableState<Date>,
    player: MutableState<Player>
) {
    if (date.value.day.value == 1 && date.value.month.value == 1 && date.value.year.value != 1) {
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

fun updateStockPrice(
    stocks: MutableState<List<Stock>>,
    date: MutableState<Date>,
    format: DecimalFormat
) {
    val priceSensitivity = 0.01
    val supplySensitivity = 0.5
    for (stock in stocks.value) {
        getPercentageChange(date, stock, format)
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

fun getPercentageChange(date: MutableState<Date>, stock: Stock, format: DecimalFormat) {
    if (date.value.day.value == 1 && date.value.month.value == 1) {
        stock.lastYearPrice.value = stock.price.value
    }
    stock.percentageChange.value =
        format.format(((stock.price.value - stock.lastYearPrice.value) / stock.lastYearPrice.value) * 100).toDouble()
}

fun updateDate(date: MutableState<Date>) {
    date.value.day.value++
    if (date.value.day.value > 30) {
        date.value.day.value = 1
        date.value.month.value++
        if (date.value.month.value > 12) {
            date.value.month.value = 1
            date.value.year.value++
        }
    }
}





