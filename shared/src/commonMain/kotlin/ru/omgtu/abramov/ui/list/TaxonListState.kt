package ru.omgtu.abramov.ui.list

import ru.omgtu.abramov.ui.model.TaxonCardUi

sealed interface TaxonListState {
    val query: String

    data class Loading(
        override val query: String = "",
    ) : TaxonListState

    data class Error(
        override val query: String = "",
        val throwable: Throwable,
    ) : TaxonListState

    data class Content(
        override val query: String = "",
        val allItems: List<TaxonCardUi> = emptyList(),
        val items: List<TaxonCardUi> = emptyList(),
        val isLoadingMore: Boolean = false,
        val loadMoreError: Throwable? = null,
        val endOfRecords: Boolean = false,
    ) : TaxonListState
}