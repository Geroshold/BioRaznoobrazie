package ru.omgtu.abramov.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import ru.omgtu.abramov.domain.TaxonRepository
import ru.omgtu.abramov.ui.model.toCardsUi
import ru.omgtu.abramov.Screen
import ru.omgtu.abramov.ui.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import ru.omgtu.abramov.domain.filterByName

class TaxonListViewModel(
    private val navigator: Navigator,
    private val repository: TaxonRepository,
    private val parentKey: Int? = null,
) : ViewModel() {

    private val _state = MutableStateFlow(TaxonListState())
    val state: StateFlow<TaxonListState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        load(query = null)
    }

    private fun load(query: String?) {
        loadJob?.cancel()
        _state.update { it.copy(isLoading = true, error = null) }
        loadJob = viewModelScope.launch {
            try {
                val base = if (parentKey == null) {
                    repository.getTaxonsPage(offset = 0, limit = 50).items
                } else {
                    repository.getChildren(parentKey)
                }
                val filtered = base.filterByName(query)
                _state.update {
                    it.copy(
                        items = filtered.toCardsUi(),
                        isLoading = false,
                        error = null,
                        endOfRecords = parentKey != null || base.size < 50
                    )
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Throwable) {
                _state.update {
                    it.copy(isLoading = false, error = error)
                }
            }
        }
    }

    private fun loadMore() {
        val current = _state.value
        if (current.isLoading || current.isLoadingMore || current.endOfRecords || parentKey != null) return

        _state.update { it.copy(isLoadingMore = true) }
        viewModelScope.launch {
            try {
                val page = repository.getTaxonsPage(offset = current.items.size, limit = 50)
                val newItems = page.items.toCardsUi()
                _state.update {
                    it.copy(
                        items = it.items + newItems,
                        isLoadingMore = false,
                        endOfRecords = page.endOfRecords
                    )
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Throwable) {
                _state.update { it.copy(isLoadingMore = false, error = error) }
            }
        }
    }

    fun onIntent(intent: TaxonListIntent) {
        when (intent) {
            is TaxonListIntent.CardClicked ->
                navigator.addToBackStack(Screen.Detail(intent.key))

            is TaxonListIntent.QueryChanged -> {
                _state.update { it.copy(query = intent.query) }
                load(query = intent.query)
            }

            TaxonListIntent.LoadMore -> loadMore()
        }
    }
}