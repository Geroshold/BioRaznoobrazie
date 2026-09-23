package ru.omgtu.abramov.list

import ru.omgtu.abramov.ui.model.TaxonCardUi

data class TaxonListState (
    val items: List<TaxonCardUi> = emptyList(),
)