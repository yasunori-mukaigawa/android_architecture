package jp.co.nsco.basearchitecture.feature.sample.presentation.home

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.architecture.Reducer

/**
 * Sample Home 画面の State 更新を行う Reducer。
 *
 * SampleReducer は、現在の SampleUiState と SampleUiMessage から、
 * 次の SampleUiState を生成する。
 *
 * ■ 提供する責務
 *   読み込み開始時の State 更新
 *   Homeヘッダー更新時の State 更新
 *   読み込み成功時の State 更新
 *   読み込み失敗時の State 更新
 *
 * ■ 設計上の意図
 *   State 更新処理を ViewModel から分離し、
 *   Message 単位で状態遷移を明確にする。
 *
 *   Reducer は純粋な State 更新のみを担当し、
 *   UseCase 呼び出し、操作ログ保存、Dialog 表示などの副作用は扱わない。
 */
class SampleReducer @Inject constructor() : Reducer<SampleUiState, SampleUiMessage> {

    /**
     * 現在 State と Message から次 State を生成する。
     *
     * @param currentState 現在の画面状態。
     * @param message State 更新理由を表す Message。
     * @return Message 反映後の画面状態。
     */
    override fun reduce(
        currentState: SampleUiState,
        message: SampleUiMessage
    ): SampleUiState {
        return when (message) {
            SampleUiMessage.LoadStarted -> {
                currentState.copy(isLoading = true)
            }

            is SampleUiMessage.HomeHeaderUpdated -> {
                currentState.copy(
                    greetingMessage = message.greetingMessage,
                    dateText = message.dateText
                )
            }

            SampleUiMessage.LoadSucceeded -> {
                currentState.copy(isLoading = false)
            }

            is SampleUiMessage.LoadFailed -> {
                currentState.copy(isLoading = false)
            }
        }
    }
}