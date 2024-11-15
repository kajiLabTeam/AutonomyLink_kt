package net.kajilab.elpissender.Presenter.ui.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import net.kajilab.elpissender.Presenter.ui.view.Home.HomeScreen
import net.kajilab.elpissender.Presenter.ui.view.Setting.SettingScreen
import net.kajilab.elpissender.Presenter.ui.view.User.UserScreen
import net.kajilab.elpissender.entity.BottomNavigationBarRoute

@Composable
fun MainRouter(
    changeTopBarTitle: (String) -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    topAppBarActions: ( List<@Composable () -> Unit>) -> Unit,
    toSettingScreen: () -> Unit,
    showSnackbar: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavigationBarRoute.HOME.route,
        modifier = modifier.fillMaxSize()
    ) {
        composable(BottomNavigationBarRoute.HOME.route) {
            HomeScreen(
                topAppBarActions = topAppBarActions
            )
            changeTopBarTitle(BottomNavigationBarRoute.HOME.title)
        }
        composable(BottomNavigationBarRoute.USER.route) {
            UserScreen(
                showSnackbar = showSnackbar
            )
            topAppBarActions(
                listOf{
                    IconButton(onClick = {
                        toSettingScreen()
                    }) {
                        Icon(Icons.Filled.Settings, "Setting")
                    }
                }
            )
            changeTopBarTitle(BottomNavigationBarRoute.USER.title)
        }
        composable(BottomNavigationBarRoute.SETTING.route) {
            SettingScreen(
                showSnackbar = showSnackbar
            )
            topAppBarActions(
                listOf()
            )
            changeTopBarTitle(BottomNavigationBarRoute.SETTING.title)
        }

    }
}