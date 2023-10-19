package com.example.stocksofpoverty.ui.game

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.SaveGame
import com.example.stocksofpoverty.data.Stock
import com.example.stocksofpoverty.data.getInitialDate
import com.example.stocksofpoverty.data.getInitialPlayer
import com.example.stocksofpoverty.data.getInitialStockList
import com.example.stocksofpoverty.module.getAllSaveGames
import com.example.stocksofpoverty.module.startNewGame
import java.text.DecimalFormat

@Composable
fun MainMenu(dataStore: DataStore<Preferences>) {
    val stocks = remember { mutableStateOf(getInitialStockList()) }
    val player = remember { mutableStateOf(getInitialPlayer()) }
    val date = remember { mutableStateOf(getInitialDate()) }
    val format = DecimalFormat("#.##")
    val devMode = true
    val saveSlot = remember { mutableStateOf(0) }
    val startGame = remember { mutableStateOf(false) }
    val loadingGame = remember { mutableStateOf(false) }
    if (startGame.value) {
        StockMarketGame(stocks, dataStore, player, date, format, devMode, saveSlot)
    } else if (!loadingGame.value) {
        MainMenuUI(stocks, player, date, saveSlot, startGame, dataStore, loadingGame)
    } else if (!startGame.value && loadingGame.value) {
        LoadGameUI(dataStore)
    }

}

@Composable
fun LoadGameUI(dataStore: DataStore<Preferences>) {
    val saveGames = mutableListOf<SaveGame>()
    LaunchedEffect(saveGames) {
        saveGames.addAll(getAllSaveGames(dataStore))
        Log.d("AllSaves",saveGames.toString())
    }
    Box(modifier = Modifier.fillMaxSize())
    {
        LazyColumn(
            modifier = Modifier,
            content = {
                items(saveGames) {
                    Log.d(it.saveSlot.toString(), it.saveSlot.toString())
                    showSaveGame(it)
                }
            }
        )
    }
}

@Composable
fun showSaveGame(saveGame: SaveGame) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Log.d("${saveGame.saveSlot}",saveGame.toString())
        Text(text = "Save")
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
    loadingGame: MutableState<Boolean>
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome To Stock Of Poverty", fontSize = 25.sp)
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            startNewGame(saveSlot, player, date, startGame, stocks, dataStore)
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