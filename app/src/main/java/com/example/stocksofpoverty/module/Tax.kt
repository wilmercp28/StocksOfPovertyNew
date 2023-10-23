package com.example.stocksofpoverty.module

import androidx.compose.runtime.MutableState
import com.example.stocksofpoverty.data.Bank
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Perk
import com.example.stocksofpoverty.data.Player

fun taxAndInterest(
    player: MutableState<Player>,
    banks: MutableState<List<Bank>>,
    date: MutableState<Date>,
    perks: MutableState<List<Perk>>
) {
    val incomeTaxPerkActivated = perks.value.firstOrNull { it.name == "Income Tax" && it.active }
    val income = player.value.yearProfit.value
    var taxRate = player.value.taxRate.value * 0.01
    if (incomeTaxPerkActivated != null) {
        if (incomeTaxPerkActivated.active){
            taxRate -= 0.05
        }
    }
    val amountToPay = income * taxRate
    player.value.expectedIncomeTax.value = amountToPay
    if (date.value.day.value == 1 && date.value.month.value == 1 && date.value.year.value != 1) {
        player.value.totalPaidTaxes.value += amountToPay
        player.value.totalPaidTaxes.value += amountToPay
        player.value.balance.value -= amountToPay
        player.value.yearProfit.value = 0.0
    }
}