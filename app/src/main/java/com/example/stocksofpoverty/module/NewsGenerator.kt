package com.example.stocksofpoverty.module

import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.News
import com.example.stocksofpoverty.data.Stock
import com.example.stocksofpoverty.data.getDateToString
import kotlin.random.Random

fun generateNews(news: MutableState<List<News>>, date: MutableState<Date>, stock: Stock) {
    val articles = getNewsArticles()
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
fun getNewsArticles(): List<Pair<String, List<Int>>> {
    return listOf(
        Pair(
            "Tech Innovators Inc. Launches AI-Powered Smart Home Device" +
                    ":Tech Innovators Inc. has introduced a state-of-the-art AI-powered smart home device. Industry analysts predict a surge in demand as consumers eagerly adopt this innovative technology.",
            listOf(25, 15, 25, -15, -25)
        ),
        Pair(
            "Global Energy Ventures Announces Breakthrough in Renewable Energy" +
                    ":Global Energy Ventures has made a significant breakthrough in renewable energy research. This development is expected to drive up demand for clean energy solutions worldwide.",
            listOf(30, 20, 30, -20, -30)
        ),
        Pair(
            "Pioneer Biotech Corporation Receives FDA Approval for New Drug" +
                    ":Pioneer Biotech Corporation has received FDA approval for their groundbreaking new pharmaceutical drug. The market responds positively, indicating a boost in demand for healthcare stocks.",
            listOf(15, 25, 15, -25, -15)
        ),
        Pair(
            "Smart Investments Group Expands into Emerging Markets" +
                    ":Smart Investments Group has announced its expansion into emerging markets. Investors are optimistic, expecting increased demand for stocks in these regions.",
            listOf(20, 30, 20, -30, -20)
        ),
        Pair(
            "Data Dynamics Launches Cloud Computing Platform" +
                    ":Data Dynamics has launched a cutting-edge cloud computing platform. Businesses are quick to adopt this technology, leading to a surge in demand for cloud-based services.",
            listOf(25, 15, 25, -15, -25)
        ),
        Pair(
            "Tech Innovators Inc. Faces Legal Challenges" +
                    ":Tech Innovators Inc. is currently embroiled in a legal battle, which has led to investor concerns. The uncertainty in the market has caused a drop in demand for the company's stocks. However, the company's efforts to address the issues have led to a gradual recovery.",
            listOf(-10, -5, 0, 5, 10, 15)
        ),
        Pair(
            "Global Energy Ventures Reports Decline in Revenue" +
                    ":Global Energy Ventures has reported a decline in quarterly revenue due to market fluctuations and decreased consumer demand. Investors react with caution, leading to a moderate decrease in demand for the company's stocks. The company's strategic plans for recovery have started to show positive signs.",
            listOf(-15, -10, -5, 0, 5, 10)
        ),
        Pair(
            "Pioneer Biotech Corporation Faces Product Recall" +
                    ":Pioneer Biotech Corporation has issued a product recall for one of its pharmaceutical drugs due to safety concerns. This news has affected investor confidence, resulting in a decrease in demand for the company's stocks. The company's transparent communication and efforts for product replacement have eased concerns, leading to a gradual recovery in demand.",
            listOf(-20, -15, -10, -5, 0, 5)
        ),
        Pair(
            "Smart Investments Group Posts Quarterly Losses" +
                    ":Smart Investments Group has reported losses in its latest quarterly financial statement. Investors respond by selling off shares, causing a decline in demand for the company's stocks. However, the company's cost-cutting measures and future growth plans have restored investor confidence, resulting in a partial recovery in demand.",
            listOf(-10, -8, -5, -3, 0, 8)
        ),
        Pair(
            "Data Dynamics Faces Security Breach" +
                    ":Data Dynamics has experienced a security breach, compromising sensitive customer data. The breach has eroded investor trust, resulting in a decrease in demand for the company's cloud services. Data Dynamics' enhanced security measures and proactive approach have started to rebuild trust, leading to a gradual recovery in demand.",
            listOf(-18, -14, -10, -6, -2, 6)
        )
    )
}