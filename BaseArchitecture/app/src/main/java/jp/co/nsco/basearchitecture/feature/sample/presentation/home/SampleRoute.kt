package jp.co.nsco.basearchitecture.feature.sample.presentation.home

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

/**
 * Sample Home画面のRoute。
 *
 * Navigation、Drawer、共通レイアウトをScreenへ接続する責務を持つ。
 * Screenは表示値だけを受け取り、NavControllerを知らない。
 */
@Composable
fun SampleRoute(navController: NavController) {
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
                    titleResId = R.string.sample_home_title,
                    onMenuClick = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            },
            bottomBar = {
                SampleBottomNavigation(
                    selectedItem = SampleBottomNavigationItem.Home,
                    onItemClick = { item ->
                        when (item) {
                            SampleBottomNavigationItem.History -> {
                                navController.navigate(SampleRoutes.History) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(SampleRoutes.List) { saveState = true }
                                }
                            }

                            SampleBottomNavigationItem.Home -> Unit

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
            SampleScreen(
                modifier = Modifier.padding(paddingValues),
                message = stringResource(R.string.sample_home_placeholder)
            )
        }
    }
}
