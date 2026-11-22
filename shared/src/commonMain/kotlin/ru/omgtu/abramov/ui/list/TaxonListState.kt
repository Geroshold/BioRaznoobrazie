package ru.omgtu.abramov.ui.list

import ru.omgtu.abramov.ui.model.TaxonCardUi

data class TaxonListState (
    val query: String = "",
    val items: List<TaxonCardUi> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val loadMoreFailed: Boolean = false,
    val endOfRecords: Boolean = false,
    val errorDetail: Throwable? = null,
)