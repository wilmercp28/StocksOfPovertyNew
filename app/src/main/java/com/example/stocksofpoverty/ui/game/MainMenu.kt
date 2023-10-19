package com.example.stocksofpoverty.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.stocksofpoverty.data.getInitialDate
import com.example.stocksofpoverty.data.getInitialPlayer
import com.example.stocksofpoverty.data.getInitialStockList
import java.text.DecimalFormat

@Composable
fun MainMenu(dataStore: DataStore<Preferences>) {
    val stocks = remember { mutableStateOf(getInitialStockList()) }
    val player = remember { mutableStateOf(getInitialPlayer()) }
    val date = remember { mutableStateOf(getInitialDate()) }
    val format = DecimalFormat("#.##")
    val devMode = true
    val saveSlot = remember { mutableStateOf(0) }

    StockMarketGame(stocks,dataStore,player,date,format,devMode,saveSlot)
}