package ru.omgtu.abramov.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ru.omgtu.abramov.core.suspendRunCatching
import ru.omgtu.abramov.data.network.dto.TaxonDto
import ru.omgtu.abramov.data.network.dto.TaxonPageDto

class GbifApi(private val client: HttpClient) {

    suspend fun searchTaxons(
        query: String?,
        offset: Int,
        limit: Int,
        rank: String? = null,
    ): Result<TaxonPageDto> = suspendRunCatching {
        client.get("species/search") {
            parameter("offset", offset)
            parameter("limit", limit)
            query?.takeIf { it.isNotBlank() }?.let { parameter("q", it) }
            rank?.let { parameter("rank", it) }
        }.body()
    }

    suspend fun getTaxon(key: Int): Result<TaxonDto> = suspendRunCatching {
        client.get("species/$key").body()
    }

    suspend fun getChildren(
        parentKey: Int,
        offset: Int,
        limit: Int,
    ): Result<TaxonPageDto> = suspendRunCatching {
        client.get("species/$parentKey/children") {
            parameter("offset", offset)
            parameter("limit", limit)
        }.body()
    }

    suspend fun getTaxonWithDescription(key: Int): Result<TaxonDto> = suspendRunCatching {
        val detail = getTaxon(key).getOrThrow()
        if (!detail.descriptions.isNullOrEmpty()) return@suspendRunCatching detail

        val name = detail.scientificName ?: return@suspendRunCatching detail
        val search = searchTaxons(query = name, offset = 0, limit = 5).getOrNull()
        val fromSearch = search?.results?.firstOrNull { it.key == key }
        detail.copy(descriptions = fromSearch?.descriptions ?: detail.descriptions)
    }
}
