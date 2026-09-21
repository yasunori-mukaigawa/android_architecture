package jp.co.nsco.basearchitecture.feature.legal.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocumentType

/**
 * Legal Feature の Navigation Graph を登録する。
 *
 * 本関数は、法務文書画面への route 定義と、
 * route argument から LegalDocumentType への復元を行う。
 *
 * ■ 提供する責務
 *   法務文書画面 route の登録
 *   Navigation argument の定義
 *   route argument から LegalDocumentType への変換
 *   LegalDocumentRoute への接続
 *
 * ■ 設計上の意図
 *   Feature ごとの Navigation 定義を AppNavHost へ直接書かず、
 *   Feature 側の Graph 拡張関数として分離する。
 *
 *   これにより、AppNavHost は各 Feature Graph を登録するだけでよくなり、
 *   Legal Feature 固有の route 名、argument 名、復元処理を本ファイルに閉じ込められる。
 *
 * ■ 注意
 *   不正な route argument が渡された場合は PrivacyPolicy を既定値として扱う。
 *   不正値をエラー画面へ遷移させたい場合は、このフォールバック方針を変更する。
 *
 * @param navController 画面遷移を制御する NavController。
 */
fun NavGraphBuilder.legalDocumentGraph(
    navController: NavController
) {
    composable(
        route = LegalDocumentRoutes.Document,
        arguments = listOf(
            navArgument(LegalDocumentRoutes.ArgType) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val type = backStackEntry.arguments
            ?.getString(LegalDocumentRoutes.ArgType)
            ?.let(LegalDocumentType::fromRouteValue)
            ?: LegalDocumentType.PrivacyPolicy

        LegalDocumentRoute(
            type = type,
            navController = navController
        )
    }
}