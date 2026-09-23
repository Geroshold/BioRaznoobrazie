package ru.omgtu.abramov

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.omgtu.abramov.data.TaxonRepositoryImpl
import ru.omgtu.abramov.list.TaxonListViewModel
import ru.omgtu.abramov.ui.components.AppScaffold
import ru.omgtu.abramov.ui.screensList.TaxonListScreen

@Composable
fun App() {
    MaterialTheme {
        val viewModel: TaxonListViewModel = viewModel { TaxonListViewModel(onOpenCard = {}) }
        val state by viewModel.state.collectAsStateWithLifecycle()

        AppScaffold { modifier ->
            TaxonListScreen(
                state = state,
                onIntent = viewModel::onIntent,
                modifier = modifier,
            )
        }
    }
}