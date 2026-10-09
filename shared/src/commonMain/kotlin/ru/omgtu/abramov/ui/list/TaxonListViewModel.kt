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
import ru.omgtu.abramov.domain.filterByName

class TaxonListViewModel(
    private val navigator: Navigator,
    private val repository: TaxonRepository = TaxonRepositoryImpl(),
    private val parentKey: Int? = null,
) : ViewModel() {

    private val _state = MutableStateFlow(TaxonListState())
    val state: StateFlow<TaxonListState> = _state.asStateFlow()

    init {
        load(null)
    }

    private var loadJob: Job? = null

    private fun load(filter: String?) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val base = if (parentKey == null) {
                repository.getTaxons()
            } else {
                repository.getChildren(parentKey)
            }
            val filtered = base.filterByName(filter)
            _state.update { it.copy(items = filtered.toCardsUi()) }
        }
    }

    fun onIntent(intent: TaxonListIntent) {
        when (intent) {
            is TaxonListIntent.CardClicked -> navigator.addToBackStack(Screen.Detail(intent.key))

            is TaxonListIntent.QueryChanged -> {
                _state.update { it.copy(query = intent.query) }
                load(intent.query)
            }
        }
    }
}