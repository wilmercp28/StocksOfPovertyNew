package com.example.stocksofpoverty.module

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.stocksofpoverty.data.Bank
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.News
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.SaveGame
import com.example.stocksofpoverty.data.Stock
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import kotlinx.coroutines.flow.firstOrNull
import java.lang.reflect.Type


val gson: Gson = GsonBuilder()
    .registerTypeAdapter(MutableState::class.java, MutableStateDoubleDeserializer())
    .registerTypeAdapter(MutableState::class.java, MutableStateDoubleSerializer())
    .registerTypeAdapter(MutableState::class.java, MutableStateIntDeserializer())
    .registerTypeAdapter(MutableState::class.java, MutableStateIntSerializer())
    .create()

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
class MutableStateDoubleDeserializer : JsonDeserializer<MutableState<Double>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MutableState<Double> {
        return if (json != null && json.isJsonPrimitive && json.asJsonPrimitive.isNumber) {
            val value = json.asDouble
            mutableStateOf(value)
        } else {
            mutableStateOf(0.0)
        }
    }
}
class MutableStateDoubleSerializer : JsonSerializer<MutableState<Double>> {
    override fun serialize(
        src: MutableState<Double>?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.value ?: 0.0)
    }
}
class MutableStateIntDeserializer : JsonDeserializer<MutableState<Int>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MutableState<Int> {
        return if (json != null && json.isJsonPrimitive && json.asJsonPrimitive.isNumber) {
            val value = json.asInt
            mutableStateOf(value)
        } else {
            mutableStateOf(0)
        }
    }
}

class MutableStateIntSerializer : JsonSerializer<MutableState<Int>> {
    override fun serialize(
        src: MutableState<Int>?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.value ?: 0)
    }
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
    perkPoint: MutableState<Int>,
    tier: MutableState<Int>
) {
    stocks.value = saveGame.stock
    player.value = saveGame.player
    date.value = saveGame.date
    saveSlot.value = saveGame.saveSlot
    loadingGame.value = false
    startGame.value = true
}

