package jp.co.nsco.basearchitecture.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import jp.co.nsco.basearchitecture.core.theme.ThemeSettings

/**
 * アプリ全体へ共通テーマを適用する Composable。
 *
 * 本 Composable は、ThemeSettings と BaseThemeRegistry をもとに、
 * MaterialTheme とアプリ独自の CompositionLocal を設定する責務を持つ。
 *
 * ■ 提供する責務
 *   テーマモードに応じた Light / Dark 判定
 *   テーマ ID に応じた ThemePreset 解決
 *   ColorScheme / Typography / Shapes の適用
 *   アプリ共通 Spacing の CompositionLocal 提供
 *
 * ■ 設計上の意図
 *   各画面が MaterialTheme や独自 spacing を個別に設定しないようにし、
 *   アプリ全体の見た目を単一の入口から制御する。
 *
 *   ThemeSettings は保存・選択されたテーマ状態を表し、
 *   BaseThemeRegistry はその状態に対応する実際のテーマ定義を解決する。
 *
 *   これにより、テーマ追加や色定義変更が Feature 側へ波及しにくくなる。
 *
 * @param themeSettings アプリに適用するテーマ設定。
 * @param themeRegistry テーマ ID から具体的な ThemePreset を解決する Registry。
 * @param content テーマ適用対象の Composable。
 */
@Composable
fun BaseAppTheme(
    themeSettings: ThemeSettings,
    themeRegistry: BaseThemeRegistry,
    content: @Composable () -> Unit
) {
    val useDarkTheme = rememberUseDarkTheme(themeSettings.themeMode)
    val preset = themeRegistry.resolveThemePreset(themeSettings.themeId)

    CompositionLocalProvider(LocalBaseSpacing provides preset.spacing) {
        MaterialTheme(
            colorScheme = if (useDarkTheme) {
                preset.darkColorScheme
            } else {
                preset.lightColorScheme
            },
            typography = preset.typography,
            shapes = preset.shapes,
            content = content
        )
    }
}