package jp.co.nsco.basearchitecture.feature.sample.presentation.history

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleBottomNavigationItem
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleRoutes

/** Sample History画面のRoute。 */
@Composable
fun SampleHistoryRoute(navController: NavController) {
    SampleHistoryScreen(
        title = stringResource(R.string.sample_history_title),
        message = stringResource(R.string.sample_history_placeholder),
        onBottomNavigationItemClick = { item ->
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
