package ru.omgtu.abramov.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TaxonName(name: String) {
    Text(name, style = MaterialTheme.typography.headlineMedium)
}