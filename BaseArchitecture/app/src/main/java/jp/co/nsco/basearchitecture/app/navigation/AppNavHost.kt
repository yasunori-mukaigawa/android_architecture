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
 * 本関数は、各 Feature が提供する Navigation Graph を束ね、
 * アプリとして利用可能な画面遷移構造を定義する責務を持つ。
 *
 * ■ 提供する責務
 *   アプリ全体の NavHost 生成
 *   初期表示画面の定義
 *   Feature 単位の Navigation Graph 登録
 *   共通の NavHostController 共有
 *
 * ■ 設計上の意図
 *   画面ごとの遷移定義は各 Feature 側の Graph に閉じ込める。
 *   AppNavHost はそれらを束ねるだけとし、
 *   個別画面の route 定義や遷移条件を直接持たない。
 *
 *   これにより、Feature 追加時は Graph 登録のみを行えばよく、
 *   アプリ全体の Navigation 構造を一箇所で把握できる。
 *
 * ■ 注意
 *   本関数では業務判断や遷移可否判定は行わない。
 *   画面遷移の要求は ViewModel / Effect 側で生成し、
 *   Navigation Graph は遷移先の構造定義に専念する。
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