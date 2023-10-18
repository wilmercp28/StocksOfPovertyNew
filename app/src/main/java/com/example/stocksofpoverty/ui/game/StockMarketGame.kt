package com.example.stocksofpoverty.ui.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.stocksofpoverty.R
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.Stock
import com.example.stocksofpoverty.module.Update
import com.example.stocksofpoverty.module.update
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockMarketGame(
    stocks: MutableState<List<Stock>>,
    dataStore: DataStore<Preferences>,
    player: MutableState<Player>,
    date: MutableState<Date>,
    format: DecimalFormat
) {
    val selectedScreen = remember { mutableStateOf("Market") }
    val paused = remember { mutableStateOf(false) }
    Update(paused) {
        update(stocks,date,player)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row() {
                        TopScreenIcons(
                            "Market",
                            selectedScreen,
                            R.drawable.account_balance_fill0_wght400_grad0_opsz24
                        )
                        TopScreenIcons(
                            "Player",
                            selectedScreen,
                            R.drawable.account_balance_fill0_wght400_grad0_opsz24
                        )
                    }

                }
            )
        }, bottomBar = {
            TopAppBar(
                title = {
                    Row() {
                        Text(text = format.format(player.value.balance.value))
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "Day ${date.value.day.value} Month ${date.value.month.value} Year ${date.value.year.value}")
                    }
                }
            )
        }
    ) {
        it
        AnimatedVisibility(
            selectedScreen.value == "Market",
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Stocks(stocks, player)
        }
        AnimatedVisibility(
            selectedScreen.value == "Player",
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Stocks(stocks, player)
        }

    }
}

@Composable
fun TopScreenIcons(iconName: String, selectedScreen: MutableState<String>, Icon: Int) {
    val isSelected = selectedScreen.value == iconName
    IconButton(
        onClick = { selectedScreen.value = iconName }
    ) {
        Icon(
            painterResource(Icon), contentDescription = iconName,
            tint = if (isSelected) Color.Yellow else Color.Gray,
            modifier = Modifier
                .size(100.dp)
        )
    }

}


@Composable
fun Stocks(stocks: MutableState<List<Stock>>, player: MutableState<Player>) {
    LazyColumn {
        items(stocks.value) { stock ->
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ShowStock(stock)
            }
        }
    }

}

@Composable
fun ShowStock(stock: Stock) {
    val format = DecimalFormat("#.##")
    val expanded = remember { mutableStateOf(false) }
    val priceBoxColor = remember { mutableStateOf(Color.Green) }
    var buying = remember { mutableStateOf(false) }
    val selling = remember { mutableStateOf(false) }
    val sharesCount = remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
            .padding(10.dp),
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    expanded.value = !expanded.value
                },
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = stock.name, fontSize = 20.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = format.format(stock.price.value), fontSize = 20.sp,
                modifier = Modifier
                    .background(priceBoxColor.value, RoundedCornerShape(10.dp))
                    .padding(5.dp)
            )
        }
        if (stock.shares.value > 0) {
            Text(text = "${stock.shares.value} Shares at avg price of ${stock.averageBuyPrice.value}")
        }
        AnimatedVisibility(expanded.value) {
            Column(
                modifier = Modifier
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(!buying.value && !selling.value) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Button(onClick = { buying.value = true }) {
                            Text(text = "Buy")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        if (stock.shares.value != 0) {
                            Button(onClick = { selling.value = true }) {
                                Text(text = "Sell")
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                AnimatedVisibility(buying.value) {
                    buyingAndSelling("Buy", sharesCount, buying, stock)
                }
                AnimatedVisibility(selling.value) {
                    buyingAndSelling("Sell", sharesCount, selling, stock)
                }
            }
        }
    }
}

@Composable
fun buyingAndSelling(
    label: String,
    shareCount: MutableState<Int>,
    isBuyingOrSelling: MutableState<Boolean>,
    stock: Stock
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = { isBuyingOrSelling.value = false }) {
            Text(text = "Cancel")
        }
        IconButton(onClick = {
            if (shareCount.value != 0) {
                shareCount.value--
            }
        }) {
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Shares--")
        }
        Text(text = shareCount.value.toString())
        IconButton(onClick = {
            if (label == "Sell" && shareCount.value < stock.shares.value) {
                shareCount.value++
            } else if (label == "Buy") {
                shareCount.value++
            }
        }) {
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Shares++")
        }
        Button(onClick = {

        }) {
            Text(text = label)
        }
    }
}