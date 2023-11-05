package com.example.stocksofpoverty.ui.game

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stocksofpoverty.data.MarketOrder

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MarketOrdersListUI(orderForExecute: MutableState<List<MarketOrder>>) {

    LazyColumn(
        content = {
            Log.d("Orders",orderForExecute.value.toString())
            items(orderForExecute.value.reversed(), key = { it.id }) { order ->
                Box(
                    modifier = Modifier
                        .animateItemPlacement(tween(200))
                )
                {
                    when (order.typeOfOrder) {
                        "Date" -> ShowMarketOrderDateUI(order, orderForExecute)
                        "Percentage Change" -> ShowMarketOrderPercentageChangeUI(order,orderForExecute)
                    }
                }
            }
        }
    )
}
@Composable
fun ShowMarketOrderPercentageChangeUI(order: MarketOrder, orderForExecute: MutableState<List<MarketOrder>>) {
    Log.d("Order",order.typeOfOrder)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Percentage Change Order", fontSize = 20.sp)
        Text(text = order.stock.name, fontSize = 20.sp)
        Divider()
        Text(text = "At ${order.percentageChange}%")
        if (order.buying) Text(text = " Buying ${order.shares} Shares") else Text(text = " Selling ${order.shares} Shares")
        MarketOrderOptions(order, orderForExecute)
    }
}
@Composable
fun ShowMarketOrderDateUI(order: MarketOrder, orderForExecute: MutableState<List<MarketOrder>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Date Order", fontSize = 20.sp)
        Text(text = order.stock.name, fontSize = 20.sp)
        Divider()
        Text(text = "${order.daysToExecute.value} days until execute")
        if (order.buying) Text(text = " Buying ${order.shares} Shares") else Text(text = " Selling ${order.shares} Shares")
        MarketOrderOptions(order, orderForExecute)
    }
}

@Composable
fun MarketOrderOptions(order: MarketOrder, orderForExecute: MutableState<List<MarketOrder>>) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Repeat Mode")
            Switch(checked = order.repeat.value, onCheckedChange = { order.repeat.value = it })
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Show notification")
            Switch(
                checked = order.showPopup.value,
                onCheckedChange = { order.showPopup.value = it })
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Cancel Order")
            IconButton(onClick = { orderForExecute.value -= order }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Order")
            }
        }

    }
}