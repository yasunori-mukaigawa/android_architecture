package jp.co.nsco.basearchitecture.feature.sample.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.navigation.NavController
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleBottomNavigationItem
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleDrawerContent
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleRoutes
import kotlinx.coroutines.launch

/**
 * Sample Home画面のRoute。
 *
 * 初期段階では状態管理やNavigation接続を持たず、Resourceから取得した表示値を
 * StatelessなScreenへ渡す責務だけを持つ。
 */
@Composable
fun SampleRoute(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

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
        SampleScreen(
            titleResId = R.string.sample_home_title,
            message = stringResource(R.string.sample_home_placeholder),
            onMenuClick = {
                coroutineScope.launch { drawerState.open() }
            },
            onBottomNavigationItemClick = { item ->
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
}
