package com.example.stocksofpoverty.module

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.MarketOrder
import com.example.stocksofpoverty.data.Player

fun checkOrders(
    orderForExecute: MutableState<List<MarketOrder>>,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>,
    player: MutableState<Player>,
    popupList: MutableState<List<String>>
) {
    for (order in orderForExecute.value) {
        when (order.typeOfOrder) {
            "Date" -> dateOrdersUpdate(
                orderForExecute,
                order,
                date,
                logs,
                player,
                popupList)
            "Percentage Change" -> percentageChangeOrdersUpdate(
                orderForExecute,
                order,
                date,
                logs,
                popupList,
                player
            )
            "Price" -> priceOrderUpdate(
                orderForExecute,
                order,
                date,
                logs,
                popupList,
                player)
        }
    }
}


fun priceOrderUpdate(
    orderForExecute: MutableState<List<MarketOrder>>,
    order: MarketOrder,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>,
    popupList: MutableState<List<String>>,
    player: MutableState<Player>
) {
    if (order.higher && order.priceToOrder < order.stock.price.value){
        if (order.buying){
            buyStock(order.stock, mutableStateOf(order.shares), player, date, logs)
            popupList.value += "Price Order buy Executed for ${order.stock.name}"
            orderForExecute.value -= order
        } else if (order.stock.shares.value >= order.shares){
            sellStock(order.stock, mutableStateOf(order.shares), player, date, logs)
            popupList.value += "Price Order sell Executed for ${order.stock.name}"
            orderForExecute.value -= order
        } else {
            popupList.value += "Price order sell cancel, insufficient shares"
            orderForExecute.value -= order
        }
    } else if (!order.higher && order.priceToOrder > order.stock.price.value){
        if (order.buying){
            buyStock(order.stock, mutableStateOf(order.shares), player, date, logs)
            popupList.value += "Price Order buy Executed for ${order.stock.name}"
            orderForExecute.value -= order
        } else if (order.stock.shares.value >= order.shares){
            sellStock(order.stock, mutableStateOf(order.shares), player, date, logs)
            popupList.value += "Price Order sell Executed for ${order.stock.name}"
            orderForExecute.value -= order
        } else {
            popupList.value += "Price order sell cancel, insufficient shares"
            orderForExecute.value -= order
        }
    }
}

fun percentageChangeOrdersUpdate(
    orderForExecute: MutableState<List<MarketOrder>>,
    order: MarketOrder,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>,
    popupList: MutableState<List<String>>,
    player: MutableState<Player>
) {
    if (order.percentageChange > 0) {
        if (order.percentageChange < order.stock.percentageChange.value) {
            if (order.buying) {
                buyStock(order.stock, mutableStateOf(order.shares), player, date, logs)
                orderForExecute.value -= order
                popupList.value += "Percentage Order buy Executed for ${order.stock.name}"
                orderForExecute.value -= order
            } else if (order.stock.shares.value > order.shares){
                sellStock(order.stock, mutableStateOf(order.shares), player, date, logs)
                orderForExecute.value -= order
                popupList.value += "Percentage Order sell Executed for ${order.stock.name}"
                orderForExecute.value -= order
            } else {
                popupList.value += "PercentageChange order sell cancel, insufficient shares"
                orderForExecute.value -= order
            }
        }
    } else {
        if (order.percentageChange > order.stock.percentageChange.value) {
            if (order.buying) {
                buyStock(order.stock, mutableStateOf(order.shares), player, date, logs)
                orderForExecute.value -= order
                popupList.value += "Percentage Order buy Executed for ${order.stock.name}"
                orderForExecute.value -= order
            } else if (order.stock.shares.value > order.shares){
                sellStock(order.stock, mutableStateOf(order.shares), player, date, logs)
                orderForExecute.value -= order
                popupList.value += "Percentage Order sell Executed for ${order.stock.name}"
                orderForExecute.value -= order
            } else {
                popupList.value += "PercentageChange order sell cancel, insufficient shares"
                orderForExecute.value -= order
            }
        }
    }
}

fun dateOrdersUpdate(
    orderForExecute: MutableState<List<MarketOrder>>,
    order: MarketOrder,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>,
    player: MutableState<Player>,
    popupList: MutableState<List<String>>
) {
    if (order.daysToExecute.value == 1 && order.buying) {
        if (player.value.balance.value >= order.stock.price.value * order.shares) {
            buyStock(order.stock, mutableStateOf(order.shares), player, date, logs)
            if (order.showPopup.value) {
                popupList.value += "${order.stock.name} Date order executed"
            }
            if (order.repeat.value) {
                order.daysToExecute.value = order.initialDaysToExecute
            } else {
                orderForExecute.value -= order
            }
        } else {
            popupList.value += "Date order buy cancel, insufficient funds"
            orderForExecute.value -= order
        }
    } else if (order.daysToExecute.value == 1 && !order.buying) {
        if (order.stock.shares.value >= order.shares) {
            sellStock(order.stock, mutableStateOf(order.shares), player, date, logs)
            if (order.showPopup.value) {
                popupList.value += "${order.stock.name} Date order executed"
            }
            if (order.repeat.value) {
                order.daysToExecute.value = order.initialDaysToExecute
            } else {
                orderForExecute.value -= order
            }
        } else {
            popupList.value += "Date order sell cancel, insufficient shares"
            orderForExecute.value -= order
        }
    } else {
        order.daysToExecute.value--
    }

}
