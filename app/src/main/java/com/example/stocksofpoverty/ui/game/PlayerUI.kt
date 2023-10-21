package com.example.stocksofpoverty.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.stocksofpoverty.data.Perk
import com.example.stocksofpoverty.data.Player

@Composable
fun PlayerUI(
    player: MutableState<Player>,
    selectedScreen: MutableState<String>,
    perkPoint: MutableState<Int>,
    perks: MutableState<List<Perk>>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "Personal Finances", fontSize = 20.sp)
        Spacer(modifier = Modifier.weight(1f))
        if (perkPoint.value > 0){
            Text(text = "Available perk points ${perkPoint.value}")
        }
    }
}