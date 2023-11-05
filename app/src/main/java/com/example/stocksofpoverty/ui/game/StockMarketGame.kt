package com.example.stocksofpoverty.ui.game

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.stocksofpoverty.R
import com.example.stocksofpoverty.data.Achievements
import com.example.stocksofpoverty.data.Bank
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.MarketOrder
import com.example.stocksofpoverty.data.News
import com.example.stocksofpoverty.data.Perk
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.SaveGame
import com.example.stocksofpoverty.data.Stock
import com.example.stocksofpoverty.data.YearlySummary
import com.example.stocksofpoverty.module.Update
import com.example.stocksofpoverty.module.executeMarketOrder
import com.example.stocksofpoverty.module.getProfitLosses
import com.example.stocksofpoverty.module.howManyShareAfford
import com.example.stocksofpoverty.module.saveGame
import com.example.stocksofpoverty.module.totalForBuying
import com.example.stocksofpoverty.module.update
import kotlinx.coroutines.launch
import java.text.DecimalFormat

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
    logs: MutableState<List<Logs>>,
    perks: MutableState<List<Perk>>,
    yearlySummary: MutableState<List<YearlySummary>>,
    achievements: MutableState<Achievements>,
    gameLost: MutableState<Boolean>,
    startGame: MutableState<Boolean>,
    orderForExecute: MutableState<List<MarketOrder>>
) {
    val selectedScreen = remember { mutableStateOf("Market") }
    val paused = remember { mutableStateOf(false) }
    val coroutine = rememberCoroutineScope()
    val popupList = remember { mutableStateOf(emptyList<String>()) }
    Update {
        if (!paused.value) {
            update(
                stocks,
                date,
                player,
                news,
                logs,
                perks,
                yearlySummary,
                banks,
                format,
                gameLost,
                orderForExecute,
                popupList
            )
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
                            yearlySummary.value.toList(),
                            perks.value.toList(),
                            achievements.value,
                            orderForExecute.value.toList()
                        ),
                        dataStore,
                        saveSlot.value
                    )
                }
            }
        }
    }
    if (popupList.value.isNotEmpty()) {
        PopupUI(popupList)
    }
    if (gameLost.value) {
        GameLostMenu()
    } else {
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
                            TopScreenIcons(
                                "Options Menu",
                                selectedScreen,
                                R.drawable.menu
                            )
                            TopScreenIcons(
                                "Market Orders List",
                                selectedScreen,
                                R.drawable.menu
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
                                    Text(
                                        text = "$${format.format(it)}", color =
                                        if (player.value.balance.value < -20000 && perks.value[2].active) {
                                            Color.Red
                                        } else if (player.value.balance.value < 0 && perks.value[2].active) {
                                            Color.Yellow
                                        } else if (player.value.balance.value < 0 && !perks.value[2].active) {
                                            Color.Red
                                        } else {
                                            Color.Green
                                        }
                                    )
                                }
                                Text(text = "Day ${date.value.day.value} Month ${date.value.month.value} Year ${date.value.year.value}")
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Button(onClick = { paused.value = !paused.value }) {
                                Text(
                                    text = if (paused.value) "Resume" else "Pause",
                                    fontSize = 10.sp
                                )
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
                    Stocks(
                        stocks,
                        player,
                        devMode,
                        date,
                        logs,
                        perks,
                        format,
                        orderForExecute,
                        popupList
                    )
                }
                AnimatedVisibility(
                    selectedScreen.value == "Player",
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    PlayerUI(
                        player,
                        perks,
                        format,
                        yearlySummary,
                        date,
                        logs,
                        devMode,
                        banks,
                        news,
                        achievements,
                        stocks,
                        popupList
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
                    BanksUI(player, banks, date, logs, format, player)
                }
                AnimatedVisibility(
                    selectedScreen.value == "News",
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    NewsUI(news)
                }
                AnimatedVisibility(
                    selectedScreen.value == "Options Menu",
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    OptionsMenu(
                        startGame,
                        player,
                        banks,
                        news,
                        logs,
                        yearlySummary,
                        saveSlot,
                        stocks,
                        date,
                        perks,
                        dataStore,
                        paused,
                        achievements,
                        orderForExecute
                    )
                }
                AnimatedVisibility(
                    selectedScreen.value == "Market Orders List",
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    MarketOrdersListUI(orderForExecute)
                }
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Stocks(
    stocks: MutableState<List<Stock>>,
    player: MutableState<Player>,
    devMode: Boolean,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>,
    perks: MutableState<List<Perk>>,
    format: DecimalFormat,
    orderForExecute: MutableState<List<MarketOrder>>,
    popupList: MutableState<List<String>>
) {
    val sortBy = remember { mutableStateOf("Name") }
    val ascendant = remember { mutableStateOf(true) }
    val autoSorting = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sort By", fontSize = 15.sp)
        Text(text = "Auto Sort", fontSize = 10.sp)
        Switch(
            checked = autoSorting.value,
            onCheckedChange = { autoSorting.value = it },
            modifier = Modifier
                .scale(0.5f)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val listOfSorts = listOf("Name", "Price", "Category", "Share", "Change")
            for (sorts in listOfSorts) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    SortByTabs(sorts, sortBy, ascendant)
                }
            }
        }
        val sortedStocks = remember(
            sortBy.value,
            ascendant.value,
            if (autoSorting.value) date.value.day.value else {
            }
        ) {
            mutableStateOf(
                stocks.value.sortedWith(compareBy { stock ->
                    when (sortBy.value) {
                        "Name" -> stock.name
                        "Price" -> stock.price.value
                        "Category" -> stock.category
                        "Shares Own" -> stock.shares.value
                        "Change" -> stock.percentageChange.value
                        else -> stock.name
                    }
                }).let {
                    if (!ascendant.value) it.reversed() else it
                }
            )
        }
        LazyColumn(
            modifier = Modifier,
        ) {
            items(sortedStocks.value, key = { it.name }) { stock ->
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .animateItemPlacement(
                            tween(1000)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    ShowStock(
                        stock,
                        devMode,
                        player,
                        date,
                        logs,
                        perks,
                        autoSorting,
                        orderForExecute,
                        popupList
                    )
                }
            }
        }
    }
}


