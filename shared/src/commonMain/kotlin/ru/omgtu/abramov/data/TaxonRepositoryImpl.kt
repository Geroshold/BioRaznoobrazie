package ru.omgtu.abramov.data

import ru.omgtu.abramov.domain.TaxonRepository
import ru.omgtu.abramov.domain.Taxon

class TaxonRepositoryImpl : TaxonRepository {
    override suspend fun getTaxons(): List<Taxon> = mockTaxons

    override suspend fun getTaxon(key: Int): Taxon = mockTaxons.first { it.key == key }
    override suspend fun getChildren(parentKey: Int): List<Taxon> = mockTaxons.filter { it.parentKey == parentKey }
}