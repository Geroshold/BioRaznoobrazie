package ru.omgtu.abramov.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import ru.omgtu.abramov.data.network.GbifApi
import ru.omgtu.abramov.data.network.toDomain
import ru.omgtu.abramov.core.suspendRunCatching
import ru.omgtu.abramov.domain.Taxon
import ru.omgtu.abramov.domain.TaxonPage
import ru.omgtu.abramov.domain.TaxonRepository
import ru.omgtu.abramov.utils.stripHtml

class GbifTaxonRepository(
    private val api: GbifApi,
    private val pageSize: Int = 50,
) : TaxonRepository {

    override suspend fun getTaxon(key: Int): Result<Taxon?> = withContext(Dispatchers.Default) {
        suspendRunCatching {
            coroutineScope {
                val detailDeferred = async { api.getTaxon(key) }
                val descriptionsDeferred = async { api.getDescriptions(key) }

                val detailDto = detailDeferred.await().getOrThrow()
                val rawDescription = descriptionsDeferred.await()
                    .getOrNull()
                    ?.results
                    ?.firstOrNull { !it.description.isNullOrBlank() }
                    ?.description

                val cleanDescription = rawDescription?.stripHtml()
                detailDto.copy(description = cleanDescription).toDomain()
            }
        }
    }

    override suspend fun getTaxonsPage(offset: Int, limit: Int, query: String?): Result<TaxonPage> =
        withContext(Dispatchers.Default) {
            suspendRunCatching {
                val pageDto = api.searchTaxons(
                    query = query,
                    offset = offset,
                    limit = limit,
                    rank = null,
                ).getOrThrow()

                TaxonPage(
                    items = pageDto.results.toDomain(),
                    endOfRecords = pageDto.endOfRecords,
                )
            }
        }

    override suspend fun getChildrenPage(parentKey: Int, offset: Int, limit: Int): Result<TaxonPage> =
        withContext(Dispatchers.Default) {
            suspendRunCatching {
                val pageDto = api.getChildren(
                    parentKey = parentKey,
                    offset = offset,
                    limit = limit,
                ).getOrThrow()

                TaxonPage(
                    items = pageDto.results.toDomain(),
                    endOfRecords = pageDto.endOfRecords,
                )
            }
        }
}