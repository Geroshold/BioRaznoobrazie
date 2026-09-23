package ru.omgtu.abramov.ui.model

import ru.omgtu.abramov.domain.Taxon

data class TaxonCardUi(
    val key: Int,
    val name: String,
    val number: String,
    val ranks: List<RankUi>,
)

fun Taxon.toCardUi(): TaxonCardUi = TaxonCardUi(
    key = key,
    name = displayTaxonName(scientificName),
    number = displayTaxonNumber(key),
    ranks = listOf(rank.toUi()),
)

fun List<Taxon>.toCardsUi(): List<TaxonCardUi> = map { it.toCardUi() }