package ru.omgtu.abramov.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
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

    private val isSearchEnabled = parentKey == null

    private val _state = MutableStateFlow<TaxonListState>(
        TaxonListState.Loading(isSearchEnabled = isSearchEnabled)
    )
    val state: StateFlow<TaxonListState> = _state.asStateFlow()

    private val queryFlow = MutableStateFlow("")
    private var loadJob: Job? = null
    private var loadMoreJob: Job? = null

    init {
        loadFirstPage(query = null)

        if (isSearchEnabled) {
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
        loadJob?.cancel()
        loadMoreJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.update {
                TaxonListState.Loading(
                    query = it.query,
                    isSearchEnabled = isSearchEnabled,
                )
            }

            val result = if (parentKey == null) {
                repository.getTaxonsPage(offset = 0, limit = PAGE_SIZE, query = query)
            } else {
                repository.getChildrenPage(parentKey, offset = 0, limit = PAGE_SIZE)
            }

            result.fold(
                onSuccess = { page ->
                    _state.update { current ->
                        TaxonListState.Content(
                            query = current.query,
                            isSearchEnabled = isSearchEnabled,
                            items = page.items.toCardsUi(),
                            endOfRecords = page.endOfRecords,
                        )
                    }
                },
                onFailure = { throwable ->
                    _state.update { current ->
                        TaxonListState.Error(
                            query = current.query,
                            isSearchEnabled = isSearchEnabled,
                            throwable = throwable,
                        )
                    }
                },
            )
        }
    }

    private fun loadMore() {
        val current = _state.value as? TaxonListState.Content ?: return
        if (current.isLoadingMore || current.endOfRecords || loadMoreJob?.isActive == true) return

        loadMoreJob = viewModelScope.launch {
            _state.update { state ->
                (state as? TaxonListState.Content)?.copy(
                    isLoadingMore = true,
                    loadMoreError = null,
                ) ?: state
            }

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
                    _state.update { state ->
                        val content = state as? TaxonListState.Content ?: return@update state
                        val seenKeys = content.items.mapTo(HashSet()) { it.key }
                        val uniqueNewItems = newItems.filter { seenKeys.add(it.key) }

                        content.copy(
                            items = content.items + uniqueNewItems,
                            isLoadingMore = false,
                            loadMoreError = null,
                            endOfRecords = page.endOfRecords,
                        )
                    }
                },
                onFailure = { throwable ->
                    _state.update { state ->
                        (state as? TaxonListState.Content)?.copy(
                            isLoadingMore = false,
                            loadMoreError = throwable,
                        ) ?: state
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
                if (!isSearchEnabled) return
                _state.update { current ->
                    when (current) {
                        is TaxonListState.Loading -> current.copy(query = intent.query)
                        is TaxonListState.Error -> current.copy(query = intent.query)
                        is TaxonListState.Content -> current.copy(query = intent.query)
                    }
                }
                queryFlow.value = intent.query
            }

            TaxonListIntent.LoadMore -> loadMore()

            TaxonListIntent.Retry -> {
                when (_state.value) {
                    is TaxonListState.Error -> {
                        val activeQuery = _state.value.query.trim().ifBlank { null }
                        loadFirstPage(query = activeQuery)
                    }
                    is TaxonListState.Content -> loadMore()
                    is TaxonListState.Loading -> Unit
                }
            }
        }
    }
}