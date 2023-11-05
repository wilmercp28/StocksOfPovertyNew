package com.example.stocksofpoverty.module

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.MarketOrder
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.getDateToString

fun checkOrders(
    orderForExecute: MutableState<List<MarketOrder>>,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>,
    player: MutableState<Player>,
    popupList: MutableState<List<String>>
) {
    for (order in orderForExecute.value) {
        when (order.typeOfOrder) {
            "Date" -> dateOrdersUpdate(orderForExecute, order, date, logs, player, popupList)
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
