package ru.omgtu.abramov.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.omgtu.abramov.resources.Res
import ru.omgtu.abramov.resources.taxon_number
import ru.omgtu.abramov.ui.model.RankUi
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaxonBaseStats(
    name: String,
    number: String,
    ranks: List<RankUi>,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = stringResource(Res.string.taxon_number, number),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
        )
        RankRow(ranks)
    }
}