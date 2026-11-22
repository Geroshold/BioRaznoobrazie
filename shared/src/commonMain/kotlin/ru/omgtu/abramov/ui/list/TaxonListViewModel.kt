package ru.omgtu.abramov.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.omgtu.abramov.Screen
import ru.omgtu.abramov.domain.TaxonRepository
import ru.omgtu.abramov.ui.model.toCardsUi
import ru.omgtu.abramov.ui.navigation.Navigator

private const val SEARCH_DEBOUNCE_MS = 400L
private const val PAGE_SIZE = 50

@OptIn(FlowPreview::class)
class TaxonListViewModel(
    private val navigator: Navigator,
    private val repository: TaxonRepository,
    private val parentKey: Int? = null,
) : ViewModel() {

    private val _state = MutableStateFlow(TaxonListState(isLoading = true))
    val state: StateFlow<TaxonListState> = _state.asStateFlow()

    private val queryFlow = MutableStateFlow("")

    init {
        loadFirstPage(query = null)

        if (parentKey == null) {
            viewModelScope.launch {
                queryFlow
                    .drop(1)
                    .map { it.trim() }
                    .debounce(SEARCH_DEBOUNCE_MS)
                    .distinctUntilChanged()
                    .collectLatest { debouncedQuery ->
                        loadFirstPage(query = debouncedQuery.ifBlank { null })
                    }
            }
        }
    }

    private fun loadFirstPage(query: String?) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    loadMoreFailed = false,
                    errorDetail = null,
                )
            }

            val result = if (parentKey == null) {
                repository.getTaxonsPage(offset = 0, limit = PAGE_SIZE, query = query)
            } else {
                repository.getChildrenPage(parentKey, offset = 0, limit = PAGE_SIZE)
            }

            result.fold(
                onSuccess = { page ->
                    _state.update {
                        it.copy(
                            items = page.items.toCardsUi(),
                            isLoading = false,
                            loadMoreFailed = false,
                            errorDetail = null,
                            endOfRecords = page.endOfRecords,
                        )
                    }
                },
                onFailure = { throwable ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorDetail = throwable,
                        )
                    }
                },
            )
        }
    }

    private fun loadMore() {
        val current = _state.value
        if (current.isLoading || current.isLoadingMore || current.endOfRecords) return

        viewModelScope.launch {
            _state.update { it.copy(isLoadingMore = true, loadMoreFailed = false) }

            val activeQuery = current.query.trim().ifBlank { null }
            val result = if (parentKey == null) {
                repository.getTaxonsPage(
                    offset = current.items.size,
                    limit = PAGE_SIZE,
                    query = activeQuery,
                )
            } else {
                repository.getChildrenPage(
                    parentKey = parentKey,
                    offset = current.items.size,
                    limit = PAGE_SIZE,
                )
            }

            result.fold(
                onSuccess = { page ->
                    val newItems = page.items.toCardsUi()
                    _state.update {
                        val seenKeys = it.items.mapTo(HashSet()) { card -> card.key }
                        val uniqueNewItems = newItems.filter { card -> seenKeys.add(card.key) }

                        it.copy(
                            items = it.items + uniqueNewItems,
                            isLoadingMore = false,
                            loadMoreFailed = false,
                            endOfRecords = page.endOfRecords,
                        )
                    }
                },
                onFailure = {
                    _state.update {
                        it.copy(
                            isLoadingMore = false,
                            loadMoreFailed = true,
                        )
                    }
                },
            )
        }
    }

    fun onIntent(intent: TaxonListIntent) {
        when (intent) {
            is TaxonListIntent.CardClicked ->
                navigator.addToBackStack(Screen.Detail(intent.key))

            is TaxonListIntent.QueryChanged -> {
                _state.update { it.copy(query = intent.query) }
                queryFlow.value = intent.query
            }

            TaxonListIntent.LoadMore -> loadMore()
        }
    }
}