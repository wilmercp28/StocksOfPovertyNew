package com.example.stocksofpoverty.ui.game

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stocksofpoverty.data.MarketOrder

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MarketOrdersListUI(orderForExecute: MutableState<List<MarketOrder>>) {

    LazyColumn(
        content = {
            items(orderForExecute.value.reversed(), key = {it.id} ){
                order ->
                Box(modifier = Modifier
                    .animateItemPlacement(tween(200)))
                {
                    when (order.typeOfOrder) {
                        "Date" -> ShowMarketOrderDateUI(order, orderForExecute)
                    }
                }
            }
        }
    )
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
        Text(text = "Date Order")
        Divider()
        val day = order.dateToExecute[0]
        val month = order.dateToExecute[1]
        val year = order.dateToExecute[2]
        Text(text = "By")
        Text(text = "Day $day Month $month Year $year")
        Text(text = "For")
        Text(text = "${order.shares} Shares")
        Row() {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { orderForExecute.value -= order }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Order")
            }
        }
    }
}