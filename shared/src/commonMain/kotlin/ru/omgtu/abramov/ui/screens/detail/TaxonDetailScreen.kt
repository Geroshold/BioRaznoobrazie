package ru.omgtu.abramov.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.omgtu.abramov.detail.TaxonDetailIntent
import ru.omgtu.abramov.resources.Res
import ru.omgtu.abramov.resources.section_children
import ru.omgtu.abramov.ui.components.TaxonBaseStats
import ru.omgtu.abramov.ui.components.TaxonChildrenButton
import ru.omgtu.abramov.ui.components.TaxonDescription
import ru.omgtu.abramov.ui.model.TaxonDetailUi
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaxonDetailScreen(
    taxon: TaxonDetailUi,
    onIntent: (TaxonDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TaxonBaseStats(
            name = taxon.name,
            number = taxon.number,
            ranks = taxon.ranks,
        )

        taxon.description?.let { TaxonDescription(it) }

        if (taxon.hasChildren) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                TaxonChildrenButton(
                    label = stringResource(Res.string.section_children),
                    onClick = { onIntent(TaxonDetailIntent.OpenChildren) },
                )
            }
        }
    }
}