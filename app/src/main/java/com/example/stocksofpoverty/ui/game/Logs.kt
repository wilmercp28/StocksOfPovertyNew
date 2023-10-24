package com.example.stocksofpoverty.ui.game

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.stocksofpoverty.data.Date
import com.example.stocksofpoverty.data.Logs

@Composable
fun LogsUI(logs: MutableState<List<Logs>>, date: MutableState<Date>) {

    LazyColumn(
        content = {
            items(logs.value) { log ->
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = log.log, textAlign = TextAlign.Center)
                    Text(text = log.date, textAlign = TextAlign.Center)
                }
            }
        }
    )
}