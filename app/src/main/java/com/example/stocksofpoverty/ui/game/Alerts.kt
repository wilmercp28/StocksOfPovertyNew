package com.example.stocksofpoverty.ui.game

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.stocksofpoverty.data.Perk

@Composable
fun PerkAlert(
    perk: MutableState<Perk>,
    showAlert: MutableState<Boolean>,
    onConfirm: (Perk) -> Unit,
) {
    AlertDialog(
        onDismissRequest = { showAlert.value = false },
        title = { Text(text = perk.value.name, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())},
        text = { Text(text = perk.value.description, textAlign = TextAlign.Center)},
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(perk.value)
                }
            ) {
                Text(text = "Activate Perk")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    showAlert.value = false
                }
            ) {
                Text(text = "Cancel")
            }
        }
    )

}