package com.example.stocksofpoverty.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.stocksofpoverty.data.getInitialStockList

@Composable
fun MainMenu(dataStore: DataStore<Preferences>) {
    val stocks = remember { mutableStateOf(getInitialStockList()) }

    StockMarketGame(stocks,dataStore)
}