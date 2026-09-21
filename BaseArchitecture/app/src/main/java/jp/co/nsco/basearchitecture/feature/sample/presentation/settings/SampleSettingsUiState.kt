package jp.co.nsco.basearchitecture.feature.sample.presentation.settings

import jp.co.nsco.basearchitecture.core.architecture.UiState
import jp.co.nsco.basearchitecture.core.theme.AppThemeId
import jp.co.nsco.basearchitecture.core.theme.AppThemeMode

/**
 * Sample Settings 画面の UI 状態。
 *
 * SampleSettingsUiState は、Sample Settings 画面を描画するために必要な
 * 状態を保持する。
 *
 * ■ 提供する責務
 *   読み込み中状態の保持
 *   Sample設定値の保持
 *   テーマモードの保持
 *   テーマIDの保持
 *   テーマモード選択肢一覧の保持
 *   テーマプリセット選択肢一覧の保持
 *   画面表示用エラーメッセージの保持
 *
 * ■ 設計上の意図
 *   UI は SampleSettingsUiState の値だけを参照して画面を描画する。
 *
 *   設定値、テーマ設定、選択肢の選択状態を State に保持することで、
 *   Screen は状態に応じて Switch / RadioButton / エラーメッセージを表示するだけでよい。
 *
 *   Navigation、Snackbar、Dialog 表示などの一回性副作用は、
 *   SampleSettingsUiEffect として分離し、本 State には含めない。
 *
 * @property isLoading 読み込み中かどうか。
 * @property notificationEnabled 通知設定が有効かどうか。
 * @property confirmOnLoginEnabled ログイン時確認設定が有効かどうか。
 * @property keepCacheEnabled キャッシュ保持設定が有効かどうか。
 * @property themeMode 現在選択中のテーマモード。
 * @property themeId 現在選択中のテーマID。
 * @property themeModeOptions テーマモード選択肢一覧。
 * @property themePresetOptions テーマプリセット選択肢一覧。
 * @property screenErrorMessage 画面上に表示するエラーメッセージ。
 */
data class SampleSettingsUiState(
    val isLoading: Boolean = true,
    val notificationEnabled: Boolean = true,
    val confirmOnLoginEnabled: Boolean = true,
    val keepCacheEnabled: Boolean = true,
    val themeMode: AppThemeMode = AppThemeMode.System,
    val themeId: AppThemeId = AppThemeId.Default,
    val themeModeOptions: List<ThemeModeOptionUiState> = emptyList(),
    val themePresetOptions: List<ThemePresetOptionUiState> = emptyList(),
    val screenErrorMessage: String? = null
) : UiState {

    companion object {

        /**
         * Sample Settings 画面の初期状態を生成する。
         *
         * 初期表示では Sample設定値とテーマ設定の購読前であるため、
         * Loading 状態として扱う。
         *
         * @return 初期状態。
         */
        fun initial(): SampleSettingsUiState {
            return SampleSettingsUiState(
                isLoading = true,
                notificationEnabled = true,
                confirmOnLoginEnabled = true,
                keepCacheEnabled = true,
                themeMode = AppThemeMode.System,
                themeId = AppThemeId.Default,
                themeModeOptions = emptyList(),
                themePresetOptions = emptyList(),
                screenErrorMessage = null
            )
        }
    }
}