package ru.omgtu.abramov.domain

data class Taxon(
    val key: Int,
    val scientificName: String,
    val canonicalName: String?,
    val rank: Rank,
    val parentKey: Int?,
    val numDescendants: Int,
    val kingdom: String?,
    val phylum: String?,
    val description: String?,
)

enum class Rank {
    KINGDOM,
    PHYLUM,
    CLASS,
    ORDER,
    FAMILY,
    GENUS,
    SPECIES
}