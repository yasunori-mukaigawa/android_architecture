package jp.co.nsco.basearchitecture.feature.versioninfo.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import jp.co.nsco.basearchitecture.R

/** VersionInfo画面のRoute。 */
@Composable
fun VersionInfoRoute(navController: NavController) {
    VersionInfoScreen(
        title = stringResource(R.string.version_info_title),
        message = stringResource(R.string.version_info_placeholder)
    )
}
