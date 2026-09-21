package jp.co.nsco.basearchitecture.feature.sample.presentation.home

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.architecture.BaseViewModel
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogResult
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogType

private const val LogTag = "SampleHome"

/**
 * Sample Home 画面の ViewModel。
 *
 * SampleViewModel は、画面から通知された Event を解釈し、
 * Homeヘッダー取得、表示値変換、操作ログ保存、
 * Message dispatch、Effect 発行を行う。
 *
 * ■ 提供する責務
 *   画面イベントの受け取り
 *   Sample Home ヘッダー情報の取得
 *   Domain Model から表示文字列への変換
 *   読み込み状態の Message dispatch
 *   エラー発生時の Message dispatch
 *   Dialog 表示 Effect の発行
 *   Home表示操作ログの保存
 *
 * ■ 設計上の意図
 *   ViewModel は UI の状態更新を直接行わず、
 *   SampleUiMessage を Reducer へ dispatch する。
 *
 *   Homeヘッダー取得は UseCase に委譲し、
 *   挨拶文言や日付の表示用変換は Presentation 側の Formatter に委譲する。
 *
 *   エラー表示文言の生成は Presentation 側の ErrorMapper に委譲する。
 *
 *   Dialog などの一回性処理は SampleUiEffect として Route へ通知する。
 *
 *   これにより、State 更新、データ取得、表示変換、副作用を分離し、
 *   MVI の流れを保つ。
 *
 * ■ 注意
 *   Home画面表示ログの保存に失敗しても、画面表示自体は継続する。
 *   操作ログ保存は画面表示の主処理ではなく、補助的な記録処理として扱う。
 *
 * @param reducer Sample Home 画面の State 更新を行う Reducer。
 * @param useCaseFacade Sample Home 画面で利用する UseCase 群。
 * @param presentationFacade Sample Home 画面で利用する Presentation 補助部品群。
 */
@HiltViewModel
class SampleViewModel @Inject constructor(
    reducer: SampleReducer,
    private val useCaseFacade: SampleHomeUseCaseFacade,
    private val presentationFacade: SampleHomePresentationFacade
) : BaseViewModel<SampleUiState, SampleUiEvent, SampleUiMessage, SampleUiEffect>(
    initialState = SampleUiState.initial(),
    reducer = reducer
) {

    /**
     * 画面イベントを処理する。
     *
     * @param event View から通知された Event。
     */
    override suspend fun handleEvent(event: SampleUiEvent) {
        when (event) {
            SampleUiEvent.Initialize -> handleInitialize()
        }
    }

    /**
     * Sample Home 画面の初期化処理を行う。
     *
     * Homeヘッダー情報を取得し、成功時は表示用文字列へ変換して State に反映する。
     * 失敗時は UiError へ変換し、State 更新と Dialog 表示 Effect を発行する。
     */
    private suspend fun handleInitialize() {
        presentationFacade.logger.debug(
            message = "Home initialization started",
            tag = LogTag
        )
        dispatch(SampleUiMessage.LoadStarted)

        when (val headerResult = useCaseFacade.getSampleHomeHeader()) {
            is AppResult.Success -> {
                presentationFacade.logger.debug(
                    message = "Home header loaded",
                    tag = LogTag
                )
                dispatch(
                    SampleUiMessage.HomeHeaderUpdated(
                        greetingMessage = presentationFacade.homeHeaderFormatter.formatGreeting(
                            headerResult.value.greetingType
                        ),
                        dateText = presentationFacade.homeHeaderFormatter.formatDate(
                            headerResult.value.date
                        )
                    )
                )
                dispatch(SampleUiMessage.LoadSucceeded)
                recordHomeDisplayedLog()
            }

            is AppResult.Failure -> {
                presentationFacade.logger.error(
                    message = "Home header load failed: code=${headerResult.error.code}",
                    throwable = headerResult.error.cause,
                    tag = LogTag
                )
                val uiError = presentationFacade.errorMapper.toUiError(headerResult.error)
                dispatch(SampleUiMessage.LoadFailed(uiError))
                emitEffect(
                    SampleUiEffect.ShowDialog(
                        title = uiError.title,
                        message = uiError.message
                    )
                )
            }
        }
    }

    /**
     * Sample Home 画面表示ログを保存する。
     *
     * 画面表示ログは補助的な記録であり、
     * 保存結果によって Sample Home の画面状態は変更しない。
     */
    private suspend fun recordHomeDisplayedLog() {
        when (val result = useCaseFacade.saveOperationLog(
            type = OperationLogType.Screen,
            result = OperationLogResult.Info,
            title = presentationFacade.operationLogMessageFactory.homeDisplayedTitle(),
            summary = presentationFacade.operationLogMessageFactory.homeDisplayedSummary()
        )) {
            is AppResult.Success -> {
                presentationFacade.logger.debug(
                    message = "Home display operation log saved",
                    tag = LogTag
                )
            }

            is AppResult.Failure -> {
                presentationFacade.logger.warn(
                    message = "Home display operation log save failed: code=${result.error.code}",
                    tag = LogTag
                )
            }
        }
    }
}
