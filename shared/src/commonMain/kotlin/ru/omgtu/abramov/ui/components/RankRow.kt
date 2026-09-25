package ru.omgtu.abramov.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import ru.omgtu.abramov.ui.model.RankUi

@Composable
fun RankRow(ranks: List<RankUi>) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        ranks.forEach { rank ->
            RankChip(rank = rank)
        }
    }
}