package com.example.stocksofpoverty.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
import com.example.stocksofpoverty.module.saveGame
import kotlinx.coroutines.launch

@Composable
fun OptionsMenu(
    startGame: MutableState<Boolean>,
    player: MutableState<Player>,
    banks: MutableState<List<Bank>>,
    news: MutableState<List<News>>,
    logs: MutableState<List<Logs>>,
    yearlySummary: MutableState<List<YearlySummary>>,
    saveSlot: MutableState<Int>,
    stocks: MutableState<List<Stock>>,
    date: MutableState<Date>,
    perks: MutableState<List<Perk>>,
    dataStore: DataStore<Preferences>,
    paused: MutableState<Boolean>,
    achievements: MutableState<Achievements>,
    orderForExecute: MutableState<List<MarketOrder>>
) {
    val coroutine = rememberCoroutineScope()
    val showAlert = remember { mutableStateOf(false) }
    if (showAlert.value) {
        LeaveBeforeSavingAlert(
            showAlert,
            onConfirm = {
                val saveGame = SaveGame(
                    saveSlot.value,
                    stocks.value,
                    player.value,
                    date.value,
                    banks.value,
                    news.value,
                    logs.value,
                    yearlySummary.value,
                    perks.value,
                    orderForExecute.value,
                    achievements.value
                )
                coroutine.launch {
                    if (!paused.value) paused.value = true
                    saveGame(saveGame, dataStore, saveSlot.value)
                    startGame.value = false
                }
            },
            onDismiss = {
                startGame.value = false
            }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {

        }) {
            Text(text = "Save Game")
        }
        Button(onClick = {
            paused.value = true
            showAlert.value = true
        }) {
            Text(text = "Main Menu")
        }


    }


}