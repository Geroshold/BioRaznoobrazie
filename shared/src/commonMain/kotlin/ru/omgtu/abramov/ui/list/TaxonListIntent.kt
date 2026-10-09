package ru.omgtu.abramov.ui.list

sealed interface TaxonListIntent {
    data class CardClicked(val key: Int) : TaxonListIntent
}