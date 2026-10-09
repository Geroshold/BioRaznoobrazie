package ru.omgtu.abramov.domain

interface TaxonRepository {
    suspend fun getTaxons(): List<Taxon>
    suspend fun getTaxon(key: Int): Taxon?
    suspend fun getChildren(parentKey: Int): List<Taxon>
}

suspend fun TaxonRepository.getTaxons(filter: String?): List<Taxon> =
    getTaxons().filterByName(filter)

fun List<Taxon>.filterByName(filter: String?): List<Taxon> {
    val needle = filter?.trim().orEmpty()
    if (needle.isEmpty()) return this
    return filter { it.scientificName.contains(needle, ignoreCase = true) }
}