package ru.omgtu.abramov.ui.screensList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.omgtu.abramov.list.TaxonListIntent
import ru.omgtu.abramov.list.TaxonListState

@Composable
fun TaxonListScreen(
    state: TaxonListState,
    onIntent: (TaxonListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(state.items, key = { it.key }) { taxon ->
            TaxonCard(
                taxon = taxon,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onIntent(TaxonListIntent.CardClicked(taxon.key)) },
            )
        }
    }
}