package com.example.stocksofpoverty.ui.game

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.stocksofpoverty.R
import com.example.stocksofpoverty.data.Bank
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.News
import com.example.stocksofpoverty.data.Perk
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.SaveGame
import com.example.stocksofpoverty.data.Stock
import com.example.stocksofpoverty.data.YearlySummary
import com.example.stocksofpoverty.module.Update
import com.example.stocksofpoverty.module.buyStock
import com.example.stocksofpoverty.module.getProfitLosses
import com.example.stocksofpoverty.module.saveGame
import com.example.stocksofpoverty.module.sellStock
import com.example.stocksofpoverty.module.update
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun StockMarketGame(
    stocks: MutableState<List<Stock>>,
    dataStore: DataStore<Preferences>,
    player: MutableState<Player>,
    date: MutableState<Date>,
    format: DecimalFormat,
    devMode: Boolean,
    saveSlot: MutableState<Int>,
    banks: MutableState<List<Bank>>,
    news: MutableState<List<News>>,
    perkPoint: MutableState<Int>,
    tier: MutableState<Int>,
    logs: MutableState<List<Logs>>,
    perks: MutableState<List<Perk>>,
    yearlySummary: MutableState<List<YearlySummary>>
) {
    val selectedScreen = remember { mutableStateOf("Market") }
    val paused = remember { mutableStateOf(false) }
    val coroutine = rememberCoroutineScope()
    Update {
        if (!paused.value) {
            update(stocks, date, player, news, logs, perks, yearlySummary, banks, format)
            if (date.value.day.value == 1 && date.value.month.value == 1) {
                coroutine.launch {
                    saveGame(
                        SaveGame(
                            saveSlot.value,
                            stocks.value.toList(),
                            player.value,
                            date.value,
                            banks.value.toList(),
                            news.value.toList(),
                            logs.value.toList(),
                            tier,
                            yearlySummary.value.toList(),
                            perks.value.toList()
                        ),
                        dataStore,
                        saveSlot.value
                    )
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        TopScreenIcons(
                            "Market",
                            selectedScreen,
                            R.drawable.financialprofit
                        )
                        TopScreenIcons(
                            "Player",
                            selectedScreen,
                            R.drawable.user
                        )
                        TopScreenIcons(
                            "Bank",
                            selectedScreen,
                            R.drawable.bank
                        )
                        TopScreenIcons(
                            "News",
                            selectedScreen,
                            R.drawable.newspaper
                        )
                        TopScreenIcons(
                            "Logs",
                            selectedScreen,
                            R.drawable.logs
                        )
                    }

                }
            )
        }, bottomBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .padding(40.dp)
                    ) {
                        Column(
                            modifier = Modifier
                        ) {
                            AnimatedContent(
                                player.value.balance.value, transitionSpec = {
                                   slideIntoContainer(AnimatedContentScope.SlideDirection.Up) with
                                           slideOutOfContainer(AnimatedContentScope.SlideDirection.Down)
                                }
                            ) {
                                Text(text = "$${format.format(it)}")
                            }
                            Text(text = "Day ${date.value.day.value} Month ${date.value.month.value} Year ${date.value.year.value}")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(onClick = { paused.value = !paused.value }) {
                            Text(text = if (paused.value) "Resume" else "Pause", fontSize = 10.sp)
                        }
                    }
                }
            )
        }

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                selectedScreen.value == "Market",
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Stocks(stocks, player, devMode, date, logs)
            }
            AnimatedVisibility(
                selectedScreen.value == "Player",
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                PlayerUI(
                    player,
                    perkPoint,
                    perks,
                    tier,
                    format,
                    yearlySummary,
                    date,
                    logs,
                    devMode
                )
            }
            AnimatedVisibility(
                selectedScreen.value == "Logs",
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LogsUI(logs)
            }
            AnimatedVisibility(
                selectedScreen.value == "Bank",
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                BanksUI(player, banks, date, logs, format, tier)
            }
            AnimatedVisibility(
                selectedScreen.value == "News",
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                NewsUI(news)
            }
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
fun Stocks(
    stocks: MutableState<List<Stock>>,
    player: MutableState<Player>,
    devMode: Boolean,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>
) {
    LazyColumn {
        items(stocks.value) { stock ->
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ShowStock(stock, devMode, player, date, logs)
            }
        }
    }

}

