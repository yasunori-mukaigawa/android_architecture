package jp.co.nsco.basearchitecture.feature.sample.presentation.history

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.architecture.BaseViewModel
import jp.co.nsco.basearchitecture.core.result.AppResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val LogTag = "SampleHistory"

/**
 * Sample History 画面の ViewModel。
 *
 * SampleHistoryViewModel は、画面から通知された Event を解釈し、
 * 操作ログ購読、表示用Item変換、Message dispatch、Effect 発行を行う。
 *
 * ■ 提供する責務
 *   画面イベントの受け取り
 *   操作ログ一覧の購読開始
 *   OperationLog から ItemUiState への変換
 *   読み込み状態の Message dispatch
 *   検索文字列更新の Message dispatch
 *   フィルタ更新の Message dispatch
 *   画面遷移 Effect の発行
 *   Dialog 表示 Effect の発行
 *
 * ■ 設計上の意図
 *   ViewModel は UI の状態更新を直接行わず、
 *   SampleHistoryUiMessage を Reducer へ dispatch する。
 *
 *   操作ログの取得は UseCase に委譲し、
 *   画面表示用の変換は Presentation 側の Mapper に委譲する。
 *
 *   Navigation や Dialog などの一回性処理は
 *   SampleHistoryUiEffect として Route へ通知する。
 *
 *   これにより、State 更新、データ取得、表示変換、副作用を分離し、
 *   MVI の流れを保つ。
 *
 * ■ 注意
 *   Initialize が複数回通知された場合でも購読が重複しないように、
 *   既存の observeJob を cancel してから再購読する。
 *
 * @param reducer Sample History 画面の State 更新を行う Reducer。
 * @param useCaseFacade Sample History 画面で利用する UseCase 群。
 * @param presentationFacade Sample History 画面で利用する Presentation 補助部品群。
 */
@HiltViewModel
class SampleHistoryViewModel @Inject constructor(
    reducer: SampleHistoryReducer,
    private val useCaseFacade: SampleHistoryUseCaseFacade,
    private val presentationFacade: SampleHistoryPresentationFacade
) : BaseViewModel<
        SampleHistoryUiState,
        SampleHistoryUiEvent,
        SampleHistoryUiMessage,
        SampleHistoryUiEffect
        >(
    initialState = SampleHistoryUiState.initial(),
    reducer = reducer
) {

    /**
     * 操作ログ購読 Job。
     *
     * Initialize が複数回呼ばれた場合に購読が重複しないよう、
     * 新しい購読開始前に既存 Job を cancel する。
     */
    private var observeJob: Job? = null

    /**
     * 画面イベントを処理する。
     *
     * @param event View から通知された Event。
     */
    override suspend fun handleEvent(event: SampleHistoryUiEvent) {
        when (event) {
            SampleHistoryUiEvent.Initialize -> observeHistory()

            is SampleHistoryUiEvent.SearchQueryChanged -> {
                dispatch(SampleHistoryUiMessage.SearchQueryUpdated(event.query))
            }

            is SampleHistoryUiEvent.FilterSelected -> {
                dispatch(SampleHistoryUiMessage.FilterUpdated(event.filter))
            }

            SampleHistoryUiEvent.HomeClicked -> {
                emitEffect(SampleHistoryUiEffect.NavigateHome)
            }

            SampleHistoryUiEvent.SettingsClicked -> {
                emitEffect(SampleHistoryUiEffect.NavigateSettings)
            }
        }
    }

    /**
     * 操作ログ一覧の購読を開始する。
     *
     * 購読開始時に Loading 状態へ更新し、
     * 成功時は OperationLog を SampleHistoryItemUiState へ変換して State に反映する。
     *
     * 失敗時は画面内エラーメッセージを State に反映し、
     * 追加で Dialog 表示 Effect を発行する。
     */
    private fun observeHistory() {
        observeJob?.cancel()
        presentationFacade.logger.debug(
            message = "History observation started",
            tag = LogTag
        )
        dispatch(SampleHistoryUiMessage.LoadStarted)

        observeJob = viewModelScope.launch {
            useCaseFacade.observeOperationLogs().collect { result ->
                when (result) {
                    is AppResult.Success -> {
                        presentationFacade.logger.debug(
                            message = "History loaded: count=${result.value.size}",
                            tag = LogTag
                        )
                        dispatch(
                            SampleHistoryUiMessage.LoadSucceeded(
                                items = result.value.map { log ->
                                    presentationFacade.itemMapper.toItemUiState(log)
                                }
                            )
                        )
                    }

                    is AppResult.Failure -> {
                        presentationFacade.logger.error(
                            message = "History load failed: code=${result.error.code}",
                            throwable = result.error.cause,
                            tag = LogTag
                        )
                        val message = presentationFacade.errorMapper.toDisplayMessage(result.error)
                        dispatch(SampleHistoryUiMessage.LoadFailed(message))
                        emitEffect(
                            SampleHistoryUiEffect.ShowDialog(
                                title = presentationFacade.stringProvider.getString(
                                    R.string.sample_history_error_title
                                ),
                                message = message
                            )
                        )
                    }
                }
            }
        }
    }
}
