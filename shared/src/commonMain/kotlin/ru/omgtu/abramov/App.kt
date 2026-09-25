package ru.omgtu.abramov

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.omgtu.abramov.data.TaxonRepositoryImpl
import ru.omgtu.abramov.detail.TaxonDetailViewModelFactory
import ru.omgtu.abramov.domain.TaxonRepository
import ru.omgtu.abramov.list.TaxonListViewModelFactory
import ru.omgtu.abramov.resources.Res
import ru.omgtu.abramov.resources.action_toggle_theme
import ru.omgtu.abramov.resources.ic_theme
import ru.omgtu.abramov.ui.AppTheme
import ru.omgtu.abramov.ui.components.AppScaffold
import ru.omgtu.abramov.ui.navigation.AppNavDisplay
import ru.omgtu.abramov.ui.navigation.Navigator

@Composable
fun App() {
    var darkTheme by remember { mutableStateOf(false) }

    AppTheme(darkTheme) {
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
            actions = {
                IconButton(onClick = { darkTheme = !darkTheme }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_theme),
                        contentDescription = stringResource(Res.string.action_toggle_theme),
                    )
                }
            },
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