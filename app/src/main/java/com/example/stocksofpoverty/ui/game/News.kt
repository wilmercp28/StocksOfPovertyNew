package com.example.stocksofpoverty.ui.game

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stocksofpoverty.R
import com.example.stocksofpoverty.data.News

@Composable
fun NewsUI(news: MutableState<List<News>>) {
    val newsIcon = painterResource(R.drawable.newsuiicon)
    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(newsIcon, contentDescription = "NewsIcon")
        LazyColumn(content = {
            items(news.value.reversed()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)),
                ) {
                    ShowNews(it)
                }
            }
        }
        )

    }
}

@Composable
fun ShowNews(news: News) {

    Column(
        Modifier
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = annotateRecursively(listOf(news.stockName),news.title),fontSize = 20.sp, textAlign = TextAlign.Center)
        Divider()
        Text(text = annotateRecursively(listOf(news.stockName),news.message),fontSize = 15.sp, textAlign = TextAlign.Center)
        Divider()
        Text(text = news.date,fontSize = 20.sp, textAlign = TextAlign.Center)
    }
}