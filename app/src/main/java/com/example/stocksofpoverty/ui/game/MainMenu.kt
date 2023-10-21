package com.example.stocksofpoverty.ui.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.stocksofpoverty.data.Bank
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.News
import com.example.stocksofpoverty.data.Perk
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.SaveGame
import com.example.stocksofpoverty.data.Stock
import com.example.stocksofpoverty.data.getInitialBanks
import com.example.stocksofpoverty.data.getInitialDate
import com.example.stocksofpoverty.data.getInitialLog
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
    val stocks = remember { mutableStateOf(getInitialStockList()) }
    val player = remember { mutableStateOf(getInitialPlayer()) }
    val date = remember { mutableStateOf(getInitialDate()) }
    val logs =  remember { mutableStateOf(getInitialLog()) }
    val banks = remember { mutableStateOf(getInitialBanks()) }
    val news = remember { mutableStateOf(getInitialNewsList()) }
    val perkPoint = remember { mutableStateOf(1) }
    val tier = remember { mutableStateOf(0) }
    val perks = remember { mutableStateOf(getInitialPerks()) }
    val format = DecimalFormat("#.##")
    val devMode = false
    val saveSlot = remember { mutableStateOf(0) }
    val startGame = remember { mutableStateOf(false) }
    val loadingGame = remember { mutableStateOf(false) }
    if (startGame.value) {
        StockMarketGame(stocks, dataStore, player, date, format, devMode, saveSlot,banks,news,perkPoint,tier,logs,perks)
    } else if (!loadingGame.value) {
        MainMenuUI(stocks, player, date, saveSlot, startGame, dataStore, loadingGame,perkPoint,perks,banks,news,logs,tier)
    } else if (!startGame.value && loadingGame.value) {
        LoadGameUI(dataStore, loadingGame) { saveGame ->
            loadSave(saveGame, stocks, player, date, saveSlot, startGame, loadingGame,logs,banks,news,perkPoint,tier)
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
            it
            LazyColumn(
                modifier = Modifier,
                content = {
                    if (saveGamesList.isNotEmpty()) {
                        items(saveGamesList) {
                            Box(
                                modifier = Modifier
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                showSaveGame(it, saveGamesList, dataStore,
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

@Composable
fun showSaveGame(
    saveGame: SaveGame,
    saveGamesList: MutableList<SaveGame>,
    dataStore: DataStore<Preferences>,
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

@Composable
fun MainMenuUI(
    stocks: MutableState<List<Stock>>,
    player: MutableState<Player>,
    date: MutableState<Date>,
    saveSlot: MutableState<Int>,
    startGame: MutableState<Boolean>,
    dataStore: DataStore<Preferences>,
    loadingGame: MutableState<Boolean>,
    perkPoint: MutableState<Int>,
    perks: MutableState<List<Perk>>,
    banks: MutableState<List<Bank>>,
    news: MutableState<List<News>>,
    logs: MutableState<List<Logs>>,
    tier: MutableState<Int>
) {
    val coroutine = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome To Stock Of Poverty", fontSize = 25.sp)
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            startNewGame(saveSlot, player, date, startGame, stocks, dataStore,perkPoint,perks,banks,tier,news,logs)
        }) {
            Text(text = "New Game", fontSize = 20.sp)
        }
        Button(onClick = {
            loadingGame.value = true
        }) {
            Text(text = "Load", fontSize = 20.sp)
        }
    }
}