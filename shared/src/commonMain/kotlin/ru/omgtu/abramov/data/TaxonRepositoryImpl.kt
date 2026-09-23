package ru.omgtu.abramov.data

import ru.omgtu.abramov.domain.TaxonRepository
import ru.omgtu.abramov.domain.Taxon

class TaxonRepositoryImpl : TaxonRepository {
    override suspend fun getTaxons(): List<Taxon> = mockTaxons
}