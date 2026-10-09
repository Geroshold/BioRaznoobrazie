package ru.omgtu.abramov.ui.list

import ru.omgtu.abramov.ui.model.TaxonCardUi

data class TaxonListState (
    val items: List<TaxonCardUi> = emptyList(),
)