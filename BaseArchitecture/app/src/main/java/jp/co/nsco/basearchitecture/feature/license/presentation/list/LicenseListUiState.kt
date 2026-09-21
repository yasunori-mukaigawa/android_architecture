package jp.co.nsco.basearchitecture.feature.license.presentation.list

import jp.co.nsco.basearchitecture.core.architecture.UiState

/**
 * LicenseListUiState。
 *
 * この状態または処理は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
data class LicenseListUiState(
    val isLoading: Boolean = true,
    val searchKeyword: String = "",
    val items: List<LicenseItemUiState> = emptyList(),
    val errorMessage: String? = null
) : UiState {

    /**
 * この要素。
 *
 * この責務 は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
    val visibleItems: List<LicenseItemUiState>
        get() {
            if (searchKeyword.isBlank()) {
                return items
            }

            return items.filter { item ->
                item.name.contains(searchKeyword, ignoreCase = true) ||
                    item.licenseName.contains(searchKeyword, ignoreCase = true)
            }
        }
}






