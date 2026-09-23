package ru.omgtu.abramov.list

sealed interface TaxonListIntent {
    data class CardClicked(val key: Int) : TaxonListIntent
}