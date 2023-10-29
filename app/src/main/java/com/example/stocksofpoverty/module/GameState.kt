package com.example.stocksofpoverty.module

import androidx.compose.runtime.MutableState
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
import com.example.stocksofpoverty.data.YearlySummary
import com.example.stocksofpoverty.data.getInitialBanks
import com.example.stocksofpoverty.data.getInitialDate
import com.example.stocksofpoverty.data.getInitialLog
import com.example.stocksofpoverty.data.getInitialNewsList
import com.example.stocksofpoverty.data.getInitialPerks
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
    dataStore: DataStore<Preferences>,
    perks: MutableState<List<Perk>>,
    banks: MutableState<List<Bank>>,
    news: MutableState<List<News>>,
    logs: MutableState<List<Logs>>,
    yearlySummary: MutableState<List<YearlySummary>>
) {
    player.value = getInitialPlayer()
    date.value = getInitialDate()
    stocks.value = getInitialStockList()
    perks.value = getInitialPerks()
    banks.value = getInitialBanks()
    news.value = getInitialNewsList()
    logs.value = getInitialLog()
    GlobalScope.launch {
        saveSlot.value = getEmptySaveSlot(dataStore)
        val saveGame = SaveGame(
            saveSlot.value,
            stocks.value,
            player.value,
            date.value,
            banks.value,
            news.value,
            logs.value,
            yearlySummary.value.toList(),
            perks.value.toList()
        )
        saveGame(saveGame, dataStore, saveSlot.value)
        startGame.value = true

    }
}