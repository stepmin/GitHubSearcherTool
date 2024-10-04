package com.example.www

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.www.ui.NavGraph
import com.example.www.ui.theme.backgroundsPrimaryLight
import kotlinx.serialization.Serializable

@Serializable
object ListDestination

@Composable
fun ComposeApp(
    navController: NavHostController = rememberNavController(),
    finishActivity: () -> Unit
) {
    //TODO-dark theme
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    actionBarTitle = stringResource(R.string.app_name),
                )
            }
        ) { paddingValues ->
            NavGraph(
                paddingValues = paddingValues,
                navController = navController,
                startDestination = ListDestination,
                finishActivity = finishActivity
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    actionBarTitle: String,
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
            Text(
                text = actionBarTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundsPrimaryLight),
    )
}