package jp.co.nsco.basearchitecture.feature.license.presentation.list

import jp.co.nsco.basearchitecture.core.architecture.Reducer
import javax.inject.Inject

/**
 * LicenseListReducer。
 *
 * この状態または処理は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
class LicenseListReducer @Inject constructor() :
    Reducer<LicenseListUiState, LicenseListUiMessage> {

    /**
 * reduce。
 *
 * この処理は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
    override fun reduce(
        currentState: LicenseListUiState,
        message: LicenseListUiMessage
    ): LicenseListUiState {
        return when (message) {
            LicenseListUiMessage.LoadStarted -> currentState.copy(
                isLoading = true,
                errorMessage = null
            )

            is LicenseListUiMessage.LoadSucceeded -> currentState.copy(
                isLoading = false,
                items = message.items,
                errorMessage = null
            )

            is LicenseListUiMessage.LoadFailed -> currentState.copy(
                isLoading = false,
                errorMessage = message.message
            )

            is LicenseListUiMessage.SearchKeywordUpdated -> currentState.copy(
                searchKeyword = message.value
            )
        }
    }
}






