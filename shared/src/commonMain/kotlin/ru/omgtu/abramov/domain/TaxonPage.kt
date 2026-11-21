package ru.omgtu.abramov.domain

data class TaxonPage(
    val items: List<Taxon>,
    val endOfRecords: Boolean
)