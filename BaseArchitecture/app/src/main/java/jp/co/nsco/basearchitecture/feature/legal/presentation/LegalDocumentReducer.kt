package jp.co.nsco.basearchitecture.feature.legal.presentation

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.architecture.Reducer

/**
 * 法務文書画面の State 更新を行う Reducer。
 *
 * LegalDocumentReducer は、現在の LegalDocumentUiState と
 * LegalDocumentUiMessage から、次の LegalDocumentUiState を生成する。
 *
 * ■ 提供する責務
 *   読み込み開始時の State 更新
 *   読み込み成功時の State 更新
 *   読み込み失敗時の State 更新
 *
 * ■ 設計上の意図
 *   State 更新処理を ViewModel から分離し、
 *   Message 単位で状態遷移を明確にする。
 *
 *   Reducer は純粋な State 更新のみを担当し、
 *   UseCase 呼び出し、Navigation、Dialog 表示などの副作用は扱わない。
 */
class LegalDocumentReducer @Inject constructor() :
    Reducer<LegalDocumentUiState, LegalDocumentUiMessage> {

    /**
     * 現在 State と Message から次 State を生成する。
     *
     * @param currentState 現在の画面状態。
     * @param message State 更新理由を表す Message。
     * @return Message 反映後の画面状態。
     */
    override fun reduce(
        currentState: LegalDocumentUiState,
        message: LegalDocumentUiMessage
    ): LegalDocumentUiState {
        return when (message) {
            LegalDocumentUiMessage.LoadStarted -> currentState.copy(
                screenState = LegalDocumentScreenState.Loading,
                errorMessage = null
            )

            is LegalDocumentUiMessage.LoadSucceeded -> currentState.copy(
                screenState = LegalDocumentScreenState.Loaded,
                title = message.title,
                markdown = message.markdown,
                errorMessage = null
            )

            is LegalDocumentUiMessage.LoadFailed -> currentState.copy(
                screenState = LegalDocumentScreenState.Error,
                errorMessage = message.message
            )
        }
    }
}