@Composable
fun SortByTabs(
    sortBy: String,
    sortByState: MutableState<String>,
    ascendant: MutableState<Boolean>
) {
    Column(
        modifier = Modifier
    ) {
        Tab(
            selected = sortBy == sortByState.value,
            onClick = {
                if (sortBy != sortByState.value) {
                    sortByState.value = sortBy
                } else ascendant.value = !ascendant.value
            },
            selectedContentColor = Color.Yellow,
            unselectedContentColor = Color.Gray
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
            ) {
                Text(text = sortBy, fontSize = 9.sp)
                if (sortBy == sortByState.value && ascendant.value) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Sort ascendant")
                } else if (sortBy == sortByState.value && !ascendant.value) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Sort descendant")
                }
            }
        }
    }
}

@Composable
fun ShowStock(
    stock: Stock, devMode: Boolean, player: MutableState<Player>,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>,
    perks: MutableState<List<Perk>>,
    autoSorting: MutableState<Boolean>,
    orderForExecute: MutableState<List<MarketOrder>>,
    popupList: MutableState<List<String>>
) {
    val format = DecimalFormat("#.##")
    val expanded = remember { mutableStateOf(false) }
    val marketOrder = remember { mutableStateOf(false) }
    val sharesCount = remember { mutableStateOf(0) }
    val pharmaIcon = painterResource(R.drawable.stockfarmaicon)
    val techIcon = painterResource(R.drawable.stocktechicon)
    val financeIcon = painterResource(R.drawable.stockfianceicon)
    val energyIcon = painterResource(R.drawable.stockfianceicon)
    val initialAutoSorting = remember { mutableStateOf(autoSorting.value) }
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
                if (!expanded.value) {
                    initialAutoSorting.value = autoSorting.value
                    autoSorting.value = false
                } else {
                    autoSorting.value = initialAutoSorting.value
                }
                expanded.value = true
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
                if (stock.shares.value >= 100 && perks.value[6].active) {
                    val yearlyDividends = remember(stock.price.value, stock.shares.value) {
                        mutableStateOf((stock.shares.value * stock.price.value) * 0.05)
                    }
                    Text(
                        text = "Yearly Dividends ${format.format(yearlyDividends.value)}",
                        fontSize = 20.sp
                    )
                }
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
                if (marketOrder.value) Text(text = "Market order")
            }
            val typeOfOrders = listOf("Market Order", "Limit Order")
            val selectedOrder = remember { mutableStateOf(typeOfOrders[0]) }
            MarketOrderUI(
                typeOfOrders,
                selectedOrder,
                sharesCount,
                stock,
                player,
                format,
                expanded,
                logs,
                date,
                orderForExecute,
                popupList
            )
        }
    }
}

