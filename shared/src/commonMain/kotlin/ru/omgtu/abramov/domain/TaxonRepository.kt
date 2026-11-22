package ru.omgtu.abramov.domain

interface TaxonRepository {
    suspend fun getTaxon(key: Int): Result<Taxon?>
    suspend fun getChildrenPage(parentKey: Int, offset: Int, limit: Int): Result<TaxonPage>
    suspend fun getTaxonsPage(offset: Int, limit: Int, query: String? = null): Result<TaxonPage>
}