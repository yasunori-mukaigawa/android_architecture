package jp.co.nsco.basearchitecture.feature.license.presentation.common

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import jp.co.nsco.basearchitecture.feature.license.presentation.detail.LicenseDetailRoute
import jp.co.nsco.basearchitecture.feature.license.presentation.list.LicenseRoute

/**
 * License Feature の Navigation Graph を登録する。
 *
 * 本関数は、ライセンス一覧画面とライセンス詳細画面の route 定義を行い、
 * Navigation argument を Route Composable へ受け渡す責務を持つ。
 *
 * ■ 提供する責務
 *   ライセンス一覧画面 route の登録
 *   ライセンス詳細画面 route の登録
 *   ライセンス詳細画面 argument の定義
 *   route argument から licenseId の取得
 *   Route Composable への NavController 受け渡し
 *
 * ■ 設計上の意図
 *   License Feature 固有の Navigation 定義を AppNavHost へ直接書かず、
 *   Feature 側の Graph 拡張関数として分離する。
 *
 *   AppNavHost は licenseGraph を登録するだけでよくなり、
 *   route 名、argument 名、画面接続の詳細を License Feature 内に閉じ込められる。
 *
 *   NavController は Graph / Route 層で扱い、
 *   ViewModel や Screen へ渡さない。
 *   これにより、Presentation の状態管理と Navigation 実行責務を分離する。
 *
 * @param navController 画面遷移を制御する NavController。
 */
fun NavGraphBuilder.licenseGraph(
    navController: NavController
) {
    composable(route = LicenseRoutes.List) {
        LicenseRoute(navController = navController)
    }

    composable(
        route = LicenseRoutes.Detail,
        arguments = listOf(
            navArgument(LicenseRoutes.ArgLicenseId) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val licenseId = backStackEntry.arguments
            ?.getString(LicenseRoutes.ArgLicenseId)
            .orEmpty()

        LicenseDetailRoute(
            licenseId = licenseId,
            navController = navController
        )
    }
}