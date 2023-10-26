package com.example.stocksofpoverty.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import com.example.stocksofpoverty.R
import com.example.stocksofpoverty.data.Bank
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs
import com.example.stocksofpoverty.data.Player
import com.example.stocksofpoverty.module.getLoan
import com.example.stocksofpoverty.module.payoffLoan
import java.text.DecimalFormat

@Composable
fun BanksUI(
    player: MutableState<Player>,
    banks: MutableState<List<Bank>>,
    date: MutableState<Date>,
    logs: MutableState<List<Logs>>,
    format: DecimalFormat,
    tier: MutableState<Int>
) {
    val isShowingBank = remember { mutableStateOf(false) }
    val showingBank = remember { mutableStateOf(banks.value[0]) }
    if (isShowingBank.value) {
        ShowBank(showingBank, isShowingBank,player,logs,date)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(painterResource(R.drawable.loan), contentDescription = "Loans Icon")
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center,
                content = {
                    items(banks.value) { bank ->
                        BankList(bank, tier, format, isShowingBank, showingBank,player)
                    }
                }
            )
        }
    }
}

@Composable
fun BankList(
    bank: Bank,
    tier: MutableState<Int>,
    format: DecimalFormat,
    isShowingBank: MutableState<Boolean>,
    showingBank: MutableState<Bank>,
    player: MutableState<Player>
) {
    val boxColor =
        if (bank.tierNeeded <= tier.value) MaterialTheme.colorScheme.primary else Color.Gray
    Column(
        modifier = Modifier
            .size(200.dp)
            .clickable {
                showingBank.value = bank
                isShowingBank.value = true
            }
            .padding(10.dp)
            .background(
                boxColor,
                RoundedCornerShape(10.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (bank.tierNeeded <= tier.value) {
            if (bank.loanBalance.value == 0.0) {
                Text(text = bank.name, textAlign = TextAlign.Center, fontSize = 20.sp)
                Text(text = "Credit limit ${bank.creditLimit.value}", textAlign = TextAlign.Center)
                Text(text = "Interest Rate ${bank.interestRate}%", textAlign = TextAlign.Center)
            } else {
                val interest = bank.interestRate / 100
                val amountToPay = bank.loanBalance.value * interest
                val paymentsNumber = bank.loanBalance.value / 200
                Text(
                    text = "Debt ${format.format(bank.loanBalance.value)}",
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Principal (200) Interest (${format.format(amountToPay)})",
                    textAlign = TextAlign.Center
                )
                Text(text = "PaymentsLeft ${paymentsNumber.toInt()}", textAlign = TextAlign.Center)
            }
        } else {
            val tierText =
                if (bank.tierNeeded == 1) "I" else if (bank.tierNeeded == 2) "II" else "III"
            Text(text = "Need\nTier", textAlign = TextAlign.Center)
            Text(text = tierText)
        }
    }
}

@Composable
fun ShowBank(
    bank: MutableState<Bank>,
    isShowingBank: MutableState<Boolean>,
    player: MutableState<Player>,
    logs: MutableState<List<Logs>>,
    date: MutableState<Date>
) {
    val interestRate = bank.value.interestRate / 100
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { isShowingBank.value = false }) {
            Text(text = "Back")
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = bank.value.name,
            fontSize = 50.sp,
            textAlign = TextAlign.Center,
            softWrap = true
        )
        Text(text = bank.value.slogan)
        if (bank.value.loanBalance.value == 0.0) {
            Text(text = "Credit Limit ${bank.value.creditLimit.value}")
        } else {
            Text(text = "Loan Balance ${bank.value.loanBalance.value}")
        }
        Text(text = "Interest Rate ${bank.value.interestRate}%")
        if (bank.value.loanBalance.value == 0.0) {
            Text(text = "Loan Term ${(bank.value.loanBalance.value / 200).toInt()} Months")
        } else {
            Text(text = "Monthly payments ${(bank.value.loanBalance.value * interestRate) + 200}")
            Text(text = "${(bank.value.loanBalance.value / 200).toInt()} Months until payoff")
        }
        if (bank.value.loanBalance.value == 0.0) {
            Button(onClick = {
                getLoan(bank,player,logs,date)
            }) {
                Text(text = "Take loan")
            }
        } else {
            Button(onClick = {
                payoffLoan(bank,player)
            }) {
                Text(text = "Payoff")
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}
