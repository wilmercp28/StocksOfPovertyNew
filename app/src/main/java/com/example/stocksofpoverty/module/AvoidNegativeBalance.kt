package com.example.stocksofpoverty.module

import androidx.compose.runtime.MutableState
import com.example.stocksofpoverty.data.Bank
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.Stock
import com.example.stocksofpoverty.data.getDateToString


fun avoidNegativeBalance(
    player: MutableState<Player>,
    banks: MutableState<List<Bank>>,
    logs: MutableState<List<Logs>>,
    stocks: MutableState<List<Stock>>,
    gameLost: MutableState<Boolean>,
    date: MutableState<Date>
) {
    fun checkIfBankAvoidLosing() {

        val bankCanPayAll =
            banks.value.find { it.loanBalance.value == 0.0 && it.creditLimit.value > -player.value.balance.value && it.tierNeeded <= player.value.tier.value }
        val bankWIthNoLoan = banks.value.find { it.loanBalance.value == 0.0 && it.tierNeeded <= player.value.tier.value }
        if (bankCanPayAll != null) {
            player.value.balance.value += bankCanPayAll.creditLimit.value
            bankCanPayAll.loanBalance.value = bankCanPayAll.creditLimit.value
            logs.value += Logs(
                getDateToString(date.value),
                "Force to Take at Loan to avoid bankrupt from ${bankCanPayAll.name} for ${bankCanPayAll.creditLimit.value}"
            )
        } else if (bankWIthNoLoan != null) {
            player.value.balance.value += bankWIthNoLoan.creditLimit.value
            bankWIthNoLoan.loanBalance.value = bankWIthNoLoan.creditLimit.value
            logs.value += Logs(
                getDateToString(date.value),
                "Force to Take at Loan to avoid bankrupt from ${bankWIthNoLoan.name} for ${bankWIthNoLoan.creditLimit.value}"
            )
            checkIfBankAvoidLosing()
        } else {
            gameLost.value = true
        }
    }

    fun checkIfSharesAvoidLosing() {
        val stocksWIthShares = stocks.value.filter { it.shares.value > 0 }
        if (stocksWIthShares.isNotEmpty()) {
            for (stock in stocksWIthShares) {
                val shareValue = stock.price.value * stock.shares.value
                if (shareValue >= -player.value.balance.value) {
                    val sharesToSell = ((-player.value.balance.value) / stock.price.value).toInt()
                    player.value.balance.value += sharesToSell * stock.price.value
                    stock.shares.value -= sharesToSell
                    logs.value += Logs(
                        getDateToString(date.value),
                        "Force to Sell $sharesToSell shares of ${stock.name} for ${sharesToSell * stock.price.value}"
                    )
                } else {
                    player.value.balance.value += shareValue
                    stock.shares.value = 0
                    logs.value += Logs(
                        getDateToString(date.value),
                        "Force to Sell ${stock.shares.value} shares of ${stock.name} for ${stock.shares.value * stock.price.value}"
                    )
                    checkIfSharesAvoidLosing()
                }
            }
        } else {
            gameLost.value = true
        }
    }

    val bankWIthNoLoan = banks.value.any { it.loanBalance.value == 0.0 }
    val stockWithShare = stocks.value.any { it.shares.value > 0 }
    if (bankWIthNoLoan) {
        checkIfBankAvoidLosing()
    } else if (stockWithShare) {
        checkIfSharesAvoidLosing()
    } else {
        gameLost.value = true
    }
}
