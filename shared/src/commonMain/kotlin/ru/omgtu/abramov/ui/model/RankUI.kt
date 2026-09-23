package ru.omgtu.abramov.ui.model

import androidx.compose.ui.graphics.Color
import ru.omgtu.abramov.domain.Rank

data class RankUi(val name: String, val color: Color)

fun Rank.toUi(): RankUi = RankUi(name = displayRankName(this), color = rankColor(this))

internal fun displayRankName(rank: Rank): String = when (rank) {
    Rank.KINGDOM -> "Kingdom"
    Rank.PHYLUM -> "Phylum"
    Rank.CLASS -> "Class"
    Rank.ORDER -> "Order"
    Rank.FAMILY -> "Family"
    Rank.GENUS -> "Genus"
    Rank.SPECIES -> "Species"
}

private fun rankColor(rank: Rank): Color = when (rank) {
    Rank.KINGDOM -> Color(0xFF6A1B9A)
    Rank.PHYLUM -> Color(0xFF4527A0)
    Rank.CLASS -> Color(0xFF283593)
    Rank.ORDER -> Color(0xFF1565C0)
    Rank.FAMILY -> Color(0xFF00838F)
    Rank.GENUS -> Color(0xFF2E7D32)
    Rank.SPECIES -> Color(0xFFEF6C00)
}