@Composable
fun MarketOrderUI(
    typeOfOrders: List<String>,
    selectedOrder: MutableState<String>,
    sharesCount: MutableState<Int>,
    stock: Stock,
    player: MutableState<Player>,
    format: DecimalFormat,
    expanded: MutableState<Boolean>,
    logs: MutableState<List<Logs>>,
    date: MutableState<Date>,
    orderForExecute: MutableState<List<MarketOrder>>,
    popupList: MutableState<List<String>>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Type Of Order")
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center
        ) {

            for (order in typeOfOrders) {
                Column(
                    modifier = Modifier
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = order)
                    RadioButton(
                        selected = selectedOrder.value == order,
                        onClick = { selectedOrder.value = order })
                }
            }
        }
        AnimatedVisibility(selectedOrder.value == typeOfOrders[0]) {
            val buying = remember { mutableStateOf(true) }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BuyingOrSellingMarkerOrderUI(buying, stock)
                GainingOrLosingUI(player, stock, sharesCount, format, buying)
                ShareCountsMenu(sharesCount, stock, buying, player)
                ExecuteOrCancelOrderUI(
                    expanded,
                    sharesCount,
                    onExecute = {
                        executeMarketOrder(
                            selectedOrder,
                            buying,
                            sharesCount,
                            logs,
                            date,
                            stock,
                            player,
                            format,
                            popupList,
                        )
                    }
                )
            }
        }
        AnimatedVisibility(selectedOrder.value == typeOfOrders[1]) {
            val buying = remember { mutableStateOf(true) }
            val type = listOf("Date", "Percentage Change", "Price")
            val selectedType = remember { mutableStateOf(type[0]) }
            val dropMenuExpand = remember { mutableStateOf(false) }
            val repeatOrder = remember { mutableStateOf(false) }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BuyingOrSellingMarkerOrderUI(buying, stock)
                Text(text = "By")
                Button(onClick = { dropMenuExpand.value = !dropMenuExpand.value }) {
                    Text(text = selectedType.value)
                    LimitOrderDropDownMenu(type, selectedType, dropMenuExpand)
                }
                ShareCountsMenu(sharesCount, stock, buying, player)
                when (selectedType.value) {
                    "Date" -> DateSelectorUI(
                        expanded,
                        sharesCount,
                        repeatOrder
                    ) { daysForOrder ->
                        executeMarketOrder(
                            selectedOrder,
                            buying,
                            sharesCount,
                            logs,
                            date,
                            stock,
                            player,
                            format,
                            popupList,
                            selectedType,
                            repeatOrder.value,
                            daysForOrder = daysForOrder,
                            ordersList = orderForExecute
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DateSelectorUI(
    expanded: MutableState<Boolean>,
    sharesCount: MutableState<Int>,
    repeatOrder: MutableState<Boolean>,
    onExecute: (Int) -> Unit
) {
    val days = remember { mutableStateOf(1) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DateNumberSelectorUI("Days to execute order",days)
        ExecuteOrCancelOrderUI(
            expanded,
            sharesCount,
            onExecute = { onExecute(days.value) }
        )
        Text(text = "Repeat order")
        Switch(checked = repeatOrder.value, onCheckedChange = {repeatOrder.value = it})
    }
}

@Composable
fun DateNumberSelectorUI(
    name: String,
    mutableInt: MutableState<Int>
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = name)
        Text(text = mutableInt.value.toString())
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                if (mutableInt.value > 5) mutableInt.value -= 5 else mutableInt.value = 0
            }
            ) {
                Text(text = "-5")
            }
            Button(onClick = { if (mutableInt.value > 0) mutableInt.value -= 1 }) {
                Text(text = "-1")
            }
            Button(onClick = { mutableInt.value++ }) {
                Text(text = "+1")
            }
            Button(onClick = { mutableInt.value += 5 }) {
                Text(text = "+5")
            }
        }
        if (mutableInt.value != 0){
            Button(onClick = { mutableInt.value = 0}) {
                Text(text = "Reset")
            }
        }
    }
}

