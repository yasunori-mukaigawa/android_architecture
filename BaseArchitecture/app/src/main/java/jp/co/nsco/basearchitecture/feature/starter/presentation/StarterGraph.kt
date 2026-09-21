package jp.co.nsco.basearchitecture.feature.starter.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * Starter FeatureのNavigation Graph。
 *
 * Starter Graphは、Starter画面のRouteと画面Composableを接続する責務を持つ。
 * NavControllerを必要としないため、画面追加時もNavigation操作をRouteへ閉じ込められる。
 *
 * ■ 設計上の意図
 *   AppNavHostはFeature Graphを登録するだけに留め、画面固有の定義を持たせない。
 */
fun NavGraphBuilder.starterGraph() {
    composable(StarterRoutes.Start) {
        StarterRoute()
    }
}
