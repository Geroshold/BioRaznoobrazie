package ru.omgtu.abramov.ui.model

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.StringResource
import ru.omgtu.abramov.domain.Rank
import ru.omgtu.abramov.resources.Res
import ru.omgtu.abramov.resources.rank_class
import ru.omgtu.abramov.resources.rank_family
import ru.omgtu.abramov.resources.rank_genus
import ru.omgtu.abramov.resources.rank_kingdom
import ru.omgtu.abramov.resources.rank_order
import ru.omgtu.abramov.resources.rank_phylum
import ru.omgtu.abramov.resources.rank_species

data class RankUi(val labelRes: StringResource, val color: Color)

fun Rank.toUi(): RankUi = RankUi(
    labelRes = when (this) {
        Rank.KINGDOM -> Res.string.rank_kingdom
        Rank.PHYLUM -> Res.string.rank_phylum
        Rank.CLASS -> Res.string.rank_class
        Rank.ORDER -> Res.string.rank_order
        Rank.FAMILY -> Res.string.rank_family
        Rank.GENUS -> Res.string.rank_genus
        Rank.SPECIES -> Res.string.rank_species
    },
    color = rankColor(this),
)

private fun rankColor(rank: Rank): Color = when (rank) {
    Rank.KINGDOM -> Color(0xFF6A1B9A)
    Rank.PHYLUM -> Color(0xFF4527A0)
    Rank.CLASS -> Color(0xFF283593)
    Rank.ORDER -> Color(0xFF1565C0)
    Rank.FAMILY -> Color(0xFF00838F)
    Rank.GENUS -> Color(0xFF2E7D32)
    Rank.SPECIES -> Color(0xFFEF6C00)
}