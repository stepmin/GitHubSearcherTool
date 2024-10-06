package com.example.www

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.www.ui.screens.RepositoryListScreen
import com.example.www.ui.theme.backgroundsPrimaryLight
import kotlinx.serialization.Serializable

@Serializable
object ListDestination

@Composable
fun ComposeApp(
    navController: NavHostController = rememberNavController(),
    finishActivity: () -> Unit
) {
    val query = rememberSaveable {
        mutableStateOf("")
    }
    //TODO-dark theme
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    query = query,
                    onSearch = {
                        query.value = it
                    }
                )
            }
        ) { paddingValues ->
            NavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                startDestination = ListDestination,
            ) {
                composable<ListDestination> {
                    BackHandler {
                        finishActivity.invoke()
                    }
                    RepositoryListScreen(query)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    query: MutableState<String>,
    onSearch: (String) -> Unit
) {
    val searchBarState = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(searchBarState.value) {
        if (searchBarState.value) {
            focusRequester.requestFocus()
        }
    }

    TopAppBar(
        title = {
            NavigationAndSearch(query, onSearch)
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundsPrimaryLight),
    )
}

@Composable
fun NavigationAndSearch(
    query: MutableState<String>,
    onSearch: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_github),
            contentDescription = "App logo",
            modifier = Modifier.size(35.dp)
        )
        Spacer(modifier = Modifier.width(8.dp)) // Adjust spacing here
        TextField(
            value = query.value,
            onValueChange = onSearch,
            placeholder = {
                Text(stringResource(R.string.search_hint))
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
        )
    }
}