package ru.omgtu.abramov.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.omgtu.abramov.data.TaxonRepositoryImpl
import ru.omgtu.abramov.domain.TaxonRepository
import ru.omgtu.abramov.ui.model.toCardsUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaxonListViewModel(
    private val onOpenCard: (Int) -> Unit,
    private val repository: TaxonRepository = TaxonRepositoryImpl(),
) : ViewModel() {

    private val _state = MutableStateFlow(TaxonListState())
    val state: StateFlow<TaxonListState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val items = repository.getTaxons().toCardsUi()
            _state.update { it.copy(items = items) }
        }
    }

    fun onIntent(intent: TaxonListIntent) {
        when (intent) {
            is TaxonListIntent.CardClicked -> onOpenCard(intent.key)
        }
    }
}