package jp.co.nsco.basearchitecture.feature.sample.presentation.settings

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.architecture.Reducer

/**
 * Sample Settings 画面の State 更新を行う Reducer。
 *
 * SampleSettingsReducer は、現在の SampleSettingsUiState と
 * SampleSettingsUiMessage から、次の SampleSettingsUiState を生成する。
 *
 * ■ 提供する責務
 *   読み込み開始時の State 更新
 *   設定読み込み成功時の State 更新
 *   設定読み込み失敗時の State 更新
 *   各設定値更新時の State 更新
 *   テーマ選択肢読み込み時の State 更新
 *   テーマ設定読み込み時の State 更新
 *   テーマ更新成功・失敗時の State 更新
 *
 * ■ 設計上の意図
 *   State 更新処理を ViewModel から分離し、
 *   Message 単位で状態遷移を明確にする。
 *
 *   Reducer は純粋な State 更新のみを担当し、
 *   UseCase 呼び出し、保存処理、操作ログ保存、Navigation、Snackbar、Dialog 表示などの
 *   副作用は扱わない。
 */
class SampleSettingsReducer @Inject constructor() :
    Reducer<SampleSettingsUiState, SampleSettingsUiMessage> {

    /**
     * 現在 State と Message から次 State を生成する。
     *
     * @param currentState 現在の画面状態。
     * @param message State 更新理由を表す Message。
     * @return Message 反映後の画面状態。
     */
    override fun reduce(
        currentState: SampleSettingsUiState,
        message: SampleSettingsUiMessage
    ): SampleSettingsUiState {
        return when (message) {
            SampleSettingsUiMessage.LoadStarted -> {
                currentState.copy(
                    isLoading = true,
                    screenErrorMessage = null
                )
            }

            is SampleSettingsUiMessage.LoadSucceeded -> {
                currentState.copy(
                    isLoading = false,
                    notificationEnabled = message.settings.notificationEnabled,
                    confirmOnLoginEnabled = message.settings.confirmOnLoginEnabled,
                    keepCacheEnabled = message.settings.keepCacheEnabled,
                    screenErrorMessage = null
                )
            }

            is SampleSettingsUiMessage.LoadFailed -> {
                currentState.copy(
                    isLoading = false,
                    screenErrorMessage = message.errorMessage
                )
            }

            is SampleSettingsUiMessage.NotificationUpdated -> {
                currentState.copy(
                    notificationEnabled = message.enabled,
                    screenErrorMessage = null
                )
            }

            is SampleSettingsUiMessage.ConfirmOnLoginUpdated -> {
                currentState.copy(
                    confirmOnLoginEnabled = message.enabled,
                    screenErrorMessage = null
                )
            }

            is SampleSettingsUiMessage.KeepCacheUpdated -> {
                currentState.copy(
                    keepCacheEnabled = message.enabled,
                    screenErrorMessage = null
                )
            }

            is SampleSettingsUiMessage.SaveFailed -> {
                currentState.copy(
                    screenErrorMessage = message.errorMessage
                )
            }

            is SampleSettingsUiMessage.ThemeOptionsLoaded -> {
                currentState.copy(
                    themeModeOptions = message.modeOptions,
                    themePresetOptions = message.presetOptions
                )
            }

            is SampleSettingsUiMessage.ThemeSettingsLoaded -> {
                currentState.copy(
                    themeMode = message.settings.themeMode,
                    themeId = message.settings.themeId,
                    themeModeOptions = message.modeOptions,
                    themePresetOptions = message.presetOptions,
                    screenErrorMessage = null
                )
            }

            is SampleSettingsUiMessage.ThemeModeUpdated -> {
                currentState.copy(
                    themeMode = message.themeMode,
                    themeModeOptions = message.modeOptions,
                    screenErrorMessage = null
                )
            }

            is SampleSettingsUiMessage.ThemePresetUpdated -> {
                currentState.copy(
                    themeId = message.themeId,
                    themePresetOptions = message.presetOptions,
                    screenErrorMessage = null
                )
            }

            is SampleSettingsUiMessage.ThemeUpdateFailed -> {
                currentState.copy(
                    screenErrorMessage = message.errorMessage
                )
            }
        }
    }
}