package com.example.stocksofpoverty.ui.game

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.stocksofpoverty.data.SaveGame
import com.example.stocksofpoverty.data.Stock
import com.example.stocksofpoverty.module.getSaveGame
import com.example.stocksofpoverty.module.saveGame
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockMarketGame(stocks: MutableState<List<Stock>>, dataStore: DataStore<Preferences>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Hola")
                }
            )
        }
    ) {
        LazyColumn {
            it
            items(stocks.value) { stock ->
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ShowStock(stock)
                }
            }
        }
    }
}

@Composable
fun ShowStock(stock: Stock) {
    val format = DecimalFormat("#.##")
    val expanded = remember { mutableStateOf(false) }
    val priceBoxColor = remember { mutableStateOf(Color.Green) }
    Column(
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
            .padding(10.dp),
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    expanded.value = !expanded.value
                },
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = stock.name, fontSize = 20.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = format.format(stock.price.value), fontSize = 20.sp,
                modifier = Modifier
                    .background(priceBoxColor.value, RoundedCornerShape(10.dp))
                    .padding(5.dp)
            )
        }
        AnimatedVisibility(expanded.value) {
            Column(
                modifier = Modifier
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { /*TODO*/ }){
                        Text(text = "Buy")
                    }
                    Button(onClick = { /*TODO*/ }){
                        Text(text = "Sell")
                    }

                }

            }

        }
    }
}