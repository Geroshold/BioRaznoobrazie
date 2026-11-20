package ru.omgtu.abramov.data.network

import ru.omgtu.abramov.data.network.dto.TaxonDto
import ru.omgtu.abramov.domain.Rank
import ru.omgtu.abramov.domain.Taxon

fun TaxonDto.toDomain(): Taxon? {
    val k = key ?: return null
    val name = scientificName ?: return null
    return Taxon(
        key = k,
        scientificName = name,
        canonicalName = canonicalName,
        rank = rank.toDomainRank(),
        parentKey = parentKey,
        numDescendants = numDescendants,
        kingdom = kingdom,
        phylum = phylum,
        description = descriptions
            ?.firstOrNull { !it.description.isNullOrBlank() }
            ?.description
            ?.trim(),
    )
}

private fun String?.toDomainRank(): Rank = when (this?.uppercase()) {
    "KINGDOM" -> Rank.KINGDOM
    "PHYLUM", "SUBPHYLUM", "SUPERPHYLUM" -> Rank.PHYLUM
    "CLASS", "SUBCLASS", "INFRACLASS", "SUPERCLASS" -> Rank.CLASS
    "ORDER", "SUBORDER", "INFRAORDER", "SUPERORDER" -> Rank.ORDER
    "FAMILY", "SUBFAMILY", "TRIBE", "SUBTRIBE" -> Rank.FAMILY
    "GENUS", "SUBGENUS", "SECTION", "SUBSECTION" -> Rank.GENUS
    "SPECIES", "SUBSPECIES", "VARIETY", "FORM", "CULTIVAR" -> Rank.SPECIES
    else -> Rank.SPECIES
}

fun List<TaxonDto>.toDomain(): List<Taxon> = mapNotNull { it.toDomain() }