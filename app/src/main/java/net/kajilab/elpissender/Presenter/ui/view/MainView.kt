package net.kajilab.elpissender.Presenter.ui.view

import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import net.kajilab.elpissender.Presenter.ui.view.Components.BottomNavigationBar
import net.kajilab.elpissender.Presenter.ui.view.Components.PermissionDialog
import net.kajilab.elpissender.R
import net.kajilab.elpissender.entity.BottomNavigationBarRoute
import net.kajilab.elpissender.entity.BottomNavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView() {
    var selectedItemIndex by remember { mutableIntStateOf(0) }
    val navController = rememberNavController()
    var topBarTitle by remember {
        mutableStateOf("TrainAlert2")
    }

    var topAppBarActions by remember {
        mutableStateOf(listOf<@Composable () -> Unit>())
    }

    val hostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val bottomNavigationItems = listOf(
        BottomNavigationItem(
            title = stringResource(id = R.string.home),
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Filled.Home,
            hasNews = false,
            badgeCount = null,
            path = BottomNavigationBarRoute.HOME
        ),
        BottomNavigationItem(
            title = stringResource(id = R.string.user),
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Filled.AccountCircle,
            hasNews = false,
            badgeCount = null,
            path = BottomNavigationBarRoute.USER
        )
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = topBarTitle,
                        fontWeight = FontWeight.Bold,
                    )
                },
                actions = {
                    topAppBarActions.forEach {
                        it()
                    }
                },
                navigationIcon = {
                    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                    if(
                        !(currentRoute == BottomNavigationBarRoute.HOME.route ||
                        currentRoute == BottomNavigationBarRoute.USER.route)
                    ){
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                },
            )
        },
        bottomBar = {
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            if(
                currentRoute == BottomNavigationBarRoute.HOME.route ||
                currentRoute == BottomNavigationBarRoute.USER.route
            ){
                BottomNavigationBar(
                    items = bottomNavigationItems,
                    selectedItemIndex = selectedItemIndex
                ) { index ->
                    selectedItemIndex = index
                    navController.navigate(bottomNavigationItems[index].path.route)
                }
            }
        }
    ) { innerPadding ->
        MainRouter(
            changeTopBarTitle = { title ->
                topBarTitle = title
            },
            navController = navController,
            topAppBarActions = {
                topAppBarActions = it
            },
            toSettingScreen = {
                navController.navigate(BottomNavigationBarRoute.SETTING.route)
            },
            showSnackbar = { message ->
                scope.launch {
                    hostState.showSnackbar(message)
                }
            },
            modifier = Modifier
                .padding(innerPadding)
        )
    }

    PermissionDialog()
}
