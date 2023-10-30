package com.example.stocksofpoverty.module

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.stocksofpoverty.data.Bank
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.data.Stock


fun avoidNegativeBalance(
    player: MutableState<Player>,
    banks: MutableState<List<Bank>>,
    logs: MutableState<List<Logs>>,
    stocks: MutableState<List<Stock>>,
    gameLost: MutableState<Boolean>
) {
    val cantWin = mutableStateOf(false)
  while (player.value.balance.value < 0.0 || !cantWin.value){
      checkIfBankAvoidLosing(banks,player,cantWin)
  }
    if (cantWin.value){
        gameLost.value = true
    }
}

fun checkIfBankAvoidLosing(
    banks: MutableState<List<Bank>>,
    player: MutableState<Player>,
    cantWin: MutableState<Boolean>,
) {
    val bankCanPayAll = banks.value.find { it.loanBalance.value == 0.0 && it.creditLimit.value > -player.value.balance.value }
    val bankWIthNoLoan = banks.value.find { it.loanBalance.value == 0.0 }
    if (bankCanPayAll != null){
        player.value.balance.value += bankCanPayAll.creditLimit.value
        bankCanPayAll.loanBalance.value = bankCanPayAll.creditLimit.value
    } else if (bankWIthNoLoan != null) {
        player.value.balance.value += bankWIthNoLoan.creditLimit.value
        bankWIthNoLoan.loanBalance.value = bankWIthNoLoan.creditLimit.value
    } else {
        cantWin.value = true
    }
}

fun checkIfSharesAvoidLosing(
    stocks: MutableState<List<Stock>>,
    player: MutableState<Player>
) {
}