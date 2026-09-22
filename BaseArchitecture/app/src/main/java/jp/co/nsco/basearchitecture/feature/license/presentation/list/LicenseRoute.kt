package jp.co.nsco.basearchitecture.feature.license.presentation.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import jp.co.nsco.basearchitecture.R

/** License一覧画面のRoute。 */
@Composable
fun LicenseRoute(navController: NavController) {
    LicenseScreen(
        title = stringResource(R.string.license_title),
        message = stringResource(R.string.license_placeholder)
    )
}
