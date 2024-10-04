package com.example.www.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.www.R
import com.example.www.screens.repositories.RepositoriesViewModel
import com.example.www.ui.components.InfoBox
import com.example.www.ui.components.RepositoryItem
import com.example.www.ui.theme.backgroundsSecondaryLight
import org.koin.androidx.compose.koinViewModel

@Composable
fun RepositoryListScreen() {
    val viewModel: RepositoriesViewModel = koinViewModel()
    val repositories by viewModel.repositories.collectAsStateWithLifecycle()

    AnimatedContent(repositories.items?.isNotEmpty()) { objectsAvailable ->
        if (objectsAvailable == true) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundsSecondaryLight)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(repositories.items?.size ?: 0) {
                    val repo = repositories.items?.get(it)
                    if (repo != null) {
                        RepositoryItem(repo = repo,
                            onFavoriteClicked = {
                                {
                                    //TODO-ADD FAVORITE
                                }
                            })
                    }
                }
            }
        } else {
            InfoBox(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                text = stringResource(R.string.no_data_available)
            )
        }
    }
}