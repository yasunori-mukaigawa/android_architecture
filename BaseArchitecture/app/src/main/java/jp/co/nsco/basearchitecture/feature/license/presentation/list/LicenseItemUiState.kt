package jp.co.nsco.basearchitecture.feature.license.presentation.list

/**
 * LicenseItemUiState。
 *
 * この状態または処理は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
data class LicenseItemUiState(
    val id: String,
    val name: String,
    val licenseName: String,
    val copyright: String
)






