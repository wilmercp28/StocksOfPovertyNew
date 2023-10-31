package com.example.stocksofpoverty.module

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun Update(
    update: () -> Unit
) {
    LaunchedEffect(Unit) {
        while (true) {
            update()
            delay(400)
        }
    }
}