package com.example.stocksofpoverty.module

import androidx.compose.runtime.MutableState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.SaveGame
import com.example.stocksofpoverty.data.Stock
import com.example.stocksofpoverty.data.getInitialDate
import com.example.stocksofpoverty.data.getInitialPlayer
import com.example.stocksofpoverty.data.getInitialStockList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
fun startNewGame(
    saveSlot: MutableState<Int>,
    player: MutableState<Player>,
    date: MutableState<Date>,
    startGame: MutableState<Boolean>,
    stocks: MutableState<List<Stock>>,
    dataStore: DataStore<Preferences>
) {
    player.value = getInitialPlayer()
    date.value = getInitialDate()
    stocks.value = getInitialStockList()
    GlobalScope.launch {
        saveSlot.value = getEmptySaveSlot(dataStore)
        val saveGame = SaveGame(saveSlot.value,stocks.value,player.value,date.value)
        saveGame(saveGame,dataStore,saveSlot.value)
        startGame.value = true

    }
}