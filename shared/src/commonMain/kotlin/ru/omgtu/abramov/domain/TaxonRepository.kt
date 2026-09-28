package ru.omgtu.abramov.domain

interface TaxonRepository {
    suspend fun getTaxons(): List<Taxon>
    suspend fun getTaxon(key: Int): Taxon?
    suspend fun getChildren(parentKey: Int): List<Taxon>
}