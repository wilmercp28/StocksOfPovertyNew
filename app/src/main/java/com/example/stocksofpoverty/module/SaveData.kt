package com.example.stocksofpoverty.module

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.stocksofpoverty.data.Achievements
import com.example.stocksofpoverty.data.Bank
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.News
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.SaveGame
import com.example.stocksofpoverty.data.Stock
import com.example.stocksofpoverty.data.createDynamicGson
import kotlinx.coroutines.flow.firstOrNull


val gson = createDynamicGson()

suspend fun saveGame(
    saveGameData: SaveGame,
    dataStore: DataStore<Preferences>,
    saveGameName: Int
) {
    val jsonString = gson.toJson(saveGameData)
    val key = stringPreferencesKey("save_game_data_$saveGameName")
    dataStore.edit { preferences ->
        preferences[key] = jsonString
    }
    Log.d("GameSaved", "Savegame, Slot $saveGameName")
}
suspend fun getSaveGame(
    dataStore: DataStore<Preferences>,
    saveGameName: Int
): SaveGame? {
    val key = stringPreferencesKey("save_game_data_$saveGameName")
    val jsonString = dataStore.data.firstOrNull()?.get(key)
    return if (jsonString != null) {
        gson.fromJson(jsonString, SaveGame::class.java)
    } else {
        null
    }
}
suspend fun removeSaveGame(
    dataStore: DataStore<Preferences>,
    saveGameName: Int
):Boolean {
    val key = stringPreferencesKey("save_game_data_$saveGameName")
     dataStore.edit { preferences ->
        preferences.remove(key)
    }
    return true
}
suspend fun getAllSaveGames(dataStore: DataStore<Preferences>): List<SaveGame> {
    val saveGames = mutableListOf<SaveGame>()
    for (slot in 0 until 20) {
        val saveGame = getSaveGame(dataStore, slot)
        if (saveGame != null) {
            saveGames.add(saveGame)
        }
    }
    return saveGames
}


suspend fun getEmptySaveSlot(dataStore: DataStore<Preferences>): Int {
    for (slot in 0 until 20) {
        val saveGame = getSaveGame(dataStore, slot)
        if (saveGame == null) {
            return slot
        }
    }
    return -1
}
fun loadSave(
    saveGame: SaveGame,
    stocks: MutableState<List<Stock>>,
    player: MutableState<Player>,
    date: MutableState<Date>,
    saveSlot: MutableState<Int>,
    startGame: MutableState<Boolean>,
    loadingGame: MutableState<Boolean>,
    logs: MutableState<List<Logs>>,
    banks: MutableState<List<Bank>>,
    news: MutableState<List<News>>,
    achievements: MutableState<Achievements>,
    gameLost: MutableState<Boolean>,
) {
    stocks.value = saveGame.stock
    player.value = saveGame.player
    date.value = saveGame.date
    saveSlot.value = saveGame.saveSlot
    logs.value = saveGame.logs
    banks.value = saveGame.bank
    news.value = saveGame.news

    loadingGame.value = false
    startGame.value = true
}

