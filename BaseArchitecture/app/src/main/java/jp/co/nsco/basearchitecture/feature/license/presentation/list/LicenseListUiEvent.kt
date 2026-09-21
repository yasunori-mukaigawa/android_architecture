package jp.co.nsco.basearchitecture.feature.license.presentation.list

import jp.co.nsco.basearchitecture.core.architecture.UiEvent

/**
 * LicenseListUiEvent。
 *
 * この契約は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
sealed interface LicenseListUiEvent : UiEvent {
    data object Initialize : LicenseListUiEvent
    data class SearchKeywordChanged(val value: String) : LicenseListUiEvent
    data class LicenseClicked(val id: String) : LicenseListUiEvent
    data object BackClicked : LicenseListUiEvent
}






