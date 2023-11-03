package com.example.stocksofpoverty.module

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.MarketOrder
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.Stock
import com.example.stocksofpoverty.data.getDateToString
import java.text.DecimalFormat

fun executeMarketOrder(
    selectedOrder: MutableState<String>,
    buying: MutableState<Boolean>,
    sharesCount: MutableState<Int>,
    logs: MutableState<List<Logs>>,
    date: MutableState<Date>,
    stock: Stock,
    player: MutableState<Player>,
    format: DecimalFormat,
    selectedType: MutableState<String> = mutableStateOf(""),
    dateForOrder: List<Int> = emptyList(),
    ordersList: MutableState<List<MarketOrder>> = mutableStateOf(emptyList())
) {
    when (selectedOrder.value) {
        "Market Order" -> marketOrder(
            buying,
            sharesCount,
            date,
            logs,
            stock,
            player,
            format,
        )

        "Limit Order" -> limitOrder(
            buying,
            sharesCount,
            date,
            logs,
            stock,
            player,
            format,
            dateForOrder,
            selectedType,
            ordersList
        )
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

fun limitOrder(
    buying: MutableState<Boolean>,
    sharesCount: MutableState<Int>,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>,
    stock: Stock,
    player: MutableState<Player>,
    format: DecimalFormat,
    dateForOrder: List<Int>,
    selectedType: MutableState<String>,
    ordersList: MutableState<List<MarketOrder>>
) {
    when (selectedType.value) {
        "Date" -> dateOrders(
            buying,
            sharesCount,
            date,
            logs,
            stock,
            format,
            dateForOrder,
            ordersList,
            selectedType
        )
    }
}

fun dateOrders(
    buying: MutableState<Boolean>,
    sharesCount: MutableState<Int>,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>,
    stock: Stock,
    format: DecimalFormat,
    dateForOrder: List<Int>,
    ordersList: MutableState<List<MarketOrder>>,
    selectedType: MutableState<String>
) {
    if (date.value.year.value <= dateForOrder[2]) {
        if (date.value.month.value <= dateForOrder[1]) {
            if (date.value.day.value < dateForOrder[0]) {
                val newDateOrder = MarketOrder(
                    stockName = stock,
                    buying = buying.value,
                    shares = sharesCount.value,
                    isLimitOrder = true,
                    typeOfOrder = selectedType.value,
                    dateToExecute = dateForOrder
                )
                ordersList.value += newDateOrder
                val buying = if (buying.value) "Buy" else "Sell"
                logs.value += Logs(
                    getDateToString(date.value),
                    "New date order for ${stock.name}.$buying To be execute by Day${dateForOrder[0]} Month ${dateForOrder[1]} Year ${dateForOrder[2]}"
                )
            }
        }
    }
}

