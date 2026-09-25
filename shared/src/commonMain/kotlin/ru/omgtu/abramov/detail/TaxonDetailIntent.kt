package ru.omgtu.abramov.detail

sealed interface TaxonDetailIntent {
    data object OpenChildren : TaxonDetailIntent
}