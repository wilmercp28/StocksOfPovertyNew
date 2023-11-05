package com.example.stocksofpoverty.module

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.MarketOrder
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
    format: DecimalFormat,
    popupList: MutableState<List<String>>,
    selectedType: MutableState<String> = mutableStateOf(""),
    repeatOrder: Boolean = false,
    daysForOrder: Int = 0,
    ordersList: MutableState<List<MarketOrder>> = mutableStateOf(emptyList()),
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
            daysForOrder,
            selectedType,
            ordersList,
            popupList,
            repeatOrder
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
        buyStock(stock, sharesCount, player, date, logs)
    } else {
        sellStock(stock, sharesCount, player, date, logs)
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
    daysForOrder: Int,
    selectedType: MutableState<String>,
    ordersList: MutableState<List<MarketOrder>>,
    popupList: MutableState<List<String>>,
    repeatOrder: Boolean
) {
    when (selectedType.value) {
        "Date" -> dateOrders(
            buying,
            sharesCount,
            date,
            logs,
            stock,
            format,
            daysForOrder,
            ordersList,
            selectedType,
            popupList,
            repeatOrder
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
    daysForOrder: Int,
    ordersList: MutableState<List<MarketOrder>>,
    selectedType: MutableState<String>,
    popupList: MutableState<List<String>>,
    repeatOrder: Boolean
) {
    if (sharesCount.value == 0){
        popupList.value += "Error, Shares cant be 0"
    } else {
        ordersList.value += MarketOrder(
            stock = stock,
            buying = buying.value,
            shares = sharesCount.value,
            isLimitOrder = true,
            typeOfOrder = selectedType.value,
            repeat = mutableStateOf(repeatOrder),
            initialDaysToExecute = daysForOrder,
            daysToExecute = mutableStateOf(daysForOrder)
        )
        popupList.value += "Date order created"
    }
}

