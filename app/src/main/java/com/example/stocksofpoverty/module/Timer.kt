package com.example.stocksofpoverty.module

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.delay

@Composable
fun Update(
    paused: MutableState<Boolean>,
    update: () -> Unit
) {
    LaunchedEffect(Unit) {
        while (!paused.value) {
            update()
            delay(200)
        }
    }
}