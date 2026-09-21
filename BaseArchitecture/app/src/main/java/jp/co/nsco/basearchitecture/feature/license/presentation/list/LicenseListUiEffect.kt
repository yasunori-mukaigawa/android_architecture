package jp.co.nsco.basearchitecture.feature.license.presentation.list

import jp.co.nsco.basearchitecture.core.architecture.UiEffect

/**
 * LicenseListUiEffect。
 *
 * この契約は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
sealed interface LicenseListUiEffect : UiEffect {
    data class NavigateToDetail(val id: String) : LicenseListUiEffect
    data object NavigateBack : LicenseListUiEffect
    data class ShowDialog(val title: String, val message: String) : LicenseListUiEffect
}






