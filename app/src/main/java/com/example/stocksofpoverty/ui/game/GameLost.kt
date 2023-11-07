package com.example.stocksofpoverty.ui.game

import android.app.Activity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.YearlySummary
import com.example.stocksofpoverty.data.formatMoneyValue

@Composable
fun GameLostMenu(
    player: MutableState<Player>,
    gameLost: MutableState<Boolean>,
    startGame: MutableState<Boolean>,
    yearlySummary: MutableState<List<YearlySummary>>
) {
    val activity = LocalContext.current as Activity
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Game Over", fontSize = 40.sp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
        ) {
            Text(text = "Balance ${formatMoneyValue(player.value.balance.value)}")
            Text(text = "Profit record ${formatMoneyValue(player.value.totalProfit.value)}")
            Text(text = "Paid taxes record ${formatMoneyValue(player.value.totalPaidTaxes.value)}")
        }
        LazyRow(
            content = {
                if (yearlySummary.value.isNotEmpty()) {
                    items(yearlySummary.value.reversed()) {
                        YearlySummaryUI(player,it)
                    }
                }
            }
        )
        Button(onClick = { 
            gameLost.value = false
            startGame.value = false
        }) {
            Text(text = "Main Menu")
        }
        Button(onClick = {
            activity.finish()
        }) {
            Text(text = "Quit")
        }
    }
}