package net.kajilab.elpissender.Presenter.ui.view.Home

import androidx.compose.runtime.Composable
import net.kajilab.elpissender.Presenter.ui.view.Components.WebViewComponent

@Composable
fun HomeScreen(
    topAppBarActions: ( List<@Composable () -> Unit>) -> Unit
) {
    WebViewComponent(
        topAppBarActions = topAppBarActions,
        url = "https://elpis.kajilab.dev/"
    )
}