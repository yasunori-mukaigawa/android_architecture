package jp.co.nsco.basearchitecture.feature.sample.presentation.settings

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.architecture.BaseViewModel
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.core.theme.AppThemeId
import jp.co.nsco.basearchitecture.core.theme.AppThemeMode
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogResult
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogType
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val LogTag = "SampleSettings"

/**
 * Sample Settings 画面の ViewModel。
 *
 * SampleSettingsViewModel は、画面から通知された Event を解釈し、
 * Sample設定購読、テーマ設定購読、設定更新、操作ログ保存、
 * Message dispatch、Effect 発行を行う。
 *
 * ■ 提供する責務
 *   画面イベントの受け取り
 *   Sample設定値の購読開始
 *   テーマ設定の購読開始
 *   テーマ選択肢の生成依頼
 *   Sample設定値の更新
 *   テーマ設定の更新
 *   設定変更成功時の操作ログ保存
 *   設定変更失敗時のエラーログ保存
 *   State 更新用 Message の dispatch
 *   Navigation / Snackbar / Dialog 用 Effect の発行
 *
 * ■ 設計上の意図
 *   ViewModel は UI の状態更新を直接行わず、
 *   SampleSettingsUiMessage を Reducer へ dispatch する。
 *
 *   設定値の取得・保存は UseCase に委譲し、
 *   テーマ選択肢の表示変換やエラー表示文言の生成は Presentation 側の補助部品に委譲する。
 *
 *   Navigation、Snackbar、Dialog などの一回性処理は
 *   SampleSettingsUiEffect として Route へ通知する。
 *
 *   これにより、State 更新、設定保存、表示変換、副作用を分離し、
 *   MVI の流れを保つ。
 *
 * ■ 注意
 *   設定変更時は、先に State を楽観的に更新してから保存処理を行う。
 *   保存に失敗した場合は画面内エラーメッセージと Dialog で通知する。
 *
 *   操作ログ保存は補助的な記録処理であり、
 *   操作ログ保存の成否によって設定画面の State は変更しない。
 *
 * @param reducer Sample Settings 画面の State 更新を行う Reducer。
 * @param useCaseFacade Sample Settings 画面で利用する UseCase 群。
 * @param presentationFacade Sample Settings 画面で利用する Presentation 補助部品群。
 */
