package com.example.stocksofpoverty.module

import androidx.compose.runtime.MutableState
import com.example.stocksofpoverty.data.Bank
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.Perk
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.Stock
import com.example.stocksofpoverty.data.getDateToString
import java.text.DecimalFormat
import kotlin.math.abs

fun taxAndInterest(
    player: MutableState<Player>,
    banks: MutableState<List<Bank>>,
    date: MutableState<Date>,
    perks: MutableState<List<Perk>>,
    logs: MutableState<List<Logs>>,
    format: DecimalFormat,
    stocks: MutableState<List<Stock>>
) {

    if (date.value.day.value == 1 && perks.value[4].active) {
        player.value.yearProfit.value += 100
        player.value.balance.value += 100
        logs.value += Logs(getDateToString(date.value), "Part time PAYDAY, got $100")
    }
    val incomeTaxPerkActivated = perks.value.firstOrNull { it.name == "Income Tax" && it.active }
    val income = player.value.yearProfit.value
    var taxRate = player.value.taxRate.value * 0.01
    if (incomeTaxPerkActivated != null) {
        if (incomeTaxPerkActivated.active) {
            taxRate -= 0.05
        }
    }
    val amountToPayTax = income * taxRate
    player.value.expectedIncomeTax.value = amountToPayTax
    if (date.value.day.value == 1 && date.value.month.value == 1 && date.value.year.value != 1) {
        player.value.totalPaidTaxes.value += amountToPayTax
        player.value.totalPaidTaxes.value += amountToPayTax
        player.value.balance.value -= amountToPayTax
        val logIncomeTax =
            Logs(getDateToString(date.value), "${format.format(amountToPayTax)} paid on income tax")
        logs.value += logIncomeTax
        if (player.value.balance.value > 0 && perks.value[2].active) {
            val amountToPayLateFeeds = abs(player.value.balance.value) * 0.10
            player.value.balance.value -= amountToPayLateFeeds
            logs.value += Logs(
                getDateToString(date.value),
                "Paid late fees for $amountToPayLateFeeds "
            )
        }
    }
    if (date.value.day.value == 1 && date.value.month.value == 1) {
        val cashbackActive = perks.value.firstOrNull { it.name == "Cash Back" && it.active }
        if (cashbackActive != null) {
            if (cashbackActive.active) {
                var amountCashBack = player.value.yearlySpend.value * 0.1
                if (amountCashBack > 500) amountCashBack = 500.0
                player.value.balance.value += amountCashBack
                logs.value += Logs(
                    getDateToString(date.value),
                    "Receive CashBack for ${format.format(amountCashBack)}"
                )
            }
        }
        if (perks.value[6].active) {
            val stockWIth100ShareOrMore = stocks.value.filter { it.shares.value >= 100 }
            var totalDividends = 0.0
            for (stock in stockWIth100ShareOrMore) {
                val dividends = (stock.price.value * stock.shares.value) * 0.05
                totalDividends += dividends
                logs.value += Logs(getDateToString(date.value),"Dividends from ${stock.name} for ${format.format(dividends)}")
            }
            player.value.balance.value += totalDividends
        }
        player.value.yearlySpend.value = 0.0
        player.value.yearProfit.value = 0.0
    }
    for (bank in banks.value) {
        if (bank.loanBalance.value != 0.0 && bank.dayToPayInterest == date.value.day.value) {
            val interest = (bank.interestRate / 100) * bank.loanBalance.value
            val principal = 200
            val amountToPay = interest + principal
            player.value.balance.value -= amountToPay
            bank.loanBalance.value -= principal
        }
    }
}