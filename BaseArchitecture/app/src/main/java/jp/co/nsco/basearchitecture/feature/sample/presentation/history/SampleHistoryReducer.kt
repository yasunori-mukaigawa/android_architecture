package jp.co.nsco.basearchitecture.feature.sample.presentation.history

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.architecture.Reducer

/**
 * Sample History 画面の State 更新を行う Reducer。
 *
 * SampleHistoryReducer は、現在の SampleHistoryUiState と
 * SampleHistoryUiMessage から、次の SampleHistoryUiState を生成する。
 *
 * ■ 提供する責務
 *   読み込み開始時の State 更新
 *   読み込み成功時の State 更新
 *   読み込み失敗時の State 更新
 *   検索文字列更新時の State 更新
 *   フィルタ更新時の State 更新
 *
 * ■ 設計上の意図
 *   State 更新処理を ViewModel から分離し、
 *   Message 単位で状態遷移を明確にする。
 *
 *   Reducer は純粋な State 更新のみを担当し、
 *   UseCase 呼び出し、Navigation、Dialog 表示などの副作用は扱わない。
 */
class SampleHistoryReducer @Inject constructor() :
    Reducer<SampleHistoryUiState, SampleHistoryUiMessage> {

    /**
     * 現在 State と Message から次 State を生成する。
     *
     * @param currentState 現在の画面状態。
     * @param message State 更新理由を表す Message。
     * @return Message 反映後の画面状態。
     */
    override fun reduce(
        currentState: SampleHistoryUiState,
        message: SampleHistoryUiMessage
    ): SampleHistoryUiState {
        return when (message) {
            SampleHistoryUiMessage.LoadStarted -> {
                currentState.copy(
                    isLoading = true,
                    screenErrorMessage = null
                )
            }

            is SampleHistoryUiMessage.LoadSucceeded -> {
                currentState.copy(
                    isLoading = false,
                    items = message.items,
                    screenErrorMessage = null
                )
            }

            is SampleHistoryUiMessage.LoadFailed -> {
                currentState.copy(
                    isLoading = false,
                    screenErrorMessage = message.message
                )
            }

            is SampleHistoryUiMessage.SearchQueryUpdated -> {
                currentState.copy(searchQuery = message.query)
            }

            is SampleHistoryUiMessage.FilterUpdated -> {
                currentState.copy(selectedFilter = message.filter)
            }
        }
    }
}