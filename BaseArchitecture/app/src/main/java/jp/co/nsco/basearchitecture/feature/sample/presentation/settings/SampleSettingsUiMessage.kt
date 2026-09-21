package jp.co.nsco.basearchitecture.feature.sample.presentation.settings

import jp.co.nsco.basearchitecture.core.architecture.UiMessage
import jp.co.nsco.basearchitecture.core.theme.AppThemeId
import jp.co.nsco.basearchitecture.core.theme.AppThemeMode
import jp.co.nsco.basearchitecture.core.theme.ThemeSettings
import jp.co.nsco.basearchitecture.feature.sample.domain.SampleSettings

/**
 * Sample Settings 画面の State 更新理由。
 *
 * SampleSettingsUiMessage は、ViewModel から Reducer へ渡す
 * State 更新のためのメッセージを表す。
 *
 * ■ 提供する責務
 *   読み込み開始の通知
 *   Sample設定読み込み成功・失敗の通知
 *   各設定値更新の通知
 *   テーマ選択肢読み込みの通知
 *   テーマ設定読み込みの通知
 *   テーマ更新成功・失敗の通知
 *   保存失敗の通知
 *
 * ■ 設計上の意図
 *   Event と State 更新を直接結びつけず、
 *   Message を経由して Reducer に状態更新を依頼する。
 *
 *   これにより、ViewModel は処理の流れを制御し、
 *   Reducer は Message に基づく純粋な State 更新に集中できる。
 */
sealed interface SampleSettingsUiMessage : UiMessage {

    /**
     * 設定読み込み開始。
     */
    data object LoadStarted : SampleSettingsUiMessage

    /**
     * Sample 設定読み込み成功。
     *
     * @property settings 読み込まれた Sample 設定値。
     */
    data class LoadSucceeded(
        val settings: SampleSettings
    ) : SampleSettingsUiMessage

    /**
     * Sample 設定読み込み失敗。
     *
     * @property errorMessage 画面に表示するエラーメッセージ。
     */
    data class LoadFailed(
        val errorMessage: String
    ) : SampleSettingsUiMessage

    /**
     * 通知設定更新。
     *
     * @property enabled 更新後の通知設定。
     */
    data class NotificationUpdated(
        val enabled: Boolean
    ) : SampleSettingsUiMessage

    /**
     * ログイン時確認設定更新。
     *
     * @property enabled 更新後のログイン時確認設定。
     */
    data class ConfirmOnLoginUpdated(
        val enabled: Boolean
    ) : SampleSettingsUiMessage

    /**
     * キャッシュ保持設定更新。
     *
     * @property enabled 更新後のキャッシュ保持設定。
     */
    data class KeepCacheUpdated(
        val enabled: Boolean
    ) : SampleSettingsUiMessage

    /**
     * テーマ選択肢読み込み。
     *
     * @property modeOptions テーマモード選択肢一覧。
     * @property presetOptions テーマプリセット選択肢一覧。
     */
    data class ThemeOptionsLoaded(
        val modeOptions: List<ThemeModeOptionUiState>,
        val presetOptions: List<ThemePresetOptionUiState>
    ) : SampleSettingsUiMessage

    /**
     * テーマ設定読み込み。
     *
     * @property settings 読み込まれたテーマ設定。
     * @property modeOptions 現在選択状態を反映したテーマモード選択肢一覧。
     * @property presetOptions 現在選択状態を反映したテーマプリセット選択肢一覧。
     */
    data class ThemeSettingsLoaded(
        val settings: ThemeSettings,
        val modeOptions: List<ThemeModeOptionUiState>,
        val presetOptions: List<ThemePresetOptionUiState>
    ) : SampleSettingsUiMessage

    /**
     * テーマモード更新。
     *
     * @property themeMode 更新後のテーマモード。
     * @property modeOptions 更新後の選択状態を反映したテーマモード選択肢一覧。
     */
    data class ThemeModeUpdated(
        val themeMode: AppThemeMode,
        val modeOptions: List<ThemeModeOptionUiState>
    ) : SampleSettingsUiMessage

    /**
     * テーマプリセット更新。
     *
     * @property themeId 更新後のテーマID。
     * @property presetOptions 更新後の選択状態を反映したテーマプリセット選択肢一覧。
     */
    data class ThemePresetUpdated(
        val themeId: AppThemeId,
        val presetOptions: List<ThemePresetOptionUiState>
    ) : SampleSettingsUiMessage

    /**
     * テーマ更新失敗。
     *
     * @property errorMessage 画面に表示するエラーメッセージ。
     */
    data class ThemeUpdateFailed(
        val errorMessage: String
    ) : SampleSettingsUiMessage

    /**
     * Sample 設定保存失敗。
     *
     * @property errorMessage 画面に表示するエラーメッセージ。
     */
    data class SaveFailed(
        val errorMessage: String
    ) : SampleSettingsUiMessage
}