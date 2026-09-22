package jp.co.nsco.basearchitecture.feature.versioninfo.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/** VersionInfo FeatureのPlaceholder Graphを登録する。 */
fun NavGraphBuilder.versionInfoGraph(navController: NavController) {
    composable(route = VersionInfoRoutes.VersionInfo) {
        VersionInfoRoute(navController = navController)
    }
}
