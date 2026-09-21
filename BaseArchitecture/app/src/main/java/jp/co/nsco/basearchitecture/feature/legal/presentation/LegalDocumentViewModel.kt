package jp.co.nsco.basearchitecture.feature.legal.presentation

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.architecture.BaseViewModel
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.feature.legal.application.GetLegalDocumentUseCase
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocumentType

/**
 * 法務文書画面の ViewModel。
 *
 * LegalDocumentViewModel は、画面から通知された Event を解釈し、
 * UseCase 呼び出し、Message dispatch、Effect 発行を行う。
 *
 * ■ 提供する責務
 *   画面イベントの受け取り
 *   法務文書取得 UseCase の実行
 *   読み込み状態の Message dispatch
 *   エラー表示用 Message dispatch
 *   戻る Navigation Effect の発行
 *   Dialog 表示 Effect の発行
 *   再試行対象文書種別の保持
 *
 * ■ 設計上の意図
 *   ViewModel は UI の状態更新を直接行わず、
 *   LegalDocumentUiMessage を Reducer へ dispatch する。
 *
 *   また、Navigation や Dialog などの一回性処理は
 *   LegalDocumentUiEffect として Route へ通知する。
 *
 *   これにより、State 更新と副作用を分離し、
 *   MVI の流れを保つ。
 *
 * ■ 注意
 *   latestType は RetryClicked 時に再読み込み対象を判断するための値である。
 *   OnAppear が一度も通知されていない状態で RetryClicked が来た場合は、
 *   再読み込み対象がないため何もしない。
 *
 * @param reducer 法務文書画面の State 更新を行う Reducer。
 * @param getLegalDocumentUseCase 法務文書取得 UseCase。
 * @param errorMapper AppError を画面表示文言へ変換する Mapper。
 */
@HiltViewModel
class LegalDocumentViewModel @Inject constructor(
    reducer: LegalDocumentReducer,
    private val getLegalDocumentUseCase: GetLegalDocumentUseCase,
    private val errorMapper: LegalDocumentUiErrorMapper
) : BaseViewModel<
        LegalDocumentUiState,
        LegalDocumentEvent,
        LegalDocumentUiMessage,
        LegalDocumentUiEffect
        >(
    initialState = LegalDocumentUiState(),
    reducer = reducer
) {

    /**
     * 直近で読み込み対象となった法務文書種別。
     *
     * RetryClicked 時に、同じ文書を再読み込みするために保持する。
     */
    private var latestType: LegalDocumentType? = null

    /**
     * 画面イベントを処理する。
     *
     * @param event View から通知された Event。
     */
    override suspend fun handleEvent(event: LegalDocumentEvent) {
        when (event) {
            is LegalDocumentEvent.OnAppear -> load(event.type)

            LegalDocumentEvent.BackClicked -> {
                emitEffect(LegalDocumentUiEffect.NavigateBack)
            }

            LegalDocumentEvent.RetryClicked -> {
                latestType?.let { type ->
                    load(type)
                }
            }
        }
    }

    /**
     * 指定された法務文書を読み込む。
     *
     * 読み込み開始時に Loading 状態へ更新し、
     * 成功時は本文表示状態へ、失敗時は Error 状態へ更新する。
     *
     * 失敗時は画面内エラーメッセージを State に反映し、
     * 追加で Dialog 表示 Effect を発行する。
     *
     * @param type 読み込み対象の法務文書種別。
     */
    private suspend fun load(type: LegalDocumentType) {
        latestType = type
        dispatch(LegalDocumentUiMessage.LoadStarted)

        when (val result = getLegalDocumentUseCase(type)) {
            is AppResult.Success -> {
                dispatch(
                    LegalDocumentUiMessage.LoadSucceeded(
                        title = result.value.title,
                        markdown = result.value.markdown
                    )
                )
            }

            is AppResult.Failure -> {
                val message = errorMapper.message(result.error)
                dispatch(LegalDocumentUiMessage.LoadFailed(message))
                emitEffect(
                    LegalDocumentUiEffect.ShowDialog(
                        title = errorMapper.title(),
                        message = message
                    )
                )
            }
        }
    }
}
