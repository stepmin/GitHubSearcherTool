package com.example.www.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import app.cash.paging.LoadStateError
import app.cash.paging.LoadStateLoading
import app.cash.paging.LoadStateNotLoading
import com.example.www.R
import com.example.www.screens.repositories.RepositoriesViewModel
import com.example.www.ui.components.InfoBox
import com.example.www.ui.components.RepositoryItem
import com.example.www.ui.theme.accentSecondaryLight
import com.example.www.ui.theme.backgroundsPrimaryLight
import com.example.www.ui.theme.backgroundsSecondaryLight
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import org.koin.androidx.compose.koinViewModel

@Composable
fun RepositoryListScreen(
    query: MutableState<String>
) {
    val viewModel: RepositoriesViewModel = koinViewModel()
    val repositories by rememberUpdatedState(viewModel.repositories.collectAsLazyPagingItems())

    LaunchedEffect(query.value) {
        snapshotFlow { query.value }
            .debounce(300)
            .filter { it.length >= 3 }
            .collect {
                viewModel.onSearch(query.value)
            }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundsSecondaryLight)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        items(repositories.itemCount) { index ->
            val item = repositories[index]
            if (item != null) {
                RepositoryItem(repo = item,
                    onStarClicked = {
                        {
                            //TODO-ADD FAVORITE
                        }
                    })
            }
        }

        repositories.loadState.apply {
            println("state: $this")
            when {
                refresh is LoadStateNotLoading && repositories.itemCount < 1 -> {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_data_available),
                                modifier = Modifier.align(Alignment.Center),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                refresh is LoadStateLoading -> {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = accentSecondaryLight
                            )
                        }
                    }
                }

                append is LoadStateLoading -> {
                    item {
                        CircularProgressIndicator(
                            color = backgroundsPrimaryLight,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }

                refresh is LoadStateError -> {
                    item {
                        InfoBox(
                            text = stringResource(R.string.data_error_message),
                            modifier = Modifier.fillParentMaxSize()
                        )
                    }
                }

                append is LoadStateError -> {
                    item {
                        InfoBox(
                            text = stringResource(R.string.data_error_message),
                        )
                    }
                }
            }
        }
    }
}