@HiltViewModel
class SampleSettingsViewModel @Inject constructor(
    reducer: SampleSettingsReducer,
    private val useCaseFacade: SampleSettingsUseCaseFacade,
    private val presentationFacade: SampleSettingsPresentationFacade
) : BaseViewModel<
        SampleSettingsUiState,
        SampleSettingsUiEvent,
        SampleSettingsUiMessage,
        SampleSettingsUiEffect
        >(
    initialState = SampleSettingsUiState.initial(),
    reducer = reducer
) {

    /**
     * Sample設定値購読 Job。
     *
     * Initialize が複数回通知された場合に購読が重複しないよう、
     * 新しい購読開始前に既存 Job を cancel する。
     */
    private var observeJob: Job? = null

    /**
     * テーマ設定購読 Job。
     *
     * Sample設定値とは別の Flow を購読するため、個別の Job として管理する。
     */
    private var observeThemeJob: Job? = null

    /**
     * 画面イベントを処理する。
     *
     * View から通知された Event を解釈し、
     * 必要な UseCase 呼び出し、Message dispatch、Effect 発行へ振り分ける。
     *
     * @param event View から通知された Event。
     */
    override suspend fun handleEvent(event: SampleSettingsUiEvent) {
        when (event) {
            SampleSettingsUiEvent.Initialize -> observeSettings()

            is SampleSettingsUiEvent.NotificationChanged -> {
                updateNotification(event.enabled)
            }

            is SampleSettingsUiEvent.ConfirmOnLoginChanged -> {
                updateConfirmOnLogin(event.enabled)
            }

            is SampleSettingsUiEvent.KeepCacheChanged -> {
                updateKeepCache(event.enabled)
            }

            is SampleSettingsUiEvent.ThemeModeChanged -> {
                updateThemeMode(event.themeMode)
            }

            is SampleSettingsUiEvent.ThemePresetChanged -> {
                updateThemeId(event.themeId)
            }

            SampleSettingsUiEvent.HomeClicked -> {
                emitEffect(SampleSettingsUiEffect.NavigateHome)
            }
        }
    }

    /**
     * Sample設定値とテーマ設定の購読を開始する。
     *
     * 既存の購読 Job を cancel してから再購読することで、
     * Initialize が複数回呼ばれても Flow の購読が重複しないようにする。
     *
     * Sample設定値とテーマ設定は別々の保存領域・Flowで管理されるため、
     * それぞれ個別に購読する。
     */
    private fun observeSettings() {
        observeJob?.cancel()
        observeThemeJob?.cancel()
        presentationFacade.logger.debug(
            message = "Settings observation started",
            tag = LogTag
        )
        dispatch(SampleSettingsUiMessage.LoadStarted)

        val themeIds = useCaseFacade.getAvailableThemePresets()
        dispatch(
            SampleSettingsUiMessage.ThemeOptionsLoaded(
                modeOptions = presentationFacade.themeOptionMapper.themeModeOptions(
                    currentState().themeMode
                ),
                presetOptions = presentationFacade.themeOptionMapper.themePresetOptions(
                    availableThemeIds = themeIds,
                    selected = currentState().themeId
                )
            )
        )

        observeJob = viewModelScope.launch {
            useCaseFacade.observeSampleSettings().collect { result ->
                when (result) {
                    is AppResult.Success -> {
                        presentationFacade.logger.debug(
                            message = "Sample settings loaded",
                            tag = LogTag
                        )
                        dispatch(SampleSettingsUiMessage.LoadSucceeded(result.value))
                    }

                    is AppResult.Failure -> {
                        presentationFacade.logger.error(
                            message = "Sample settings load failed: code=${result.error.code}",
                            throwable = result.error.cause,
                            tag = LogTag
                        )
                        val message = presentationFacade.errorMapper.toDisplayMessage(result.error)
                        dispatch(SampleSettingsUiMessage.LoadFailed(message))
                        emitEffect(
                            SampleSettingsUiEffect.ShowDialog(
                                title = presentationFacade.stringProvider.getString(
                                    R.string.sample_settings_error_title
                                ),
                                message = message
                            )
                        )
                    }
                }
            }
        }

        observeThemeJob = viewModelScope.launch {
            useCaseFacade.observeThemeSettings().collect { settings ->
                dispatch(
                    SampleSettingsUiMessage.ThemeSettingsLoaded(
                        settings = settings,
                        modeOptions = presentationFacade.themeOptionMapper.themeModeOptions(
                            settings.themeMode
                        ),
                        presetOptions = presentationFacade.themeOptionMapper.themePresetOptions(
                            availableThemeIds = themeIds,
                            selected = settings.themeId
                        )
                    )
                )
            }
        }
    }

    /**
     * 通知設定を更新する。
     *
     * 画面操作への反応を早くするため、先に State を更新してから保存処理を行う。
     *
     * @param enabled 変更後の通知設定。
     */
    private suspend fun updateNotification(enabled: Boolean) {
        dispatch(SampleSettingsUiMessage.NotificationUpdated(enabled))
        handleSaveResult(
            result = useCaseFacade.updateNotificationEnabled(enabled),
            successSummary = presentationFacade.operationLogMessageFactory.notificationSummary(enabled)
        )
    }

    /**
     * ログイン時確認設定を更新する。
     *
     * @param enabled 変更後のログイン時確認設定。
     */
    private suspend fun updateConfirmOnLogin(enabled: Boolean) {
        dispatch(SampleSettingsUiMessage.ConfirmOnLoginUpdated(enabled))
        handleSaveResult(
            result = useCaseFacade.updateConfirmOnLoginEnabled(enabled),
            successSummary = presentationFacade.operationLogMessageFactory.confirmOnLoginSummary(enabled)
        )
    }

    /**
     * キャッシュ保持設定を更新する。
     *
     * @param enabled 変更後のキャッシュ保持設定。
     */
    private suspend fun updateKeepCache(enabled: Boolean) {
        dispatch(SampleSettingsUiMessage.KeepCacheUpdated(enabled))
        handleSaveResult(
            result = useCaseFacade.updateKeepCacheEnabled(enabled),
            successSummary = presentationFacade.operationLogMessageFactory.keepCacheSummary(enabled)
        )
    }

    /**
     * テーマモードを更新する。
     *
     * テーマは画面全体の見た目に即時反映されるため、
     * State 側も先に更新してから永続化する。
     *
     * @param themeMode 変更後のテーマモード。
     */
    private suspend fun updateThemeMode(themeMode: AppThemeMode) {
        dispatch(
            SampleSettingsUiMessage.ThemeModeUpdated(
                themeMode = themeMode,
                modeOptions = presentationFacade.themeOptionMapper.themeModeOptions(themeMode)
            )
        )
        handleThemeSaveResult(useCaseFacade.updateThemeMode(themeMode))
    }

    /**
     * テーマプリセットを更新する。
     *
     * @param themeId 変更後のテーマID。
     */
    private suspend fun updateThemeId(themeId: AppThemeId) {
        dispatch(
            SampleSettingsUiMessage.ThemePresetUpdated(
                themeId = themeId,
                presetOptions = presentationFacade.themeOptionMapper.themePresetOptions(
                    availableThemeIds = useCaseFacade.getAvailableThemePresets(),
                    selected = themeId
                )
            )
        )
        handleThemeSaveResult(useCaseFacade.updateThemeId(themeId))
    }

    /**
     * テーマ設定保存結果を処理する。
     *
     * 成功時は Snackbar を表示し、失敗時は画面内エラー表示と Dialog 表示を行う。
     *
     * @param result テーマ設定保存結果。
     */
    private suspend fun handleThemeSaveResult(result: AppResult<Unit>) {
        when (result) {
            is AppResult.Success -> {
                presentationFacade.logger.debug(
                    message = "Theme setting saved",
                    tag = LogTag
                )
                emitEffect(
                    SampleSettingsUiEffect.ShowSnackbar(
                        presentationFacade.stringProvider.getString(
                            R.string.sample_settings_save_success
                        )
                    )
                )
            }

            is AppResult.Failure -> {
                presentationFacade.logger.error(
                    message = "Theme setting save failed: code=${result.error.code}",
                    throwable = result.error.cause,
                    tag = LogTag
                )
                val message = presentationFacade.errorMapper.toDisplayMessage(result.error)
                dispatch(SampleSettingsUiMessage.ThemeUpdateFailed(message))
                emitEffect(
                    SampleSettingsUiEffect.ShowDialog(
                        title = presentationFacade.stringProvider.getString(
                            R.string.sample_settings_error_title
                        ),
                        message = message
                    )
                )
            }
        }
    }

    /**
     * Sample設定保存結果を処理する。
     *
     * 成功時は設定変更ログを保存し、Snackbar を表示する。
     * 失敗時はエラーログを保存し、画面内エラー表示と Dialog 表示を行う。
     *
     * @param result 設定保存結果。
     * @param successSummary 設定変更ログに保存する概要文言。
     */
    private suspend fun handleSaveResult(
        result: AppResult<Unit>,
        successSummary: String
    ) {
        when (result) {
            is AppResult.Success -> {
                recordSettingChangedLog(summary = successSummary)
                presentationFacade.logger.debug(
                    message = "Sample setting saved",
                    tag = LogTag
                )
                emitEffect(
                    SampleSettingsUiEffect.ShowSnackbar(
                        message = presentationFacade.stringProvider.getString(
                            R.string.sample_settings_save_success
                        )
                    )
                )
            }

            is AppResult.Failure -> {
                presentationFacade.logger.error(
                    message = "Sample setting save failed: code=${result.error.code}",
                    throwable = result.error.cause,
                    tag = LogTag
                )
                val message = presentationFacade.errorMapper.toDisplayMessage(result.error)
                recordErrorLog(
                    summary = presentationFacade.operationLogMessageFactory.settingsErrorSummary()
                )
                dispatch(SampleSettingsUiMessage.SaveFailed(message))
                emitEffect(
                    SampleSettingsUiEffect.ShowDialog(
                        title = presentationFacade.stringProvider.getString(
                            R.string.sample_settings_error_title
                        ),
                        message = message
                    )
                )
            }
        }
    }

    /**
     * 設定変更成功ログを保存する。
     *
     * 操作ログ保存は補助的な記録処理のため、
     * 保存結果によって Settings 画面の State は変更しない。
     *
     * @param summary 操作ログ概要。
     */
    private suspend fun recordSettingChangedLog(summary: String) {
        when (val result = useCaseFacade.saveOperationLog(
            type = OperationLogType.Setting,
            result = OperationLogResult.Success,
            title = presentationFacade.operationLogMessageFactory.settingChangedTitle(),
            summary = summary
        )) {
            is AppResult.Success -> {
                presentationFacade.logger.debug(
                    message = "Setting change operation log saved",
                    tag = LogTag
                )
            }

            is AppResult.Failure -> {
                presentationFacade.logger.warn(
                    message = "Setting change operation log save failed: code=${result.error.code}",
                    tag = LogTag
                )
            }
        }
    }

    /**
     * 設定変更失敗ログを保存する。
     *
     * @param summary 操作ログ概要。
     */
    private suspend fun recordErrorLog(summary: String) {
        when (val result = useCaseFacade.saveOperationLog(
            type = OperationLogType.Error,
            result = OperationLogResult.Failure,
            title = presentationFacade.operationLogMessageFactory.errorTitle(),
            summary = summary
        )) {
            is AppResult.Success -> {
                presentationFacade.logger.debug(
                    message = "Setting error operation log saved",
                    tag = LogTag
                )
            }

            is AppResult.Failure -> {
                presentationFacade.logger.warn(
                    message = "Setting error operation log save failed: code=${result.error.code}",
                    tag = LogTag
                )
            }
        }
    }
}
