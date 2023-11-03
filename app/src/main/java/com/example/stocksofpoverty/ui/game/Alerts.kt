package com.example.stocksofpoverty.ui.game

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.example.stocksofpoverty.data.Perk
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
            Button(onClick = { onConfirm() }) {
                Text(text = "Yes")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text(text = "No")
            }
        }
    )
}

@Composable
fun PopupUI(popupList: MutableState<List<String>>) {
    val coroutineScope = rememberCoroutineScope()

    if (popupList.value.isNotEmpty()) {
        val currentPopupList = popupList.value
        LaunchedEffect(currentPopupList) {
            delay(5000) // Wait for 5 seconds
            if (currentPopupList.isNotEmpty()) {
                popupList.value = currentPopupList.drop(1)
            }
        }

        Popup(
            alignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = currentPopupList[0], fontSize = 20.sp)
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp))
            }
        }
    }
}
