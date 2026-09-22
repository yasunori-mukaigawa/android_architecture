package jp.co.nsco.basearchitecture.feature.sample.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleBottomNavigationItem
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleRoutes

/**
 * Sample Home画面のRoute。
 *
 * 初期段階では状態管理やNavigation接続を持たず、Resourceから取得した表示値を
 * StatelessなScreenへ渡す責務だけを持つ。
 */
@Composable
fun SampleRoute(navController: NavController) {
    SampleScreen(
        title = stringResource(R.string.sample_home_title),
        message = stringResource(R.string.sample_home_placeholder),
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
