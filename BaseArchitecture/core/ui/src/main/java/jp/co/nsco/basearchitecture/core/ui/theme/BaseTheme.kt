package jp.co.nsco.basearchitecture.core.ui.theme

import androidx.compose.runtime.Composable

/**
 * アプリ共通テーマ値へアクセスするための入口。
 *
 * BaseTheme は、MaterialTheme では提供していない
 * アプリ独自のテーマ値を参照するための object である。
 *
 * ■ 提供する責務
 *   現在テーマの spacing 取得
 *
 * ■ 設計上の意図
 *   各 Composable が LocalBaseSpacing を直接参照しないようにし、
 *   `BaseTheme.spacing` という統一された入口から spacing を取得できるようにする。
 *
 *   将来的に独自の elevation、size、animation duration などを追加する場合も、
 *   BaseTheme に入口を集約できる。
 */
object BaseTheme {

    /**
     * 現在適用中テーマの spacing。
     *
     * BaseAppTheme 配下で呼び出すことで、
     * ThemePreset に紐づく spacing を取得する。
     */
    val spacing: BaseSpacing
        @Composable get() = LocalBaseSpacing.current
}