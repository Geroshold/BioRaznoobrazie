package ru.omgtu.abramov

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.omgtu.abramov.data.TaxonRepositoryImpl
import ru.omgtu.abramov.detail.TaxonDetailViewModelFactory
import ru.omgtu.abramov.domain.TaxonRepository
import ru.omgtu.abramov.list.TaxonListViewModelFactory
import ru.omgtu.abramov.ui.components.AppScaffold
import ru.omgtu.abramov.ui.navigation.AppNavDisplay
import ru.omgtu.abramov.ui.navigation.Navigator

@Composable
fun App() {
    MaterialTheme {
        val repository: TaxonRepository = remember { TaxonRepositoryImpl() }
        val navigator = remember { Navigator() }
        val listViewModelFactory = remember { TaxonListViewModelFactory(navigator, repository) }
        val detailViewModelFactory = remember { TaxonDetailViewModelFactory(navigator, repository) }
        val childrenViewModelFactory: (Int) -> TaxonListViewModelFactory =
            remember {
                { parentKey ->
                    TaxonListViewModelFactory(navigator, repository, parentKey)
                }
            }

        val navStack = navigator.navStack.collectAsStateWithLifecycle()
        val backNavigationIsAvailable by remember {
            derivedStateOf { navStack.value.size > 1 }
        }

        AppScaffold(
            onBack = navigator::back.takeIf { backNavigationIsAvailable },
        ) { modifier ->
            AppNavDisplay(
                modifier = modifier,
                navigator = navigator,
                listViewModelFactory = listViewModelFactory,
                detailViewModelFactory = detailViewModelFactory,
                childrenViewModelFactory = childrenViewModelFactory,
            )
        }
    }
}