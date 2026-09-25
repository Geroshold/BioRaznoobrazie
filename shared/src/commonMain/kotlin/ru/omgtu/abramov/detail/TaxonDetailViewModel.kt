package ru.omgtu.abramov.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.omgtu.abramov.Screen
import ru.omgtu.abramov.data.TaxonRepositoryImpl
import ru.omgtu.abramov.domain.TaxonRepository
import ru.omgtu.abramov.ui.model.TaxonDetailUi
import ru.omgtu.abramov.ui.model.toDetailUi
import ru.omgtu.abramov.ui.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaxonDetailViewModel(
    private val key: Int,
    private val navigator: Navigator,
    private val repository: TaxonRepository = TaxonRepositoryImpl(),
) : ViewModel() {

    private val _state = MutableStateFlow<TaxonDetailUi?>(null)
    val state: StateFlow<TaxonDetailUi?> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val taxon = repository.getTaxon(key)
            _state.value = taxon?.toDetailUi()
        }
    }

    fun onIntent(intent: TaxonDetailIntent) {
        when (intent) {
            TaxonDetailIntent.OpenChildren -> openChildren()
        }
    }

    private fun openChildren() {
        _state.value?.let {
            navigator.addToBackStack(Screen.Children(it.key, it.name))
        }
    }
}