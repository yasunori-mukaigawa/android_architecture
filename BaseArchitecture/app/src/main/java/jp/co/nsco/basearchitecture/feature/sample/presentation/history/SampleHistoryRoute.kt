package jp.co.nsco.basearchitecture.feature.sample.presentation.history

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleBottomNavigation
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleBottomNavigationItem
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleDrawerContent
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleRoutes
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleTopBar
import kotlinx.coroutines.launch

/** Sample History画面のRoute。 */
@Composable
fun SampleHistoryRoute(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SampleDrawerContent(
                onItemClick = { item ->
                    coroutineScope.launch { drawerState.close() }
                    item.destinationRoute?.let { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                SampleTopBar(
                    titleResId = R.string.sample_history_title,
                    onMenuClick = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            },
            bottomBar = {
                SampleBottomNavigation(
                    selectedItem = SampleBottomNavigationItem.History,
                    onItemClick = { item ->
                        when (item) {
                            SampleBottomNavigationItem.History -> Unit

                            SampleBottomNavigationItem.Home -> {
                                navController.navigate(SampleRoutes.List) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(SampleRoutes.List) { saveState = true }
                                }
                            }

                            SampleBottomNavigationItem.Settings -> {
                                navController.navigate(SampleRoutes.Settings) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(SampleRoutes.List) { saveState = true }
                                }
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            SampleHistoryScreen(
                modifier = Modifier.padding(paddingValues),
                message = stringResource(R.string.sample_history_placeholder)
            )
        }
    }
}
