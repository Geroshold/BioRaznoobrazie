package ru.omgtu.abramov.ui.screens.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import ru.omgtu.abramov.ui.components.CardSurface
import ru.omgtu.abramov.ui.components.TaxonBaseStats
import ru.omgtu.abramov.ui.components.TaxonPlaceholder
import ru.omgtu.abramov.ui.model.TaxonCardUi

@Composable
fun TaxonCard(taxon: TaxonCardUi, modifier: Modifier = Modifier) {
    CardSurface(modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TaxonPlaceholder(
                name = taxon.name,
                bgColor = taxon.ranks.first().color,
                size = 56.dp,
            )
            Spacer(modifier = Modifier.width(16.dp))

            TaxonBaseStats(
                name = taxon.name,
                number = taxon.number,
                ranks = taxon.ranks,
            )
        }
    }
}
