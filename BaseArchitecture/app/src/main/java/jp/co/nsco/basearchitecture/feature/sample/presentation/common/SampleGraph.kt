package jp.co.nsco.basearchitecture.feature.sample.presentation.common

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jp.co.nsco.basearchitecture.feature.sample.presentation.history.SampleHistoryRoute
import jp.co.nsco.basearchitecture.feature.sample.presentation.home.SampleRoute
import jp.co.nsco.basearchitecture.feature.sample.presentation.settings.SampleSettingsRoute

/**
 * Sample FeatureのNavigation Graphを登録する。
 *
 * Sample Feature内のRouteとComposableの接続をここへ集約し、AppNavHostには
 * Feature Graphの登録だけを残す。
 */
fun NavGraphBuilder.sampleGraph(navController: NavController) {
    composable(route = SampleRoutes.List) {
        SampleRoute(navController = navController)
    }

    composable(route = SampleRoutes.History) {
        SampleHistoryRoute(navController = navController)
    }

    composable(route = SampleRoutes.Settings) {
        SampleSettingsRoute(navController = navController)
    }
}
