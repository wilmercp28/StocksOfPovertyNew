package com.example.stocksofpoverty.module

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.MarketOrder
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.Stock

fun executeMarketOrder(
    selectedOrder: MutableState<String>,
    buying: MutableState<Boolean>,
    sharesCount: MutableState<Int>,
    logs: MutableState<List<Logs>>,
    date: MutableState<Date>,
    stock: Stock,
    player: MutableState<Player>,
    popupList: MutableState<List<String>>,
    selectedType: MutableState<String> = mutableStateOf(""),
    repeatOrder: Boolean = false,
    daysForOrder: Int = 0,
    percentageChange: Double = 0.0,
    priceToOrder: Double = 0.0,
    higher: Boolean = true,
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
        )

        "Limit Order" -> limitOrder(
            buying,
            sharesCount,
            date,
            logs,
            stock,
            player,
            daysForOrder,
            selectedType,
            ordersList,
            popupList,
            repeatOrder,
            percentageChange,
            priceToOrder,
            higher
        )
    }
}

fun marketOrder(
    buying: MutableState<Boolean>,
    sharesCount: MutableState<Int>,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>,
    stock: Stock,
    player: MutableState<Player>
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
    daysForOrder: Int,
    selectedType: MutableState<String>,
    ordersList: MutableState<List<MarketOrder>>,
    popupList: MutableState<List<String>>,
    repeatOrder: Boolean,
    percentageChange: Double,
    priceToOrder: Double,
    higher: Boolean
) {
    when (selectedType.value) {
        "Date" -> dateOrders(
            buying,
            sharesCount,
            date,
            logs,
            stock,
            daysForOrder,
            ordersList,
            selectedType,
            popupList,
            repeatOrder
        )

        "Percentage Change" -> percentageChangeOrder(
            buying,
            sharesCount,
            stock,
            ordersList,
            selectedType,
            popupList,
            percentageChange
        )
        "Price" -> priceOrder(
            buying,
            sharesCount,
            stock,
            ordersList,
            selectedType,
            popupList,
            priceToOrder,
            higher
        )
    }
}


fun priceOrder(
    buying: MutableState<Boolean>,
    sharesCount: MutableState<Int>,
    stock: Stock,
    ordersList: MutableState<List<MarketOrder>>,
    selectedType: MutableState<String>,
    popupList: MutableState<List<String>>,
    priceToOrder: Double,
    higher: Boolean
) {
    if (sharesCount.value == 0){
        popupList.value += "Error, Shares cant be 0"
    } else {
     ordersList.value += MarketOrder(
         stock = stock,
         buying = buying.value,
         shares = sharesCount.value,
         isLimitOrder = true,
         repeat = mutableStateOf(false),
         showPopup = mutableStateOf(true),
         typeOfOrder = selectedType.value,
         priceToOrder = priceToOrder,
         higher = higher
     )
        popupList.value += "Price order created"
    }
}
fun percentageChangeOrder(
    buying: MutableState<Boolean>,
    sharesCount: MutableState<Int>,
    stock: Stock,
    ordersList: MutableState<List<MarketOrder>>,
    selectedType: MutableState<String>,
    popupList: MutableState<List<String>>,
    percentageChange: Double
) {
    if (sharesCount.value != 0) {
        ordersList.value += MarketOrder(
            stock = stock,
            buying = buying.value,
            shares = sharesCount.value,
            isLimitOrder = true,
            repeat = mutableStateOf(false),
            showPopup = mutableStateOf(true),
            typeOfOrder = selectedType.value,
            percentageChange = percentageChange
        )
        Log.d("Order Created",ordersList.value.toString())
        popupList.value += "Percentage change order created"

    } else {
        popupList.value += "Error, Shares cant be 0"
    }


}

fun dateOrders(
    buying: MutableState<Boolean>,
    sharesCount: MutableState<Int>,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>,
    stock: Stock,
    daysForOrder: Int,
    ordersList: MutableState<List<MarketOrder>>,
    selectedType: MutableState<String>,
    popupList: MutableState<List<String>>,
    repeatOrder: Boolean
) {
    if (sharesCount.value == 0) {
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

