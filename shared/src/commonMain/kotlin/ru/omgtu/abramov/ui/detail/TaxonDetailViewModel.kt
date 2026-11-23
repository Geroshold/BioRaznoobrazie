package ru.omgtu.abramov.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.omgtu.abramov.Screen
import ru.omgtu.abramov.domain.TaxonRepository
import ru.omgtu.abramov.ui.model.TaxonDetailUi
import ru.omgtu.abramov.ui.model.toDetailUi
import ru.omgtu.abramov.ui.navigation.Navigator

class TaxonDetailViewModel(
    private val key: Int,
    private val navigator: Navigator,
    private val repository: TaxonRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<TaxonDetailUi?>(null)
    val state: StateFlow<TaxonDetailUi?> = _state.asStateFlow()

    private val _error = MutableStateFlow<Throwable?>(null)
    val error: StateFlow<Throwable?> = _error.asStateFlow()

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _error.value = null
            repository.getTaxon(key).fold(
                onSuccess = { taxon ->
                    _state.value = taxon?.toDetailUi()
                },
                onFailure = { throwable ->
                    _error.value = throwable
                },
            )
        }
    }

    fun onIntent(intent: TaxonDetailIntent) {
        when (intent) {
            TaxonDetailIntent.OpenChildren -> {
                _state.value?.let {
                    navigator.addToBackStack(Screen.Children(it.key, it.name))
                }
            }
            TaxonDetailIntent.Retry -> load()
        }
    }
}