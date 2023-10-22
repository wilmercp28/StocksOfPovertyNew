package com.example.stocksofpoverty.ui.game

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stocksofpoverty.data.Bank
import com.example.stocksofpoverty.data.Perk
import com.example.stocksofpoverty.data.Player
import java.text.DecimalFormat


@Composable
fun PlayerUI(
    player: MutableState<Player>,
    selectedScreen: MutableState<String>,
    perkPoint: MutableState<Int>,
    perks: MutableState<List<Perk>>,
    tier: MutableState<Int>,
    banks: MutableState<List<Bank>>,
    format: DecimalFormat
) {
    val selectedPerk = remember { mutableStateOf(perks.value[0]) }
    val showAlert = remember { mutableStateOf(false) }
    val modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
    if (showAlert.value){
        PerkAlert(selectedPerk,showAlert, onConfirm = {
            perk ->
            showAlert.value = false
            perk.active.value = true
            perkPoint.value -= 1
        })
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Personal Finances", fontSize = 20.sp)
        if (perkPoint.value > 0) {
            Text(text = "Available perk points ${perkPoint.value}", color = Color.Yellow)
        }
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Perks", fontSize = 40.sp)
            Divider()

            PerkTierColumn(1,perks.value.filter { it.tier == 1 },selectedPerk,showAlert)
            PerkTierColumn(2, perks.value.filter { it.tier == 2 }, selectedPerk, showAlert)
            PerkTierColumn(3, perks.value.filter { it.tier == 3 }, selectedPerk, showAlert)
        }
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Record", fontSize = 40.sp)
            Divider()
            Text(text = "Total profit")
            Text(text = player.value.totalProfit.value.toString())
            Text(text = "Total paid taxes")
            Text(text = player.value.totalPaidTaxes.value.toString())
        }
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyRow(
                content = {
                    item { ResumeUI(player,format,perks) }
                }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun PerkTierColumn(
    tier: Int,
    perks: List<Perk>,
    selectedPerk: MutableState<Perk>,
    showAlert: MutableState<Boolean>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Tier $tier")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            perks.forEach { perk ->
                ShowPerk(perk,selectedPerk,showAlert)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ShowPerk(perk: Perk, selectedPerk: MutableState<Perk>, showAlert: MutableState<Boolean>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable {
                if (!perk.active.value) {
                    selectedPerk.value = perk
                    showAlert.value = true
                }
            }
    ) {
        Text(text = perk.name, textAlign = TextAlign.Center, fontSize = 10.sp)
        Icon(
            painterResource(perk.icon),
            tint = if (perk.active.value) Color.Yellow else Color.Gray,
            contentDescription = perk.name,
            modifier = Modifier.size(50.dp)
        )
    }
}

@Composable
fun ResumeUI(player: MutableState<Player>, format: DecimalFormat, perks: MutableState<List<Perk>>) {
    val incomeTaxPerkActivated = perks.value.firstOrNull { it.name == "Income Tax" && it.active.value }
    Column {
        Text(text = "This Year")
        Divider()
        if (incomeTaxPerkActivated != null) {
            if (incomeTaxPerkActivated.active.value)
                Text(text = "Expected IncomeTax (-5%) ${format.format(player.value.expectedIncomeTax.value)}")
        } else {
            Text(text = "Expected IncomeTax ${format.format(player.value.expectedIncomeTax.value)}")
        }
    }


}