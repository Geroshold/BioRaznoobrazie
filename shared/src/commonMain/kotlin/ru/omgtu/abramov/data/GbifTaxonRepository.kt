package ru.omgtu.abramov.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.omgtu.abramov.core.suspendRunCatching
import ru.omgtu.abramov.data.network.GbifApi
import ru.omgtu.abramov.data.network.toDomain
import ru.omgtu.abramov.domain.Taxon
import ru.omgtu.abramov.domain.TaxonRepository

class GbifTaxonRepository(
    private val api: GbifApi,
    private val pageSize: Int = 50,
) : TaxonRepository {

    override suspend fun getTaxons(): List<Taxon> = withContext(Dispatchers.Default) {
        api.searchTaxons(query = null, offset = 0, limit = pageSize, rank = "KINGDOM")
            .map { page -> page.results.toDomain() }
            .getOrThrow()
    }

    override suspend fun getTaxon(key: Int): Taxon? = withContext(Dispatchers.Default) {
        api.getTaxonWithDescription(key)
            .map { it.toDomain() }
            .getOrNull()
    }

    override suspend fun getChildren(parentKey: Int): List<Taxon> = withContext(Dispatchers.Default) {
        api.getChildren(parentKey = parentKey, offset = 0, limit = pageSize)
            .map { page -> page.results.toDomain() }
            .getOrThrow()
    }
}