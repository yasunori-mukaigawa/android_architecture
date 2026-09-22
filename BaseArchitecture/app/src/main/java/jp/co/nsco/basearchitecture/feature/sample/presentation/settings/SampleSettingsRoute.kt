package jp.co.nsco.basearchitecture.feature.sample.presentation.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import jp.co.nsco.basearchitecture.R

/** Sample Settings画面のRoute。 */
@Composable
fun SampleSettingsRoute(navController: NavController) {
    SampleSettingsScreen(
        title = stringResource(R.string.sample_settings_title),
        message = stringResource(R.string.sample_settings_placeholder)
    )
}
