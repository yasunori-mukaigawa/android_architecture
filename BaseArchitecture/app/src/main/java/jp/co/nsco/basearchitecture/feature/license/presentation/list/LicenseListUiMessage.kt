package jp.co.nsco.basearchitecture.feature.license.presentation.list

import jp.co.nsco.basearchitecture.core.architecture.UiMessage

/**
 * LicenseListUiMessage。
 *
 * この契約は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
sealed interface LicenseListUiMessage : UiMessage {
    data object LoadStarted : LicenseListUiMessage
    data class LoadSucceeded(val items: List<LicenseItemUiState>) : LicenseListUiMessage
    data class LoadFailed(val message: String) : LicenseListUiMessage
    data class SearchKeywordUpdated(val value: String) : LicenseListUiMessage
}






