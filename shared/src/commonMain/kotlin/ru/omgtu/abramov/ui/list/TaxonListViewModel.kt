package ru.omgtu.abramov.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.omgtu.abramov.data.TaxonRepositoryImpl
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
import ru.omgtu.abramov.ui.model.getCards
import kotlin.collections.copy

class TaxonListViewModel(
    private val navigator: Navigator,
    private val repository: TaxonRepository = TaxonRepositoryImpl(),
    private val parentKey: Int? = null,
) : ViewModel() {

    private val _state = MutableStateFlow(TaxonListState())
    val state: StateFlow<TaxonListState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val taxa = if (parentKey == null) {
                repository.getTaxons()
            } else {
                repository.getChildren(parentKey)
            }
            _state.update { it.copy(items = taxa.toCardsUi()) }
        }
        search(filter = null)
    }

    private var searchJob: Job? = null

    private fun search(filter: String?) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val items = repository.getCards(filter)
            _state.update { it.copy(items = items) }
        }
    }

    fun onIntent(intent: TaxonListIntent) {
        when (intent) {
            is TaxonListIntent.CardClicked -> navigator.addToBackStack(Screen.Detail(intent.key))

            is TaxonListIntent.QueryChanged -> {
                _state.update { it.copy(query = intent.query) }
                search(filter = intent.query)
            }
        }
    }
}