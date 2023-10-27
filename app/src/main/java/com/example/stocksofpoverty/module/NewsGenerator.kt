package com.example.stocksofpoverty.module

import androidx.compose.runtime.MutableState
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.News
import com.example.stocksofpoverty.data.Stock
import com.example.stocksofpoverty.data.getDateToString
import com.example.stocksofpoverty.data.getNewsEnergyArticles
import com.example.stocksofpoverty.data.getNewsFinanceArticles
import com.example.stocksofpoverty.data.getNewsGenericArticles
import com.example.stocksofpoverty.data.getNewsPharmaArticles
import com.example.stocksofpoverty.data.getNewsTechArticles
import kotlin.random.Random

fun generateNews(news: MutableState<List<News>>, date: MutableState<Date>, stock: Stock) {
    val articles = when (stock.category) {
        "Tech" -> getNewsTechArticles(stock)
        "Pharma" -> getNewsPharmaArticles(stock)
        "Energy" -> getNewsEnergyArticles(stock)
        "Finance" -> getNewsFinanceArticles(stock)
        else -> getNewsGenericArticles()
    }
    val randomIndex = Random.nextInt(articles.size)
    val randomArticle = articles[randomIndex]
    val demandChanges = randomArticle.second

    val newsArticle = News(
        title = randomArticle.first.substringBefore(":"),
        message = randomArticle.first.substringAfter(":"),
        stockName = stock.name,
        date = getDateToString(date.value)
    )
    news.value += newsArticle
    stock.demandChangesList.addAll(demandChanges)
    stock.inEvent = true
}
