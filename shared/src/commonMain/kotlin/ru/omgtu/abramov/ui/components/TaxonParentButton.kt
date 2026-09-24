package ru.omgtu.abramov.ui.components

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun TaxonParentButton(label: String, onClick: () -> Unit) {
    TextButton(onClick = onClick) { Text(label) }
}