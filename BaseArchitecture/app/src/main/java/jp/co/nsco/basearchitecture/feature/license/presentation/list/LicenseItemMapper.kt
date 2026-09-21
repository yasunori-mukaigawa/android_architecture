package jp.co.nsco.basearchitecture.feature.license.presentation.list

import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.license.LicenseInfo
import jp.co.nsco.basearchitecture.core.resource.StringProvider
import javax.inject.Inject

/**
 * LicenseItemMapper。
 *
 * この状態または処理は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
class LicenseItemMapper @Inject constructor(
    private val stringProvider: StringProvider
) {

    /**
 * map。
 *
 * この処理は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
    fun map(info: LicenseInfo): LicenseItemUiState {
        val unknown = stringProvider.getString(R.string.license_unknown)

        return LicenseItemUiState(
            id = info.id,
            name = info.name.ifBlank { unknown },
            licenseName = info.licenseName.ifBlank { unknown },
            copyright = info.copyright.ifBlank { unknown }
        )
    }
}






