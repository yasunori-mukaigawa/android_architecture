package jp.co.nsco.basearchitecture.ui.theme

import androidx.compose.runtime.Composable
import jp.co.nsco.basearchitecture.core.theme.ThemeSettings
import jp.co.nsco.basearchitecture.core.ui.theme.BaseAppTheme
import jp.co.nsco.basearchitecture.core.ui.theme.DefaultBaseThemeRegistry

/**
 * Templateアプリ用のテーマ。
 *
 * BaseArchitectureTheme は、Core 層で定義した BaseAppTheme に
 * Templateの初期テーマ設定とテーマレジストリを渡す。
 *
 * ■ 提供する責務
 *   アプリ全体の MaterialTheme 適用
 *   Template用 ThemeSettings の指定
 *   利用可能テーマレジストリの指定
 *
 * ■ 設計上の意図
 *   Core 層の BaseAppTheme を直接各画面で利用せず、
 *   アプリ側の Theme Composable として一段ラップする。
 *
 *   これにより、実案件では ThemeSettings の取得元を DataStore や ViewModel に変更しても、
 *   各 Screen / Preview 側の呼び出し方を大きく変えずに済む。
 *
 * @param content テーマを適用する Composable。
 */
@Composable
fun BaseArchitectureTheme(
    content: @Composable () -> Unit
) {
    BaseAppTheme(
        themeSettings = ThemeSettings(),
        themeRegistry = DefaultBaseThemeRegistry(),
        content = content
    )
}
