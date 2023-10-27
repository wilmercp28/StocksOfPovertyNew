package com.example.stocksofpoverty.data

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.stocksofpoverty.R
import kotlin.random.Random

data class Stock(
    val name: String,
    val symbol: String,
    val category: String,
    val price: MutableState<Double>,
    val lastYearPrice: MutableState<Double>,
    val percentageChange: MutableState<Double>,
    var demand: Double,
    var supply: Double,
    val shares: MutableState<Int>,
    val averageBuyPrice: MutableState<Double>,
    var inEvent: Boolean = false,
    var demandChangesList: MutableList<Int> = mutableListOf()
)

fun getInitialStockList(
): List<Stock> {
    val listOfNames = getStockNameList()
    val listOfStocks = mutableListOf<Stock>()
    for (stock in listOfNames) {
        val name = stock.substringBefore(":")
        val abbreviation = stock.substringAfter(":").substringBefore(":")
        val category = stock.substringAfterLast(":")
        val randomPrice = Random.nextDouble(100.0, 1000.0)
        val randomSupply = Random.nextDouble(100.00) + 100
        val randomDemand = Random.nextDouble(100.00) + 100

        val stockObject =
            Stock(
                name,
                abbreviation,
                category,
                mutableStateOf(randomPrice),
                mutableStateOf(randomPrice),
                mutableStateOf(0.0),
                randomDemand,
                randomSupply,
                mutableStateOf(0),
                mutableStateOf(0.0)
            )
        listOfStocks += stockObject
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

fun getDateToString(date: Date): String {
    return "Day ${date.day.value} Month ${date.month.value} Year ${date.year.value}"
}

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
    val date: String,
    val log: String
)

fun getInitialLog(): List<Logs> {
    return emptyList()
}

data class Bank(
    val name: String,
    val slogan: String,
    val loanBalance: MutableState<Double>,
    val interestRate: Double,
    val creditLimit: MutableState<Double>,
    val tierNeeded: Int,
    var dayToPayInterest: Int
)

fun getInitialBanks(): List<Bank> {
    val bankNames = listOf(
        Pair("Easy Bank", "Simplifying Your Finances"),
        Pair("Secure Bank", "Your Safety, Our Priority"),
        Pair("Elite Bank", "Banking for the Elite"),
        Pair("Smart Bank", "Innovative Banking Solutions"),
        Pair("Global Bank", "Connecting the World's Finances"),
        Pair("Special Bank", "Your Special Banking Experience")
    )
    val banks = mutableListOf<Bank>()
    for (i in bankNames.indices) {
        val (name, slogan) = bankNames[i]
        val initialCreditLimit = 5000 + i * 5000
        val initialInterestRate = 5 + i * 2
        banks.add(
            Bank(
                name = name,
                slogan = slogan,
                loanBalance = mutableStateOf(0.0),
                interestRate = initialInterestRate.toDouble(),
                creditLimit = mutableStateOf(initialCreditLimit.toDouble()),
                tierNeeded = when (i) {
                    0 -> 0
                    1 -> 1
                    2 -> 1
                    3 -> 2
                    4 -> 2
                    5 -> 3
                    else -> {
                        0
                    }
                },
                dayToPayInterest = 1
            )
        )
    }
    Log.d("Banks", banks.toString())
    return banks
}

data class News(
    val title: String,
    val message: String,
    val stockName: String,
    val date: String,
    val unread: MutableState<Boolean> = mutableStateOf(true),
    var changesInDemand: List<Double> = emptyList()
)

fun getInitialNewsList(): List<News> {
    return emptyList()
}

data class Perk(
    val name: String,
    val description: String,
    val append: List<String>,
    var active: Boolean,
    val tier: Int,
    val icon: Int,
)

fun getInitialPerks(): List<Perk> {

    return listOf(
        Perk(
            name = "Income Tax",
            description = "Reduces the interest rates on your income tax by 5%.",
            append = listOf("income tax","5%"),
            active = false,
            1,
            R.drawable.ratedown
        ),
        Perk(
            name = "Bank interest",
            description = "Reduces the interest rate on your bank loans by 5%.",
            append = listOf("interest rate","bank loans","5%"),
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
    return emptyList()
}