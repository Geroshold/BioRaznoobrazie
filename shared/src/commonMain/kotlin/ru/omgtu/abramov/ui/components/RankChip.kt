package ru.omgtu.abramov.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import ru.omgtu.abramov.ui.model.RankUi

@Composable
fun RankChip(rank: RankUi, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = rank.color,
        contentColor = Color.White,
        shape = MaterialTheme.shapes.small,
    ) {
        Text(
            text = stringResource(rank.labelRes),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
        )
    }
}