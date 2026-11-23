package ru.omgtu.abramov.ui.screens.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import ru.omgtu.abramov.resources.Res
import ru.omgtu.abramov.resources.error_load_list
import ru.omgtu.abramov.resources.error_unknown
import ru.omgtu.abramov.resources.search_empty
import ru.omgtu.abramov.resources.search_hint
import ru.omgtu.abramov.resources.action_retry
import ru.omgtu.abramov.resources.children_empty
import ru.omgtu.abramov.ui.components.FullScreenError
import ru.omgtu.abramov.ui.components.FullScreenLoading
import ru.omgtu.abramov.ui.list.TaxonListIntent
import ru.omgtu.abramov.ui.list.TaxonListState

@Composable
fun TaxonListScreen(
    state: TaxonListState,
    onIntent: (TaxonListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        OutlinedTextField(
            value = state.query,
            onValueChange = { onIntent(TaxonListIntent.QueryChanged(it)) },
            label = { Text(stringResource(Res.string.search_hint)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )

        when (state) {
            is TaxonListState.Loading -> FullScreenLoading()

            is TaxonListState.Error -> {
                val errorMessage = state.throwable.message
                    ?: stringResource(Res.string.error_unknown)
                FullScreenError(
                    message = errorMessage,
                    onRetry = { onIntent(TaxonListIntent.Retry) },
                )
            }

            is TaxonListState.Content -> {
                if (state.items.isEmpty()) {
                    val emptyText = if (state.query.isNotBlank() || state.allItems.isNotEmpty()) {
                        stringResource(Res.string.search_empty)
                    } else {
                        stringResource(Res.string.children_empty)
                    }
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(emptyText)
                    }
                } else {
                    TaxonListContent(
                        state = state,
                        onIntent = onIntent,
                    )
                }
            }
        }
    }
}

@Composable
private fun TaxonListContent(
    state: TaxonListState.Content,
    onIntent: (TaxonListIntent) -> Unit,
) {
    val listState = rememberLazyListState()
    val currentItemsSize by rememberUpdatedState(state.items.size)
    val currentCanLoadMore by rememberUpdatedState(
        !state.isLoadingMore && !state.endOfRecords && state.loadMoreError == null
    )

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= currentItemsSize - 5 &&
                    currentCanLoadMore
                ) {
                    onIntent(TaxonListIntent.LoadMore)
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(state.items, key = { it.key }) { taxon ->
            TaxonCard(
                taxon = taxon,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onIntent(TaxonListIntent.CardClicked(taxon.key)) },
            )
        }

        if (state.isLoadingMore) {
            item(key = "pagination_loading") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        } else if (state.loadMoreError != null) {
            item(key = "pagination_error") {
                PaginationErrorFooter(
                    throwable = state.loadMoreError,
                    onRetry = { onIntent(TaxonListIntent.Retry) },
                )
            }
        }
    }
}

@Composable
private fun PaginationErrorFooter(
    throwable: Throwable,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val errorMessage = throwable.message ?: stringResource(Res.string.error_unknown)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(Res.string.error_load_list, errorMessage),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
        )
        Button(onClick = onRetry) {
            Text(stringResource(Res.string.action_retry))
        }
    }
}