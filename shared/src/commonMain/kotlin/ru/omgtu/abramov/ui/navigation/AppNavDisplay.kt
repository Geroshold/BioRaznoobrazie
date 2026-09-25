package ru.omgtu.abramov.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import ru.omgtu.abramov.Screen
import ru.omgtu.abramov.detail.TaxonDetailViewModel
import ru.omgtu.abramov.detail.TaxonDetailViewModelFactory
import ru.omgtu.abramov.list.TaxonListViewModel
import ru.omgtu.abramov.list.TaxonListViewModelFactory
import ru.omgtu.abramov.ui.screens.detail.TaxonDetailScreen
import ru.omgtu.abramov.ui.screens.list.TaxonListScreen

private const val TRANSITION_MS = 300

@Composable
fun AppNavDisplay(
    modifier: Modifier,
    navigator: Navigator,
    listViewModelFactory: TaxonListViewModelFactory,
    detailViewModelFactory: TaxonDetailViewModelFactory,
    childrenViewModelFactory: (Int) -> TaxonListViewModelFactory,
) {
    val backStack by navigator.navStack.collectAsStateWithLifecycle()

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = navigator::back,
        transitionSpec = { slide(SlideDirection.Start) },
        popTransitionSpec = { slide(SlideDirection.End) },
        predictivePopTransitionSpec = { slide(SlideDirection.End) },

        entryProvider = entryProvider {
            entry<Screen.List> {
                val viewModel: TaxonListViewModel = viewModel(factory = listViewModelFactory)
                val state by viewModel.state.collectAsStateWithLifecycle()
                TaxonListScreen(state = state, onIntent = viewModel::onIntent)
            }

            entry<Screen.Detail> { screen ->
                val viewModel: TaxonDetailViewModel = viewModel(
                    key = "detail-${screen.key}",
                    factory = detailViewModelFactory,
                    extras = TaxonDetailViewModelFactory.extrasFor(screen.key),
                )
                val state by viewModel.state.collectAsStateWithLifecycle()
                state?.let { TaxonDetailScreen(taxon = it, onIntent = viewModel::onIntent) }
            }

            entry<Screen.Children> { screen ->
                val factory = remember(screen.parentKey) {
                    childrenViewModelFactory(screen.parentKey)
                }
                val viewModel: TaxonListViewModel = viewModel(
                    key = "children-${screen.parentKey}",
                    factory = factory,
                )
                val state by viewModel.state.collectAsStateWithLifecycle()
                TaxonListScreen(state = state, onIntent = viewModel::onIntent)
            }
        },
    )
}

private fun AnimatedContentTransitionScope<*>.slide(
    direction: SlideDirection,
): ContentTransform =
    (slideIntoContainer(direction, tween(TRANSITION_MS)) + fadeIn(tween(TRANSITION_MS)))
        .togetherWith(
            slideOutOfContainer(direction, tween(TRANSITION_MS)) + fadeOut(tween(TRANSITION_MS)),
        )