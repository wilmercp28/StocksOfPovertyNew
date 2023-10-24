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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.Perk
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.YearlySummary
import com.example.stocksofpoverty.data.getDateToString
import java.text.DecimalFormat


@Composable
fun PlayerUI(
    player: MutableState<Player>,
    selectedScreen: MutableState<String>,
    perkPoint: MutableState<Int>,
    perks: MutableState<List<Perk>>,
    tier: MutableState<Int>,
    banks: MutableState<List<Bank>>,
    format: DecimalFormat,
    yearlySummary: MutableState<List<YearlySummary>>,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>,
    devMode: Boolean
) {
    val selectedPerk = remember { mutableStateOf(perks.value[0]) }
    val showAlert = remember { mutableStateOf(false) }
    val modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
    if (showAlert.value) {
        PerkAlert(selectedPerk, showAlert, onConfirm = { perk ->
            showAlert.value = false
            perk.active = true
            val log = Logs(getDateToString(date.value),"${perk.name} perk has been activated")
            logs.value += log
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
        if (devMode){
            Button(onClick = { tier.value += 1 }) {
                Text(text = "Tier Up")
            }
        }
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Perks", fontSize = 40.sp)
            Divider()

            PerkTierColumn(1, perks.value.filter { it.tier == 1 }, selectedPerk, showAlert)
            PerkTierColumn(2, perks.value.filter { it.tier == 2 }, selectedPerk, showAlert)
            PerkTierColumn(3, perks.value.filter { it.tier == 3 }, selectedPerk, showAlert)
        }
        Records(player, modifier, format)
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyRow(
                content = {
                    item { ResumeUI(player, format, perks, date, yearlySummary) }
                    if (yearlySummary.value.isNotEmpty()) {
                        items(yearlySummary.value.reversed()) {
                            YearlySummaryUI(player,format,it)
                        }
                    }
                }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun Records(player: MutableState<Player>, modifier: Modifier, format: DecimalFormat) {
    Column(
        modifier = modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Record", fontSize = 40.sp)
        Divider()
        Row() {
            Text(text = "Total profit")
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Total paid taxes")
        }
        Row() {
            Text(text = format.format(player.value.totalProfit.value))
            Spacer(modifier = Modifier.weight(1f))
            Text(text = format.format(player.value.totalPaidTaxes.value))
        }
        Row() {
            Text(text = "Total Debt")
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Total Debt")
        }
        Row() {
            Text(text = format.format(player.value.totalInterestPaid.value))
            Spacer(modifier = Modifier.weight(1f))
            Text(text = format.format(player.value.totalInterestPaid.value))
        }
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
                ShowPerk(perk, selectedPerk, showAlert)
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
                if (!perk.active) {
                    selectedPerk.value = perk
                    showAlert.value = true
                }
            }
    ) {
        Text(text = perk.name, textAlign = TextAlign.Center, fontSize = 10.sp)
        Icon(
            painterResource(perk.icon),
            tint = if (perk.active) Color.Yellow else Color.Gray,
            contentDescription = perk.name,
            modifier = Modifier.size(50.dp)
        )
    }
}

@Composable
fun ResumeUI(
    player: MutableState<Player>,
    format: DecimalFormat,
    perks: MutableState<List<Perk>>,
    date: MutableState<Date>,
    yearlySummary: MutableState<List<YearlySummary>>
) {
    val incomeTaxPerkActivated =
        perks.value.firstOrNull { it.name == "Income Tax" && it.active }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "This Year")
        Divider()
        if (incomeTaxPerkActivated != null) {
            if (incomeTaxPerkActivated.active)
                Text(text = "Expected IncomeTax (-5%) ${format.format(player.value.expectedIncomeTax.value)}")
        } else {
            Text(text = "Expected IncomeTax ${format.format(player.value.expectedIncomeTax.value)}")
        }
        if (player.value.yearProfit.value >= 0) {
            Text(
                text = "Profit ${format.format(player.value.yearProfit.value)}",
                color = Color.Green
            )
        } else {
            Text(text = "Loses ${format.format(player.value.yearProfit.value)}", color = Color.Red)
        }
        Text(text = "Total Debt ${format.format(player.value.yearlyDebt.value)}")
        Text(text = "Interest paid ${format.format(player.value.yearlyInterestPaid.value)}")
    }
}

@Composable
fun YearlySummaryUI(
    player: MutableState<Player>,
    format: DecimalFormat,
    yearlySummary: YearlySummary,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = yearlySummary.date)
        Divider()
        Text(text = "IncomeTax ${format.format(yearlySummary.incomeTax)}")
        if (player.value.yearProfit.value >= 0) {
            Text(
                text = "Profit ${format.format(yearlySummary.profit)}",
                color = Color.Green
            )
        } else {
            Text(text = "Loses ${format.format(yearlySummary.profit)}", color = Color.Red)
        }
        Text(text = "Total Debt ${format.format(yearlySummary.debt)}")
        Text(text = "Interest paid ${format.format(yearlySummary.interestPaid)}")
    }
}