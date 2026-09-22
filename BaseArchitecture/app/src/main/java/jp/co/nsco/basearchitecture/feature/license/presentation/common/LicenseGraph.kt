package jp.co.nsco.basearchitecture.feature.license.presentation.common

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jp.co.nsco.basearchitecture.feature.license.presentation.list.LicenseRoute

/** License FeatureのPlaceholder Graphを登録する。 */
fun NavGraphBuilder.licenseGraph(navController: NavController) {
    composable(route = LicenseRoutes.List) {
        LicenseRoute(navController = navController)
    }
}
