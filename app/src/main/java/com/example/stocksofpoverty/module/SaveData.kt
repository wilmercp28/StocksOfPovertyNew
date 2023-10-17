package com.example.stocksofpoverty.module

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.stocksofpoverty.data.SaveGame
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

