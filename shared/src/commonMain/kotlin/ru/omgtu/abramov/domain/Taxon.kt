package ru.omgtu.abramov.domain

data class Taxon(
    val key: Int,
    val scientificName: String,
    val canonicalName: String?,
    val rank: Rank,
    val parentKey: Int?,
    val numDescendants: Int,
    val kingdom: String?,
    val family: String?,
    val genus: String?
)

enum class Rank {
    KINGDOM, PHYLUM, CLASS, ORDER, FAMILY, GENUS, SPECIES;

    companion object {
        fun of(value: String): Rank =
            entries.firstOrNull { it.name == value } ?: SPECIES
    }
}