@Composable
fun LimitOrderDropDownMenu(
    type: List<String>,
    selectedType: MutableState<String>,
    dropMenuExpand: MutableState<Boolean>
) {
    DropdownMenu(
        expanded = dropMenuExpand.value,
        onDismissRequest = { dropMenuExpand.value = false }) {
        for (typeName in type) {
            DropdownMenuItem(
                text = { Text(text = typeName) },
                onClick = {
                    selectedType.value = typeName
                    dropMenuExpand.value = false
                }
            )
        }
    }
}

@Composable
fun ExecuteOrCancelOrderUI(
    expanded: MutableState<Boolean>,
    sharesCount: MutableState<Int>,
    onExecute: () -> Unit
) {
    Row(
    ) {
        Button(onClick = {
            expanded.value = false
            sharesCount.value = 0
        }) {
            Text(text = "Cancel Order")
        }
        Button(onClick = {
            expanded.value = false
            onExecute()
        }) {
            Text(text = "Execute Order")
        }
    }


}

@Composable
fun ShareCountsMenu(
    sharesCount: MutableState<Int>,
    stock: Stock,
    buying: MutableState<Boolean>,
    player: MutableState<Player>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Shares")
        Text(text = sharesCount.value.toString())
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { if (sharesCount.value >= 5) sharesCount.value -= 5 }) {
                Text(text = "-5")
            }
            Button(onClick = { if (sharesCount.value >= 1) sharesCount.value -= 1 }) {
                Text(text = "-1")
            }
            Button(onClick = { if (buying.value) sharesCount.value += 1 else if (sharesCount.value < stock.shares.value) sharesCount.value += 5 }) {
                Text(text = "+1")
            }
            Button(onClick = { if (buying.value) sharesCount.value += 5 else if (sharesCount.value < stock.shares.value) sharesCount.value += 5 }) {
                Text(text = "+5")
            }
        }
        if (sharesCount.value > 0) {
            Button(onClick = { sharesCount.value = 0 }) {
                Text(text = "Reset")
            }
        } else {
            Button(onClick = {
                if (buying.value) {
                    howManyShareAfford(player, stock, sharesCount)
                } else {
                    sharesCount.value = stock.shares.value
                }
            }) {
                Text(text = "Max")
            }
        }
    }
}

@Composable
fun BuyingOrSellingMarkerOrderUI(buying: MutableState<Boolean>, stock: Stock) {
    Row(horizontalArrangement = Arrangement.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Buy")
            RadioButton(selected = buying.value, onClick = { buying.value = true })
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Sell")
            RadioButton(
                selected = !buying.value, onClick = { buying.value = false },
                enabled = stock.shares.value > 0
            )
        }
    }
}

@Composable
fun GainingOrLosingUI(
    player: MutableState<Player>,
    stock: Stock,
    sharesCount: MutableState<Int>,
    format: DecimalFormat,
    buying: MutableState<Boolean>
) {
    if (sharesCount.value != 0) {
        Text(text = "Total ${totalForBuying(stock, sharesCount, format)}")
        if (!buying.value) {
            val profitLoses = getProfitLosses(sharesCount, stock)
            if (profitLoses.toDouble() >= 0) {
                Text(
                    text = "Gaining ${getProfitLosses(sharesCount, stock)}",
                    color = Color.Green
                )
            } else {
                Text(
                    text = "Losing ${getProfitLosses(sharesCount, stock)}",
                    color = Color.Red
                )
            }
        }
    }
}