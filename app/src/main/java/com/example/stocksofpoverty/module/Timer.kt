package com.example.stocksofpoverty.module

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.delay

@Composable
fun Update(
    update: () -> Unit
) {
    LaunchedEffect(Unit) {
        while (true) {
            update()
            delay(100)
        }
    }
}