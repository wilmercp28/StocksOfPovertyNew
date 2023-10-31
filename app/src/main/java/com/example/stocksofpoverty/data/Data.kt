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
    var name: String,
    val balance: MutableState<Double>,
    val yearProfit: MutableState<Double>,
    val totalProfit: MutableState<Double>,
    val totalPaidTaxes: MutableState<Double>,
    val taxRate: MutableState<Double>,
    val expectedIncomeTax: MutableState<Double>,
    val totalDebt: MutableState<Double>,
    val totalInterestPaid: MutableState<Double>,
    val yearlySpend: MutableState<Double>,
    val yearlyDebt: MutableState<Double>,
    val yearlyInterestPaid: MutableState<Double>,
    var tier: MutableState<Int>,
    var perkPoints: MutableState<Int>,
)
data class Achievements(
    var currentIndex: Int,
    var advanceTierProfitRequirements: Pair<List<Double>, MutableState<Boolean>>,
    var advanceTierBalanceRequirements: Pair<List<Double>, MutableState<Boolean>>

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
        mutableStateOf(0.0),
        mutableStateOf(0.0),
        mutableStateOf(0),
        mutableStateOf(0),
    )
}
fun getInitialAchievements(): Achievements {
    return Achievements(
        currentIndex = 0,
        advanceTierBalanceRequirements = Pair(
            listOf(10000.0, 30000.0, 60000.0, 0.0),
            mutableStateOf(false)
        ),
        advanceTierProfitRequirements = Pair(
            listOf(20000.0, 40000.0, 70000.0, 0.0),
            mutableStateOf(false)
        )
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
    var interestRate: Double,
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
            name = "Hidden\nBank Account",
            description = "Receive a one time sum of money for 10000",
            append = listOf("one","time","10000"),
            active = false,
            1,
            R.drawable.cashperk
        ),
        Perk(
            name = "Cash Back",
            description = "Get 1% cash back for all your stocks buys (Maximum 500)",
            append = listOf("1%","cash","back","500"),
            active = false,
            1,
            R.drawable.cashbackperk
        ),
        Perk(
            name = "Super overdraft",
            description = "Can go until -20000 without going bankrupt, but has to pay 4% on late fees monthly",
            append = listOf("-20000","bankrupt","10%","fees","monthly"),
            active = false,
            1,
            R.drawable.card
        ),
        Perk(
            name = "Income Tax",
            description = "Reduces the interest rates on your income tax by 5%.",
            append = listOf("income tax", "5%"),
            active = false,
            2,
            R.drawable.ratedown
        ),
        Perk(
            name = "Part time\njob",
            description = "Work a part time job, get $100 monthly",
            append = listOf("$100","monthly"),
            active = false,
            2,
            R.drawable.parttimejobperk
        ),
        Perk(
            name = "Bank interest",
            description = "Reduces the interest rate on your bank loans by 5%.",
            append = listOf("interest rate", "bank loans", "5%"),
            active = false,
            2,
            R.drawable.ratedown
        ),
        Perk(
            name = "Dividends",
            description = "For each 100 shares, you receive dividends for 0.05% of the stock price",
            append = listOf("100","shares","dividends","0.05","price"),
            active = false,
            3,
            R.drawable.ratedown
        ),
        Perk(
            name = "Gift from a stranger",
            description = "An unknown person send you some shares, 100 to be precise, from 3 different stocks (From a random stock)",
            append = listOf("100","random","3","random"),
            active = false,
            3,
            R.drawable.anonymity
        ), Perk(
            name = "empty",
            description = "For each 100 shares, you receive dividends for 0.05% of the stock price",
            append = listOf("100","shares","dividends","0.05","price"),
            active = false,
            3,
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
    val incomeTax: Double,
    val spend: Double
)

fun getIInitialYearlySummary(): List<YearlySummary> {
    return emptyList()
}