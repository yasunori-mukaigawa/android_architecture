package jp.co.nsco.basearchitecture.app

import androidx.compose.runtime.Composable
import jp.co.nsco.basearchitecture.app.navigation.AppNavHost
import jp.co.nsco.basearchitecture.ui.theme.BaseArchitectureTheme

/**
 * アプリ全体の Compose ルート。
 *
 * AppRoot はアプリ共通の Theme と Navigation の起点を接続する責務を持つ。
 * 画面固有の状態管理や業務処理は、各 Feature の Route / ViewModel へ委譲する。
 *
 * ■ 提供する責務
 *   アプリ共通 Theme の適用
 *   AppNavHost の配置
 *
 * ■ 設計上の意図
 *   MainActivity は Android のエントリーポイントに限定し、
 *   Compose のアプリ共通構成は AppRoot に集約する。
 *   新しい画面を追加する場合も、AppRootへ画面固有の処理を直接追加しない。
 */
@Composable
fun AppRoot() {
    BaseArchitectureTheme {
        AppNavHost()
    }
}
