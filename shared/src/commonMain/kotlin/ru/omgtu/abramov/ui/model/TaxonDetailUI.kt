package ru.omgtu.abramov.ui.model

import ru.omgtu.abramov.domain.Taxon
import ru.omgtu.abramov.utils.displayTaxonName
import ru.omgtu.abramov.utils.displayTaxonNumber

data class TaxonDetailUi(
    val key: Int,
    val name: String,
    val number: String,
    val ranks: List<RankUi>,
    val description: String?,
    val hasChildren: Boolean,
)

fun Taxon.toDetailUi(): TaxonDetailUi = TaxonDetailUi(
    key = key,
    name = displayTaxonName(scientificName),
    number = displayTaxonNumber(key),
    ranks = listOf(rank.toUi()),
    description = description?.trim()?.takeIf { it.isNotEmpty() },
    hasChildren = numDescendants > 0,
)