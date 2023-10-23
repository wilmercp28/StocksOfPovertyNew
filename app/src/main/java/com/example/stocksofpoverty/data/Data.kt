package com.example.stocksofpoverty.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.example.stocksofpoverty.R
import kotlin.random.Random

data class Stock(
    val name: String,
    val symbol: String,
    val price: MutableState<Double>,
    var demand: Double,
    var supply: Double,
    val shares: MutableState<Int>,
    val averageBuyPrice: MutableState<Double>
)

fun getInitialStockList(
): List<Stock> {
    val listOfNames = getStockNameList()
    val listOfStocks = mutableListOf<Stock>()
    for (stock in listOfNames) {
        val name = stock.substringBefore(":")
        val abbreviation = stock.substringAfter(":")
        val randomPrice = Random.nextDouble(100.0, 1000.0)
        val randomSupply = Random.nextDouble(100.00) + 100
        val randomDemand = Random.nextDouble(100.00) + 100

        val stock =
            Stock(
                name,
                abbreviation,
                mutableStateOf(randomPrice),
                randomDemand,
                randomSupply,
                mutableStateOf(5),
                mutableStateOf(0.0)
            )
        listOfStocks += stock
    }
    return listOfStocks
}

data class SaveGame(
    val saveSlot: Int,
    val stock: List<Stock>,
    val player: Player,
    val date: Date,
    val bank: List<Bank>,
    val news: List<News>,
    val logs: List<Logs>,
    val tier: MutableState<Int>,
    val yearlySummary: List<YearlySummary>,
    val perk: List<Perk>
)

data class Date(
    val day: MutableState<Int>,
    val month: MutableState<Int>,
    val year: MutableState<Int>
)

fun getInitialDate(): Date {
    return Date(
        mutableStateOf(1),
        mutableStateOf(1),
        mutableStateOf(1)
    )
}

data class Player(
    val name: String,
    val balance: MutableState<Double>,
    val yearProfit: MutableState<Double>,
    val totalProfit: MutableState<Double>,
    val totalPaidTaxes: MutableState<Double>,
    val taxRate: MutableState<Double>,
    val expectedIncomeTax: MutableState<Double>,
    val totalDebt: MutableState<Double>,
    val totalInterestPaid: MutableState<Double>,
    val yearlyDebt: MutableState<Double>,
    val yearlyInterestPaid: MutableState<Double>
)

fun getInitialPlayer(): Player {
    return Player(
        "Player",
        mutableStateOf(10000.00),//Balance
        mutableStateOf(0.0),
        mutableStateOf(0.0),
        mutableStateOf(0.0),
        mutableStateOf(5.0),//IncomeTax
        mutableStateOf(0.0),
        mutableStateOf(0.0),
        mutableStateOf(0.0),
        mutableStateOf(0.0),
        mutableStateOf(0.0)
    )
}

data class Logs(
    val date: Date,
    val log: String
)

fun getInitialLog(): List<Logs> {
    return emptyList<Logs>()
}

data class Bank(
    val name: String,
    val slogan: String,
    val loanBalance: MutableState<Double>,
    val interestRate: Double,
    val creditLimit: MutableState<Double>
)

fun getInitialBanks(): List<Bank> {
    val bankNames = listOf(
        Pair("Easy Bank", "Simplifying Your Finances"),
        Pair("Secure Bank", "Your Safety, Our Priority"),
        Pair("Elite Bank", "Banking for the Elite"),
        Pair("Smart Bank", "Innovative Banking Solutions"),
        Pair("Global Bank", "Connecting the World's Finances")
    )
    val banks = mutableListOf<Bank>()
    for (i in bankNames.indices) {
        val (name, slogan) = bankNames[i]
        val initialCreditLimit = 5000 + i * 2000
        val initialInterestRate = 0.02 + i * 0.005
        banks.add(
            Bank(
                name = name,
                slogan = slogan,
                loanBalance = mutableStateOf(0.0),
                interestRate = initialInterestRate,
                creditLimit = mutableStateOf(initialCreditLimit.toDouble())
            )
        )
    }
    return banks
}

data class News(
    val title: String,
    val message: String,
    val date: Date,
    val unread: MutableState<Boolean> = mutableStateOf(true),
    val changesInDemand: List<Double>
)

fun getInitialNewsList(): List<News> {
    return emptyList<News>()
}

data class Perk(
    val name: String,
    val description: String,
    var active: Boolean,
    val tier: Int,
    val icon: Int,
)

fun getInitialPerks(): List<Perk> {
    val incomeTaxDescription = buildAnnotatedString {
        append("Reduces the interest rates on your ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Yellow)) {
            append("IncomeTax")
        }
        append(" loans by 5%.")
    }
    val bankInterestDescription = buildAnnotatedString {
        append("Reduces the interest rates on your ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Yellow)) {
            append("Bank loans")
        }
        append(" by 5%.")
    }
    return listOf(
        Perk(
            name = "Income Tax",
            description = incomeTaxDescription.text,
            active = false,
            1,
            R.drawable.ratedown
        ),
        Perk(
            name = "Bank interest",
            description = bankInterestDescription.text,
            active = false,
            1,
            R.drawable.ratedown
        )
    )
}

data class YearlySummary(
    val date: String,
    val balance: Double,
    val debt: Double,
    val interestPaid: Double,
    val profit: Double,
    val incomeTax: Double
)

fun getIInitialYearlySummary(): List<YearlySummary> {
    return emptyList<YearlySummary>()
}