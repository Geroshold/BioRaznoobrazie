package ru.omgtu.abramov.ui.detail

sealed interface TaxonDetailIntent {
    data object OpenChildren : TaxonDetailIntent
    data object Retry : TaxonDetailIntent
}