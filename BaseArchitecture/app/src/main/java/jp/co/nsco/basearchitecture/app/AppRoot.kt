package jp.co.nsco.basearchitecture.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import jp.co.nsco.basearchitecture.app.navigation.AppNavHost
import jp.co.nsco.basearchitecture.core.ui.theme.BaseAppTheme
import jp.co.nsco.basearchitecture.core.ui.theme.BaseThemeRegistry

/**
 * アプリ全体の Compose ルート。
 *
 * 本関数は、アプリ共通の状態を取得し、
 * Theme / Navigation などのアプリ全体に関わる UI 基盤を接続する責務を持つ。
 *
 * ■ 提供する責務
 *   AppViewModel の取得
 *   アプリ共通テーマ設定の購読
 *   ThemeRegistry の取得
 *   BaseAppTheme の適用
 *   AppNavHost の配置
 *
 * ■ 設計上の意図
 *   各 Feature 画面では、アプリ全体のテーマ適用や NavHost 生成を意識しない。
 *   AppRoot がアプリ全体の外枠を構成し、
 *   Feature 側は自身の画面表示と状態管理に集中できるようにする。
 *
 *   ThemeRegistry は Hilt 管理下の Singleton 依存であるため、
 *   Compose ルートから EntryPoint 経由で取得する。
 *   remember(context) により、同一 Context 中では再取得を抑制する。
 *
 * ■ 注意
 *   本関数では、個別画面の業務処理や遷移条件は扱わない。
 *   画面単位の状態管理は各 Feature の ViewModel に委譲し、
 *   AppRoot はアプリ共通の外枠構成に専念する。
 */
@Composable
fun AppRoot(
    appViewModel: AppViewModel = hiltViewModel()
) {
    val themeSettings by appViewModel.themeSettings.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val themeRegistry = remember(context) {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            AppRootEntryPoint::class.java
        ).baseThemeRegistry()
    }

    BaseAppTheme(
        themeSettings = themeSettings,
        themeRegistry = themeRegistry
    ) {
        AppNavHost()
    }
}

/**
 * AppRoot から SingletonComponent 管理の依存を取得するための EntryPoint。
 *
 * 本 EntryPoint は、通常の constructor injection が使いづらい
 * Compose ルート関数から Hilt 管理オブジェクトを取得するために使用する。
 *
 * ■ 提供する責務
 *   BaseThemeRegistry の公開
 *
 * ■ 設計上の意図
 *   AppRoot は Composable 関数であり、クラスとして Hilt injection できない。
 *   そのため、Hilt の EntryPoint を経由して、
 *   アプリ全体で共有する Singleton 依存を取得する。
 *
 * ■ 注意
 *   ここには AppRoot が必要とするアプリ共通依存のみを定義する。
 *   Feature 固有の依存を追加し始めると AppRoot が肥大化するため、
 *   画面単位の依存は各 Feature の ViewModel / DI Module に閉じ込める。
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppRootEntryPoint {
    fun baseThemeRegistry(): BaseThemeRegistry
}