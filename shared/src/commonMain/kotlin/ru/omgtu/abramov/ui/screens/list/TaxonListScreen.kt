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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import ru.omgtu.abramov.ui.list.TaxonListIntent
import ru.omgtu.abramov.ui.list.TaxonListState
import ru.omgtu.abramov.resources.Res
import ru.omgtu.abramov.resources.search_empty
import ru.omgtu.abramov.resources.search_hint
import ru.omgtu.abramov.resources.error_load_list
import ru.omgtu.abramov.resources.error_unknown
import ru.omgtu.abramov.ui.components.FullScreenError
import ru.omgtu.abramov.ui.components.FullScreenLoading

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
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        )
        val errorMessage = state.errorDetail?.let {
            it.message ?: stringResource(Res.string.error_unknown)
        }
        when {
            state.isLoading && state.items.isEmpty() -> FullScreenLoading()

            state.errorDetail != null && state.items.isEmpty() ->
                FullScreenError(
                    message = stringResource(Res.string.error_load_list, errorMessage ?: ""),
                    onRetry = { onIntent(TaxonListIntent.QueryChanged(state.query)) },
                )

            state.items.isEmpty() && state.query.isNotBlank() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(Res.string.search_empty))
                }
            }

            else -> {
                val listState = rememberLazyListState()
                val currentItemsSize by rememberUpdatedState(state.items.size)
                val currentIsLoadingMore by rememberUpdatedState(state.isLoadingMore)
                val currentEndOfRecords by rememberUpdatedState(state.endOfRecords)
                LaunchedEffect(listState) {
                    snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                        .collect { lastVisibleIndex ->
                            if (lastVisibleIndex != null &&
                                lastVisibleIndex >= currentItemsSize - 5 &&
                                !currentIsLoadingMore &&
                                !currentEndOfRecords
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
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}