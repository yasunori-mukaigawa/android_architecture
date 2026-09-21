package jp.co.nsco.basearchitecture.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import jp.co.nsco.basearchitecture.core.theme.AppThemeMode

/**
 * テーマモードから現在 Dark テーマを使用するか判定する。
 *
 * System モードの場合は OS の Dark テーマ設定に追従し、
 * Light / Dark モードの場合はアプリ設定を優先する。
 *
 * @param themeMode アプリのテーマモード設定。
 * @return Dark テーマを使用する場合は true。
 */
@Composable
fun rememberUseDarkTheme(themeMode: AppThemeMode): Boolean {
    return useDarkThemeForMode(
        themeMode = themeMode,
        systemInDarkTheme = isSystemInDarkTheme()
    )
}

/**
 * テーマモードとシステム Dark テーマ状態から、
 * アプリで Dark テーマを使用するか判定する。
 *
 * ■ 設計上の意図
 *   Compose の isSystemInDarkTheme に直接依存する処理と、
 *   純粋な判定ロジックを分離する。
 *
 *   useDarkThemeForMode は pure function として定義しているため、
 *   Unit テストで System / Light / Dark の分岐を検証しやすい。
 *
 * @param themeMode アプリのテーマモード設定。
 * @param systemInDarkTheme OS が Dark テーマ状態の場合は true。
 * @return アプリで Dark テーマを使用する場合は true。
 */
fun useDarkThemeForMode(
    themeMode: AppThemeMode,
    systemInDarkTheme: Boolean
): Boolean {
    return when (themeMode) {
        AppThemeMode.System -> systemInDarkTheme
        AppThemeMode.Light -> false
        AppThemeMode.Dark -> true
    }
}