package com.example.stocksofpoverty.ui.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
import com.example.stocksofpoverty.data.News
import com.example.stocksofpoverty.data.Perk
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.SaveGame
import com.example.stocksofpoverty.data.Stock
import com.example.stocksofpoverty.data.YearlySummary
import com.example.stocksofpoverty.data.getIInitialYearlySummary
import com.example.stocksofpoverty.data.getInitialAchievements
import com.example.stocksofpoverty.data.getInitialBanks
import com.example.stocksofpoverty.data.getInitialDate
import com.example.stocksofpoverty.data.getInitialLog
import com.example.stocksofpoverty.data.getInitialMarketOrderList
import com.example.stocksofpoverty.data.getInitialNewsList
import com.example.stocksofpoverty.data.getInitialPerks
import com.example.stocksofpoverty.data.getInitialPlayer
import com.example.stocksofpoverty.data.getInitialStockList
import com.example.stocksofpoverty.module.getAllSaveGames
import com.example.stocksofpoverty.module.loadSave
import com.example.stocksofpoverty.module.removeSaveGame
import com.example.stocksofpoverty.module.startNewGame
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@Composable
fun MainMenu(dataStore: DataStore<Preferences>) {
    val gameLost = remember { mutableStateOf(false) }
    val achievements = remember { mutableStateOf(getInitialAchievements()) }
    val stocks = remember { mutableStateOf(getInitialStockList()) }
    val player = remember { mutableStateOf(getInitialPlayer()) }
    val date = remember { mutableStateOf(getInitialDate()) }
    val logs = remember { mutableStateOf(getInitialLog()) }
    val banks = remember { mutableStateOf(getInitialBanks()) }
    val news = remember { mutableStateOf(getInitialNewsList()) }
    val perks = remember { mutableStateOf(getInitialPerks()) }
    val yearlySummary = remember { mutableStateOf(getIInitialYearlySummary()) }
    val format = DecimalFormat("#.##")
    val devMode = remember { mutableStateOf(false) }
    val saveSlot = remember { mutableStateOf(0) }
    val startGame = remember { mutableStateOf(false) }
    val loadingGame = remember { mutableStateOf(false) }
    val orderForExecute = remember { mutableStateOf(getInitialMarketOrderList()) }
    if (startGame.value) {
        StockMarketGame(
            stocks,
            dataStore,
            player,
            date,
            format,
            devMode.value,
            saveSlot,
            banks,
            news,
            logs,
            perks,
            yearlySummary,
            achievements,
            gameLost,
            startGame,
            orderForExecute
        )
    } else if (!loadingGame.value) {
        MainMenuUI(
            stocks,
            player,
            date,
            saveSlot,
            startGame,
            dataStore,
            loadingGame,
            perks,
            banks,
            news,
            logs,
            yearlySummary,
            devMode,
            achievements
        )
    } else if (!startGame.value && loadingGame.value) {
        LoadGameUI(dataStore, loadingGame) { saveGame ->
            loadSave(
                saveGame,
                stocks,
                player,
                date,
                saveSlot,
                startGame,
                loadingGame,
                logs,
                banks,
                news,
                achievements,
                gameLost
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadGameUI(
    dataStore: DataStore<Preferences>,
    loadingGame: MutableState<Boolean>,
    onLoadSave: (SaveGame) -> Unit
) {
    val coroutine = rememberCoroutineScope()
    val loadingSaves = remember { mutableStateOf(true) }
    val triggerRecomposition = remember { mutableStateOf(false) }
    val saveGamesList = remember { mutableListOf<SaveGame>() }
    LaunchedEffect(saveGamesList, triggerRecomposition.value) {
        saveGamesList.addAll(getAllSaveGames(dataStore))
        loadingSaves.value = false
    }
    if (loadingSaves.value) {
        CircularProgressIndicator()
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            IconButton(onClick = { loadingGame.value = false }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
            ) {
                LazyColumn(
                    modifier = Modifier,
                    content = {
                        if (saveGamesList.isNotEmpty()) {
                            item {
                                Button(onClick = {
                                    for (saveGames in saveGamesList) {
                                        coroutine.launch {
                                            removeSaveGame(dataStore, saveGames.saveSlot)
                                            triggerRecomposition.value = !triggerRecomposition.value
                                        }
                                    }
                                }) {
                                    Text(text = "Remove All Saves")
                                }
                            }
                            items(saveGamesList) {
                                Box(
                                    modifier = Modifier
                                        .padding(10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ShowSaveGame(it,
                                        onLoadSave = { saveGame ->
                                            onLoadSave(saveGame)
                                        },
                                        onRemoveSave = { removeSave, saveSlot ->
                                            coroutine.launch {
                                                val confirmDeletion =
                                                    removeSaveGame(dataStore, saveSlot)
                                                if (confirmDeletion) {
                                                    saveGamesList.remove(removeSave)
                                                    triggerRecomposition.value =
                                                        !triggerRecomposition.value
                                                }
                                            }
                                        }
                                    )
                                }

                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ShowSaveGame(
    saveGame: SaveGame,
    onLoadSave: (SaveGame) -> Unit,
    onRemoveSave: (SaveGame, Int) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .animateContentSize()
            .clickable {
                expanded.value = !expanded.value
            }
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = saveGame.player.name)
        Text(text = "${saveGame.player.name} Balance ${saveGame.player.balance.value}")
        Text(text = "Day ${saveGame.date.day.value} Month ${saveGame.date.month.value} Year ${saveGame.date.year.value}")
        AnimatedVisibility(expanded.value) {
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {
                    onRemoveSave(saveGame, saveGame.saveSlot)
                    expanded.value = false
                }) {
                    Text(text = "Delete")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { onLoadSave(saveGame) }) {
                    Text(text = "Load")
                }
                Spacer(modifier = Modifier.weight(1f))
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuUI(
    stocks: MutableState<List<Stock>>,
    player: MutableState<Player>,
    date: MutableState<Date>,
    saveSlot: MutableState<Int>,
    startGame: MutableState<Boolean>,
    dataStore: DataStore<Preferences>,
    loadingGame: MutableState<Boolean>,
    perks: MutableState<List<Perk>>,
    banks: MutableState<List<Bank>>,
    news: MutableState<List<News>>,
    logs: MutableState<List<Logs>>,
    yearlySummary: MutableState<List<YearlySummary>>,
    devMode: MutableState<Boolean>,
    achievements: MutableState<Achievements>
) {
    var selectingName by remember { mutableStateOf(false)}
    Image(
        painter = painterResource(R.drawable.stonksbackground),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxSize()
    )
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Welcome\n\nTo\n\nStock Of Poverty",
            fontSize = 40.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))
        if (selectingName){
            var nameText by remember { mutableStateOf("") }
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = nameText,
                    onValueChange = {nameText = it},
                    placeholder = { Text(text = "Player Name")}
                )
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {selectingName = false}) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = {
                        player.value.name = nameText
                        startNewGame(
                            saveSlot,
                            player,
                            date,
                            startGame,
                            stocks,
                            dataStore,
                            perks,
                            banks,
                            news,
                            logs,
                            yearlySummary,
                            achievements
                        )
                    }) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
        if (!selectingName) {
            Button(onClick = {
                selectingName = true
            }) {
                Text(text = "New Game", fontSize = 20.sp)
            }
            Button(onClick = {
                loadingGame.value = true
            }) {
                Text(text = "Load", fontSize = 20.sp)
            }
        }
        Button(
            onClick = { devMode.value = !devMode.value },
        ) {
            Text(text = if (devMode.value) "Dev Mode On" else "Dev Mode off")
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}