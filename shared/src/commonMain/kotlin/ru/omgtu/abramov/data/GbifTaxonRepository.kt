package ru.omgtu.abramov.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.omgtu.abramov.data.network.GbifApi
import ru.omgtu.abramov.data.network.toDomain
import ru.omgtu.abramov.domain.Taxon
import ru.omgtu.abramov.domain.TaxonPage
import ru.omgtu.abramov.domain.TaxonRepository
import ru.omgtu.abramov.utils.stripHtml

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
        val detailDto = api.getTaxon(key).getOrNull() ?: return@withContext null

        val rawDescription = api.getDescriptions(key)
            .getOrNull()
            ?.results
            ?.firstOrNull { !it.description.isNullOrBlank() }
            ?.description

        val cleanDescription = rawDescription?.stripHtml()

        detailDto.copy(description = cleanDescription).toDomain()
    }

    override suspend fun getTaxonsPage(offset: Int, limit: Int): TaxonPage =
        withContext(Dispatchers.Default) {
            val result = api.searchTaxons(query = null, offset = offset, limit = limit, rank = "KINGDOM")
                .getOrThrow()
            TaxonPage(
                items = result.results.toDomain(),
                endOfRecords = result.endOfRecords
            )
        }

    override suspend fun getChildren(parentKey: Int): List<Taxon> = withContext(Dispatchers.Default) {
        api.getChildren(parentKey = parentKey, offset = 0, limit = pageSize)
            .map { page -> page.results.toDomain() }
            .getOrThrow()
    }
}