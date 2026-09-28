package ru.omgtu.abramov.ui.screens.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.omgtu.abramov.ui.components.CardSurface
import ru.omgtu.abramov.ui.components.TaxonBaseStats
import ru.omgtu.abramov.ui.model.TaxonCardUi

@Composable
fun TaxonCard(taxon: TaxonCardUi, modifier: Modifier = Modifier) {
    CardSurface(modifier) {
        TaxonBaseStats(
            name = taxon.name,
            number = taxon.number,
            ranks = taxon.ranks,
        )
    }
}