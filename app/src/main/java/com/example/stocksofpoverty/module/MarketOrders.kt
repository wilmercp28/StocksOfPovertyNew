package com.example.stocksofpoverty.module

import androidx.compose.runtime.MutableState
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.Stock
import java.text.DecimalFormat

fun executeMarketOrder(
    selectedOrder: MutableState<String>,
    buying: MutableState<Boolean>,
    sharesCount: MutableState<Int>,
    logs: MutableState<List<Logs>>,
    date: MutableState<Date>,
    stock: Stock,
    player: MutableState<Player>,
    format: DecimalFormat
) {
    when (selectedOrder.value) {
        "Market Order" -> marketOrder(buying, sharesCount, date, logs, stock, player, format)
        "Limit Order" -> marketOrder(buying, sharesCount, date, logs, stock, player, format)
    }
}

fun marketOrder(
    buying: MutableState<Boolean>,
    sharesCount: MutableState<Int>,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>,
    stock: Stock,
    player: MutableState<Player>,
    format: DecimalFormat
) {
    if (buying.value) {
        buyStock(stock, sharesCount, player, date, logs, format)
    } else {
        sellStock(stock, sharesCount, player, date, logs, format)
    }
}

