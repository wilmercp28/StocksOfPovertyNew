package com.example.stocksofpoverty.module

import androidx.compose.runtime.MutableState
import com.example.stocksofpoverty.data.Achievements
import com.example.stocksofpoverty.data.Player
import java.lang.IndexOutOfBoundsException

fun tierCompleted(
    player: MutableState<Player>,
    indexForGoals: MutableState<Int>,
    achievements: MutableState<Achievements>
) {
    try {
        indexForGoals.value++
        player.value.perkPoints.value++
        player.value.tier.value++
        allAchievementsFalse(achievements)
    } catch (e: IndexOutOfBoundsException) {

    }
}

fun checkAllAchievements(achievements: MutableState<Achievements>): Boolean {
    val profitRequirements = achievements.value.advanceTierProfitRequirements.second.value
    val balanceRequirements = achievements.value.advanceTierBalanceRequirements.second.value

    return profitRequirements && balanceRequirements
}

fun allAchievementsFalse(achievements: MutableState<Achievements>) {
    achievements.value.advanceTierBalanceRequirements.second.value = false
    achievements.value.advanceTierProfitRequirements.second.value = false
}