package ru.omgtu.abramov.domain

interface TaxonRepository {
    suspend fun getTaxons(): List<Taxon>
}