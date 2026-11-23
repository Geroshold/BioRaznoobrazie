package ru.omgtu.abramov.ui.list

import ru.omgtu.abramov.ui.model.TaxonCardUi

sealed interface TaxonListState {
    val query: String
    val isSearchEnabled: Boolean

    data class Loading(
        override val query: String = "",
        override val isSearchEnabled: Boolean = true,
    ) : TaxonListState

    data class Error(
        override val query: String = "",
        override val isSearchEnabled: Boolean = true,
        val throwable: Throwable,
    ) : TaxonListState

    data class Content(
        override val query: String = "",
        override val isSearchEnabled: Boolean = true,
        val items: List<TaxonCardUi> = emptyList(),
        val isLoadingMore: Boolean = false,
        val loadMoreError: Throwable? = null,
        val endOfRecords: Boolean = false,
    ) : TaxonListState
}