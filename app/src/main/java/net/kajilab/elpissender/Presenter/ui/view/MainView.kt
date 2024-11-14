package net.kajilab.elpissender.Presenter.ui.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.kajilab.elpissender.Presenter.ui.theme.EstimatingLocationUsingRadioWavesTheme
import net.kajilab.elpissender.Presenter.ui.view.Components.BottomNavigationBar
import net.kajilab.elpissender.R
import net.kajilab.elpissender.Service.SensingService
import net.kajilab.elpissender.ViewModel.MainViewModel
import net.kajilab.elpissender.entity.BottomNavigationBarRoute
import net.kajilab.elpissender.entity.BottomNavigationItem
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView() {
    var selectedItemIndex by remember { mutableIntStateOf(0) }
    val navController = rememberNavController()
    var topBarTitle by remember {
        mutableStateOf("TrainAlert2")
    }

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
        topBar = {
            TopAppBar(title = {
                Text(text = topBarTitle)
            })
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
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}