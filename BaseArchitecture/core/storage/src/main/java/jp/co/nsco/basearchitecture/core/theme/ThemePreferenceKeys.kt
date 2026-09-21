package jp.co.nsco.basearchitecture.core.theme

/**
 * テーマ設定を PreferenceDataStore に保存するためのキー定義。
 *
 * 本 object は、テーマ関連の保存キーを一箇所に集約する。
 *
 * ■ 設計上の意図
 *   保存キーを各処理に文字列直書きすると、
 *   typo やキー名変更時の修正漏れが発生しやすくなる。
 *
 *   ThemePreferenceKeys に集約することで、
 *   ThemeDataStore が使用する保存キーを明確にする。
 */
object ThemePreferenceKeys {

    /**
     * AppThemeMode を保存するキー。
     */
    const val ThemeMode = "theme_mode"

    /**
     * AppThemeId を保存するキー。
     */
    const val ThemeId = "theme_id"
}