package jp.co.nsco.basearchitecture.feature.versioninfo.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * VersionInfo Feature の Navigation Graph を登録する。
 *
 * 本関数は、VersionInfo 画面の route と Composable を
 * Navigation Graph に追加する。
 *
 * ■ 提供する責務
 *   VersionInfo 画面 route の登録
 *   VersionInfoRoute への NavController 接続
 *
 * ■ 設計上の意図
 *   Navigation 定義を Feature 単位で分離することで、
 *   アプリ全体の NavHost 側が各画面の詳細を知らずに済むようにする。
 *
 *   NavController は Graph / Route 層に閉じ込め、
 *   Screen や ViewModel には直接渡さない。
 *
 * @param navController 画面遷移を制御する NavController。
 */
fun NavGraphBuilder.versionInfoGraph(navController: NavController) {
    composable(route = VersionInfoRoutes.VersionInfo) {
        VersionInfoRoute(navController = navController)
    }
}