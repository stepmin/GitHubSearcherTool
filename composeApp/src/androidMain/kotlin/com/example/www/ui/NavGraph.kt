package com.example.www.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.www.ListDestination
import com.example.www.ui.screens.RepositoryListScreen

@Composable
fun NavGraph(
    paddingValues: PaddingValues,
    navController: NavHostController = rememberNavController(),
    startDestination: Any,
    finishActivity: () -> Unit = {},
) {
    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<ListDestination> {
            BackHandler {
                finishActivity.invoke()
            }
            RepositoryListScreen()
        }
    }
}