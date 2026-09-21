package jp.co.nsco.basearchitecture.feature.license.presentation.list

import javax.inject.Inject
import jp.co.nsco.basearchitecture.feature.license.application.GetLicensesUseCase

/**
 * LicenseListUseCaseFacade。
 *
 * この状態または処理は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
class LicenseListUseCaseFacade @Inject constructor(
    val getLicenses: GetLicensesUseCase
)




