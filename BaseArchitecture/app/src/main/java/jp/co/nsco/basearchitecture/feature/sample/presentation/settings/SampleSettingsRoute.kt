package jp.co.nsco.basearchitecture.feature.sample.presentation.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleBottomNavigationItem
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleRoutes

/** Sample Settings画面のRoute。 */
@Composable
fun SampleSettingsRoute(navController: NavController) {
    SampleSettingsScreen(
        title = stringResource(R.string.sample_settings_title),
        message = stringResource(R.string.sample_settings_placeholder),
        onBottomNavigationItemClick = { item ->
            when (item) {
                SampleBottomNavigationItem.History -> {
                    navController.navigate(SampleRoutes.History) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(SampleRoutes.List) { saveState = true }
                    }
                }

                SampleBottomNavigationItem.Home -> {
                    navController.navigate(SampleRoutes.List) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(SampleRoutes.List) { saveState = true }
                    }
                }

                SampleBottomNavigationItem.Settings -> Unit
            }
        }
    )
}
