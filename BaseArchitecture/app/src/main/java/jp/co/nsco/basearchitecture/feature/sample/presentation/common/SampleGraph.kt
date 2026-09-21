package jp.co.nsco.basearchitecture.feature.sample.presentation.common

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jp.co.nsco.basearchitecture.feature.sample.presentation.history.SampleHistoryRoute
import jp.co.nsco.basearchitecture.feature.sample.presentation.home.SampleRoute
import jp.co.nsco.basearchitecture.feature.sample.presentation.settings.SampleSettingsRoute

/**
 * Sample Feature の Navigation Graph を登録する。
 *
 * 本関数は、Sample Home / History / Settings 画面の route 定義を行い、
 * 各 Route Composable と Navigation route を紐付ける責務を持つ。
 *
 * ■ 提供する責務
 *   Sample Home 画面 route の登録
 *   Sample History 画面 route の登録
 *   Sample Settings 画面 route の登録
 *   Route Composable への NavController 受け渡し
 *
 * ■ 設計上の意図
 *   Sample Feature 固有の Navigation 定義を AppNavHost へ直接書かず、
 *   Feature 側の Graph 拡張関数として分離する。
 *
 *   AppNavHost は sampleGraph を登録するだけでよくなり、
 *   route 名や画面接続の詳細を Sample Feature 内に閉じ込められる。
 *
 *   NavController は Graph / Route 層で扱い、
 *   ViewModel や Screen へ渡さない。
 *   これにより、画面状態管理と Navigation 実行責務を分離する。
 *
 * @param navController 画面遷移を制御する NavController。
 */
fun NavGraphBuilder.sampleGraph(
    navController: NavController
) {
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