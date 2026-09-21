package jp.co.nsco.basearchitecture.feature.versioninfo.presentation

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.architecture.Reducer

/**
 * VersionInfo 画面の State 更新を行う Reducer。
 *
 * VersionInfoReducer は、現在の VersionInfoUiState と
 * VersionInfoUiMessage から、次の VersionInfoUiState を生成する。
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
 *   UseCase 呼び出し、外部URI起動、Navigation、Dialog 表示などの副作用は扱わない。
 */
class VersionInfoReducer @Inject constructor() :
    Reducer<VersionInfoUiState, VersionInfoUiMessage> {

    /**
     * 現在 State と Message から次 State を生成する。
     *
     * @param currentState 現在の画面状態。
     * @param message State 更新理由を表す Message。
     * @return Message 反映後の画面状態。
     */
    override fun reduce(
        currentState: VersionInfoUiState,
        message: VersionInfoUiMessage
    ): VersionInfoUiState {
        return when (message) {
            VersionInfoUiMessage.LoadStarted -> {
                currentState.copy(
                    screenState = VersionInfoScreenState.Loading,
                    errorMessage = null
                )
            }

            is VersionInfoUiMessage.LoadSucceeded -> {
                message.uiState.copy(
                    screenState = VersionInfoScreenState.Loaded,
                    errorMessage = null
                )
            }

            is VersionInfoUiMessage.LoadFailed -> {
                currentState.copy(
                    screenState = VersionInfoScreenState.Error,
                    errorMessage = message.message,
                    showRetryButton = true
                )
            }
        }
    }
}