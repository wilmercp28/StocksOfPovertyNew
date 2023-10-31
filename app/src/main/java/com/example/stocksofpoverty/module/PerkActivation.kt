package com.example.stocksofpoverty.module

import androidx.compose.runtime.MutableState
import com.example.stocksofpoverty.data.Bank
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.News
import com.example.stocksofpoverty.data.Perk
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.Stock
import com.example.stocksofpoverty.data.getDateToString
import kotlin.random.Random


fun activatePerk(
    perk: Perk,
    showAlert: MutableState<Boolean>,
    logs: MutableState<List<Logs>>,
    date: MutableState<Date>,
    player: MutableState<Player>,
    news: MutableState<List<News>>,
    banks: MutableState<List<Bank>>,
    stocks: MutableState<List<Stock>>
) {
    val log = Logs(getDateToString(date.value), "${perk.name} perk has been activated")
    logs.value += log
    when (perk.name) {
        "Income Tax" -> activatePerkIncometax(player)
        "Bank interest" -> activatePerkBankInterest(banks)
        "Gift from a stranger" -> activatePerkGiftFromAStranger(stocks,logs,date)
    }
    player.value.perkPoints.value--
    perk.active = true
    showAlert.value = false
}

fun activatePerkGiftFromAStranger(
    stocks: MutableState<List<Stock>>,
    logs: MutableState<List<Logs>>,
    date: MutableState<Date>
) {
    val selectedIndices = mutableSetOf<Int>()
    while (selectedIndices.size < 2) {
        val randomIndex = Random.nextInt(stocks.value.size)
        if (randomIndex !in selectedIndices) {
            stocks.value[randomIndex].shares.value += 100
            logs.value += Logs(getDateToString(date.value),"Receive 100 shares of ${stocks.value[randomIndex].name}")
            selectedIndices.add(randomIndex)
        }
    }
}

fun activatePerkIncometax(player: MutableState<Player>) {
    player.value.taxRate.value -= 5.0
}

fun activatePerkBankInterest(banks: MutableState<List<Bank>>) {
    for (bank in banks.value) {
        bank.interestRate -= 5.0
    }
}