package jp.co.nsco.basearchitecture.feature.sample.presentation.settings

import jp.co.nsco.basearchitecture.core.theme.AppThemeId
import jp.co.nsco.basearchitecture.core.theme.AppThemeMode

/**
 * テーマモード選択肢の表示状態。
 *
 * ThemeModeOptionUiState は、System / Light / Dark のテーマモードを
 * Sample Settings 画面の選択肢として表示するための ItemState。
 *
 * @property mode テーマモード。
 * @property label 表示ラベル。
 * @property selected 現在選択中かどうか。
 */
data class ThemeModeOptionUiState(
    val mode: AppThemeMode,
    val label: String,
    val selected: Boolean
)

/**
 * テーマプリセット選択肢の表示状態。
 *
 * ThemePresetOptionUiState は、利用可能なテーマプリセットを
 * Sample Settings 画面の選択肢として表示するための ItemState。
 *
 * @property themeId テーマID。
 * @property label 表示ラベル。
 * @property description 表示説明文。
 * @property selected 現在選択中かどうか。
 */
data class ThemePresetOptionUiState(
    val themeId: AppThemeId,
    val label: String,
    val description: String,
    val selected: Boolean
)