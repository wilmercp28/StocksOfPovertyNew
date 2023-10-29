package com.example.stocksofpoverty.module

import androidx.compose.runtime.MutableState
import com.example.stocksofpoverty.data.Bank
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.News
import com.example.stocksofpoverty.data.Perk
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.getDateToString


fun activatePerk(
    perk: Perk,
    showAlert: MutableState<Boolean>,
    logs: MutableState<List<Logs>>,
    date: MutableState<Date>,
    player: MutableState<Player>,
    news: MutableState<List<News>>,
    banks: MutableState<List<Bank>>
) {
    val log = Logs(getDateToString(date.value), "${perk.name} perk has been activated")
    logs.value += log
    when (perk.name) {
        "Income Tax" -> activatePerkIncometax(player)
        "Bank interest" -> activatePerkBankInterest(banks)
    }
    player.value.perkPoints.value--
    perk.active = true
    showAlert.value = false
}
fun activatePerkIncometax(player: MutableState<Player>) {
    player.value.taxRate.value -= 5.0
}

fun activatePerkBankInterest(banks: MutableState<List<Bank>>) {
    for (bank in banks.value){
     bank.interestRate -= 5.0
    }
}