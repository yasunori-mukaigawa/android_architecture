package jp.co.nsco.basearchitecture.feature.license.presentation.detail

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.architecture.Reducer

/**
 * ライセンス詳細画面の State 更新を行う Reducer。
 *
 * LicenseDetailReducer は、現在の LicenseDetailUiState と
 * LicenseDetailUiMessage から、次の LicenseDetailUiState を生成する。
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
class LicenseDetailReducer @Inject constructor() :
    Reducer<LicenseDetailUiState, LicenseDetailUiMessage> {

    /**
     * 現在 State と Message から次 State を生成する。
     *
     * @param currentState 現在の画面状態。
     * @param message State 更新理由を表す Message。
     * @return Message 反映後の画面状態。
     */
    override fun reduce(
        currentState: LicenseDetailUiState,
        message: LicenseDetailUiMessage
    ): LicenseDetailUiState {
        return when (message) {
            LicenseDetailUiMessage.LoadStarted -> currentState.copy(
                isLoading = true,
                errorMessage = null
            )

            is LicenseDetailUiMessage.LoadSucceeded -> currentState.copy(
                isLoading = false,
                name = message.name,
                licenseName = message.licenseName,
                copyright = message.copyright,
                licenseText = message.licenseText,
                errorMessage = null
            )

            is LicenseDetailUiMessage.LoadFailed -> currentState.copy(
                isLoading = false,
                errorMessage = message.message
            )
        }
    }
}