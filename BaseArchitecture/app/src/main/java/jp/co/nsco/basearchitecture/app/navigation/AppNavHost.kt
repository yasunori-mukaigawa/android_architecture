package jp.co.nsco.basearchitecture.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import jp.co.nsco.basearchitecture.feature.legal.presentation.legalDocumentGraph
import jp.co.nsco.basearchitecture.feature.license.presentation.common.licenseGraph
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleRoutes
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.sampleGraph
import jp.co.nsco.basearchitecture.feature.versioninfo.presentation.versionInfoGraph

/**
 * アプリ全体の Navigation Host。
 *
 * AppNavHost は、アプリで利用する Feature Graph を束ねる責務を持つ。
 * Routeの詳細や画面固有の遷移処理は、各FeatureのGraphへ委譲する。
 *
 * ■ 設計上の意図
 *   Navigation Composeの利用箇所をアプリの入口で確認できるようにする。
 *   Feature追加時はGraphを登録し、AppNavHostへ業務処理を追加しない。
 */
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = SampleRoutes.List
    ) {
        sampleGraph(navController = navController)
        licenseGraph(navController = navController)
        legalDocumentGraph(navController = navController)
        versionInfoGraph(navController = navController)
    }
}
