package jp.co.nsco.basearchitecture.core.theme

/**
 * アプリ全体に適用するテーマ設定。
 *
 * 本クラスは、明暗テーマの適用方針と、
 * アプリ独自テーマ ID をまとめて保持する。
 *
 * ■ 提供する責務
 *   テーマモードの保持
 *   テーマ ID の保持
 *
 * ■ 設計上の意図
 *   OS の明暗設定に関する AppThemeMode と、
 *   アプリ独自のテーマ種別である AppThemeId を分離して保持する。
 *
 *   これにより、Light / Dark の切り替えと、
 *   Dashboard / Default などの色・表現テーマ切り替えを独立して扱える。
 *
 * @property themeMode System / Light / Dark の適用方針。
 * @property themeId アプリ独自テーマを識別する ID。
 */
data class ThemeSettings(
    val themeMode: AppThemeMode = AppThemeMode.System,
    val themeId: AppThemeId = AppThemeId.Default
)