package com.example.www

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.www.ui.theme.backgroundsPrimaryLight
import kotlinx.serialization.Serializable

@Serializable
object ListDestination

@Composable
fun App(
    navController: NavHostController = rememberNavController(),
) {
    //TODO-dark theme
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        Surface {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = ListDestination
            ) {
                composable<ListDestination> {
                    //TODO
                }
            }
        }

        Scaffold(
            topBar = {
                TopBar(
                    actionBarTitle = stringResource(R.string.app_name),
                )
            }
        ) { paddingValues ->

            NavHost(
                navController = navController,
                startDestination = ListDestination,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable<ListDestination> {
                    //TODO
                }
            }
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