@Composable
fun ShowStock(
    stock: Stock, devMode: Boolean, player: MutableState<Player>,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>
) {
    val format = DecimalFormat("#.##")
    val expanded = remember { mutableStateOf(false) }
    val buying = remember { mutableStateOf(false) }
    val selling = remember { mutableStateOf(false) }
    val sharesCount = remember { mutableStateOf(0) }
    val pharmaIcon = painterResource(R.drawable.stockfarmaicon)
    val techIcon = painterResource(R.drawable.stocktechicon)
    val financeIcon = painterResource(R.drawable.stockfianceicon)
    val energyIcon = painterResource(R.drawable.stockfianceicon)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(
                if (stock.inEvent && devMode) {
                    Color.Yellow
                } else MaterialTheme.colorScheme.background
            )
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
            .clickable {
                expanded.value = !expanded.value
                sharesCount.value = 0
            },
    ) {

        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                        .padding(5.dp)
                ) {
                    Text(text = stock.category, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = when (stock.category) {
                        "Finance" -> financeIcon
                        "Tech" -> techIcon
                        "Pharma" -> pharmaIcon
                        "Energy" -> energyIcon

                        else -> financeIcon
                    },
                    contentDescription = "Stock Icon",
                    modifier = Modifier
                        .size(40.dp)
                )
            }
            Text(text = stock.name, fontSize = 30.sp)
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .background(
                        if (stock.percentageChange.value >= 0) Color.Green else Color.Red,
                        RoundedCornerShape(10.dp)
                    )
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Text(
                    text = format.format(stock.price.value),
                    fontSize = 20.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${stock.percentageChange.value}%",
                    fontSize = 20.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
            if (stock.shares.value > 0) {
                Text(
                    text = "${stock.shares.value} Shares at avg price of ${format.format(stock.averageBuyPrice.value)}",
                    fontSize = 20.sp
                )
            }
            if (devMode) {
                Text(text = "Demand ${format.format(stock.demand)} Supply ${format.format(stock.supply)}")
                Button(onClick = { stock.demand += 100 }) {
                    Text(text = "increase Demand")
                }
            }
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
                    BuyingAndSelling(
                        "Buy",
                        sharesCount,
                        buying,
                        stock,
                        player,
                        format,
                        date,
                        logs
                    )
                }
                AnimatedVisibility(selling.value) {
                    BuyingAndSelling(
                        "Sell",
                        sharesCount,
                        selling,
                        stock,
                        player,
                        format,
                        date,
                        logs
                    )
                }
            }
        }
    }

}

@Composable
fun BuyingAndSelling(
    label: String,
    shareCount: MutableState<Int>,
    isBuyingOrSelling: MutableState<Boolean>,
    stock: Stock,
    player: MutableState<Player>,
    format: DecimalFormat,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>
) {
    val profitLosses = remember(stock.price.value, shareCount.value) {
        format.format(
            getProfitLosses(
                shareCount,
                stock
            )
        ).toDouble()
    }
    val isLosingMoney = remember(profitLosses) { profitLosses < 0 }
    Column {
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
                if (label == "Buy") {
                    buyStock(stock, shareCount, player, date, logs, format, isBuyingOrSelling)
                } else {
                    sellStock(stock, shareCount, player, date, logs, format, isBuyingOrSelling)
                }

            }) {
                if (label == "Sell") {
                    Text(text = if (shareCount.value == 0) "Sell all" else "Sell")
                }
                if (label == "Buy") {
                    Text(text = if (shareCount.value == 0) "Max" else "Buy")
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (shareCount.value != 0 && label == "Sell") {
                Text(
                    text = if (isLosingMoney) "Losing " else "Gaining ",
                    color = if (isLosingMoney) Color.Red else Color.Green
                )
                Text(text = "${abs(profitLosses)}")
            } else if (shareCount.value != 0 && label == "Buy") {
                Text(text = "Total Price ${format.format(stock.price.value * shareCount.value)}")
            }
        }
    }
}