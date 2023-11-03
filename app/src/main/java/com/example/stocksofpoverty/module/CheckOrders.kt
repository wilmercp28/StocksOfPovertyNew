package com.example.stocksofpoverty.module

import androidx.compose.runtime.MutableState
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.MarketOrder
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.getDateToString

fun checkOrders(
    orderForExecute: MutableState<List<MarketOrder>>,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>,
    player: MutableState<Player>
) {
    for (orders in orderForExecute.value) {
        when (orders.typeOfOrder) {
            "Date" -> dateOrdersUpdate(orderForExecute, orders, date, logs, player)
        }
    }
}

fun dateOrdersUpdate(
    orderForExecute: MutableState<List<MarketOrder>>,
    orders: MarketOrder,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>,
    player: MutableState<Player>
) {
    if (
        date.value.day.value == orders.dateToExecute[0] &&
        date.value.month.value == orders.dateToExecute[1] &&
        date.value.year.value == orders.dateToExecute[2]
    ) {
        if (orders.buying) {
            if (player.value.balance.value >= orders.shares * orders.stockName.price.value) {
                player.value.balance.value -= orders.shares * orders.stockName.price.value
                orders.stockName.shares.value += orders.shares
                orderForExecute.value -= orders
                logs.value += Logs(
                    getDateToString(date.value),
                    "Date order executed for a total price of ${orders.shares * orders.stockName.price.value}"
                )
            } else {
                logs.value += Logs(
                    getDateToString(date.value),
                    "Date order cancel for insufficient funds"
                )
                orderForExecute.value -= orders
            }
        } else {






        }
    }
}