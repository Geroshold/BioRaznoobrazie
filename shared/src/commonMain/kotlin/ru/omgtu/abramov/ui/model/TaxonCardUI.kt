package ru.omgtu.abramov.ui.model

import ru.omgtu.abramov.domain.Taxon
import ru.omgtu.abramov.domain.TaxonRepository
import ru.omgtu.abramov.domain.getTaxons
import ru.omgtu.abramov.utils.displayTaxonName
import ru.omgtu.abramov.utils.displayTaxonNumber

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

suspend fun TaxonRepository.getCards(filter: String?): List<TaxonCardUi> =
    getTaxons(filter).toCardsUi()