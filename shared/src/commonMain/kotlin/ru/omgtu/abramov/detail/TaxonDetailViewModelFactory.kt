package ru.omgtu.abramov.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import ru.omgtu.abramov.domain.TaxonRepository
import ru.omgtu.abramov.ui.navigation.Navigator
import kotlin.reflect.KClass

class TaxonDetailViewModelFactory(
    private val navigator: Navigator,
    private val repository: TaxonRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        val taxonKey = checkNotNull(extras[TaxonKeyArg]) { "key не передан в CreationExtras" }
        @Suppress("UNCHECKED_CAST")
        return TaxonDetailViewModel(
            key = taxonKey,
            navigator = navigator,
            repository = repository,
        ) as T
    }

    companion object {
        val TaxonKeyArg = CreationExtras.Key<Int>()

        fun extrasFor(key: Int): CreationExtras =
            MutableCreationExtras().apply { set(TaxonKeyArg, key) }
    }
}