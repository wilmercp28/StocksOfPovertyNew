package com.example.stocksofpoverty.ui.game

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import com.example.stocksofpoverty.data.Perk

@Composable
fun annotateRecursively(
    placeHolderList: List<String>,
    originalText: String
): AnnotatedString {
    val builder = AnnotatedString.Builder(originalText)

    placeHolderList.forEach { placeHolder ->
        val startIndex = originalText.indexOf(placeHolder, ignoreCase = true)
        if (startIndex != -1) {
            val endIndex = startIndex + placeHolder.length
            builder.addStyle(
                style = SpanStyle(color = Color.Yellow),
                start = startIndex,
                end = endIndex
            )
        }
    }
    return builder.toAnnotatedString()
}

@Composable
fun PerkAlert(
    perk: MutableState<Perk>,
    showAlert: MutableState<Boolean>,
    onConfirm: (Perk) -> Unit,
) {

    AlertDialog(
        onDismissRequest = { showAlert.value = false },
        title = {
            Text(
                text = perk.value.name,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = annotateRecursively(perk.value.append, perk.value.description),
                textAlign = TextAlign.Center
            )
        },
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

@Composable
fun LeaveBeforeSavingAlert(
    showAlert: MutableState<Boolean>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            showAlert.value = false
        },
        title = {
            Text(text = "Save Before Leaving?")
        },
        confirmButton = {
            Button(onClick = {onConfirm()}) {
                Text(text = "Yes" )
            }
        },
        dismissButton = {
            Button(onClick = {onDismiss()}) {
                Text(text = "No")
            }
        }
    )
}