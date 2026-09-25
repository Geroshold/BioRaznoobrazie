package ru.omgtu.abramov.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import ru.omgtu.abramov.domain.TaxonRepository
import ru.omgtu.abramov.ui.navigation.Navigator
import kotlin.reflect.KClass

class TaxonListViewModelFactory (
    private val navigator: Navigator,
    private val repository: TaxonRepository,
    private val parentKey: Int? = null,
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            @Suppress("UNCHECKED_CAST")
            return TaxonListViewModel(
                navigator = navigator,
                repository = repository,
                parentKey = parentKey
            ) as T
        }
    }