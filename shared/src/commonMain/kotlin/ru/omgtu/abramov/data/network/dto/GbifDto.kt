package ru.omgtu.abramov.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class TaxonPageDto(
    val count: Int = 0,
    val offset: Int = 0,
    val limit: Int = 0,
    val endOfRecords: Boolean = false,
    val results: List<TaxonDto> = emptyList(),
)

@Serializable
data class TaxonDto(
    val key: Int? = null,
    val scientificName: String? = null,
    val canonicalName: String? = null,
    val rank: String? = null,
    val parentKey: Int? = null,
    val numDescendants: Int = 0,
    val kingdom: String? = null,
    val phylum: String? = null,
    val descriptions: List<TaxonDescriptionDto>? = null,
)

@Serializable
data class TaxonDescriptionDto(
    val description: String?,
)
