package ru.omgtu.abramov.detail

sealed interface TaxonDetailIntent {
    data object OpenParent : TaxonDetailIntent
    data object OpenChildren : TaxonDetailIntent
}