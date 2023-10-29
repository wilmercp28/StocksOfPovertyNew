package com.example.stocksofpoverty.ui.game

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.stocksofpoverty.data.News
import com.example.stocksofpoverty.data.Perk
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.YearlySummary
import com.example.stocksofpoverty.data.formatNumberToK
import com.example.stocksofpoverty.module.activatePerk
import com.example.stocksofpoverty.module.tierCompleted
import java.text.DecimalFormat


@Composable
fun PlayerUI(
    player: MutableState<Player>,
    perks: MutableState<List<Perk>>,
    format: DecimalFormat,
    yearlySummary: MutableState<List<YearlySummary>>,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>,
    devMode: Boolean,
    banks: MutableState<List<Bank>>,
    news: MutableState<List<News>>
) {
    val selectedPerk = remember { mutableStateOf(perks.value[0]) }
    val showAlert = remember { mutableStateOf(false) }
    val modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
    if (showAlert.value) {
        PerkAlert(selectedPerk, showAlert, onConfirm = { perk ->
            activatePerk(selectedPerk.value, showAlert, logs, date, player, news, banks)
        })
    }
    val balanceAchievementReach = remember { mutableStateOf(false) }
    val profitAchievementReach = remember { mutableStateOf(false) }
    var indexForGoals = remember { mutableStateOf(0) }
    if (balanceAchievementReach.value && profitAchievementReach.value) {
        tierCompleted(balanceAchievementReach, profitAchievementReach, player)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Tier", fontSize = 30.sp)
                Text(
                    text = when (player.value.tier.value) {
                        0 -> "0"
                        1 -> "I"
                        2 -> "II"
                        3 -> "III"
                        else -> ""
                    }, fontSize = 20.sp
                )
                ProgressIndicatorLinear(
                    "Balance",
                    player.value.balance.value,
                    player.value.advanceTierBalanceRequirements,
                    balanceAchievementReach,
                    indexForGoals
                )
            }
        }
        item {
            Text(text = "Personal Finances", fontSize = 20.sp)
            if (player.value.perkPoints.value > 0) {
                Text(
                    text = "Available perk points ${player.value.perkPoints}",
                    color = Color.Yellow
                )
            }
            if (devMode) {
                Button(onClick = { player.value.tier.value += 1 }) {
                    Text(text = "Tier Up")
                }
                Button(onClick = { player.value.balance.value += 1000 }) {
                    Text(text = "Money Up")
                }
            }
        }
        item {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Perks", fontSize = 40.sp)
                Divider()
                Text(text = "Tier I")
                PerkTierColumn(1, perks.value.filter { it.tier == 1 }, selectedPerk, showAlert)
                Divider()
                Text(text = "Tier II")
                PerkTierColumn(2, perks.value.filter { it.tier == 2 }, selectedPerk, showAlert)
                Divider()
                Text(text = "Tier III")
                PerkTierColumn(3, perks.value.filter { it.tier == 3 }, selectedPerk, showAlert)
            }
        }
        item {
            Records(player, modifier, format)
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyRow(
                    content = {
                        item { ResumeUI(player, format, perks) }
                        if (yearlySummary.value.isNotEmpty()) {
                            items(yearlySummary.value.reversed()) {
                                YearlySummaryUI(player, format, it)
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ProgressIndicatorLinear(
    title: String,
    currentAmount: Double,
    goals: List<Double>,
    completedAchievement: MutableState<Boolean>,
    index: MutableState<Int>
) {
    if (goals[index.value] == 0.0) {
        Text(text = "All achievements for $title Completed")
    } else
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            val progress = if (index.value == 0) {
                (currentAmount / goals[index.value]).coerceIn(0.0, 1.0)
            } else {
                ((currentAmount - goals[index.value - 1]) / (goals[index.value] - goals[index.value - 1])).coerceIn(
                    0.0,
                    1.0
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {

                Text(text = title)
                Spacer(modifier = Modifier.weight(1f))
                LinearProgressIndicator(progress = progress.toFloat())
                Spacer(modifier = Modifier.weight(1f))
                Text(formatNumberToK(goals[index.value]))
            }
            Box() {
                if (progress >= 1) {
                    Text(text = "Completed")
                    completedAchievement.value = true
                }
            }
        }
}

@Composable
fun PerkTierColumn(
    tier: Int,
    listOfPerk: List<Perk>,
    selectedPerk: MutableState<Perk>,
    showAlert: MutableState<Boolean>
) {
    val isAnyPerkActive: Boolean = listOfPerk.any { it.active }
    Row(
        modifier = Modifier
    ) {
        if (!isAnyPerkActive) {
            for (perks in listOfPerk) {
                showPerk(perks, selectedPerk, showAlert)
            }
        } else {
            val activePerk = listOfPerk.filter { it.active }
            for (perk in activePerk) {
                showPerk(perk, selectedPerk, showAlert)
            }
        }
    }
}

@Composable
fun showPerk(perks: Perk, selectedPerk: MutableState<Perk>, showAlert: MutableState<Boolean>) {
    val icon = painterResource(perks.icon)
    Column(
        modifier = Modifier
            .padding(5.dp)
            .clickable {
                if (!perks.active) {
                    selectedPerk.value = perks
                    showAlert.value = true
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon, perks.name, tint = if (perks.active) Color.Yellow else Color.Gray,
            modifier = Modifier
                .size(50.dp)
        )
        Text(text = perks.name, textAlign = TextAlign.Center, modifier = Modifier.padding(10.dp))
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
        Row {
            Text(text = "Total profit")
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Total paid taxes")
        }
        Row {
            Text(text = format.format(player.value.totalProfit.value))
            Spacer(modifier = Modifier.weight(1f))
            Text(text = format.format(player.value.totalPaidTaxes.value))
        }
        Row {
            Text(text = "Total Debt")
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Total Debt")
        }
        Row {
            Text(text = format.format(player.value.totalInterestPaid.value))
            Spacer(modifier = Modifier.weight(1f))
            Text(text = format.format(player.value.totalInterestPaid.value))
        }
    }
}

@Composable
fun ResumeUI(
    player: MutableState<Player>,
    format: DecimalFormat,
    perks: MutableState<List<Perk>>,
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