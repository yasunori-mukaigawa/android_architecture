package jp.co.nsco.basearchitecture.feature.license.application

import jp.co.nsco.basearchitecture.core.error.AppError
import jp.co.nsco.basearchitecture.core.license.LicenseDetail
import jp.co.nsco.basearchitecture.core.license.LicenseInfo
import jp.co.nsco.basearchitecture.core.license.LicenseProvider
import jp.co.nsco.basearchitecture.core.result.AppResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * LicenseUseCaseTest。
 *
 * この状態または処理は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
class LicenseUseCaseTest {

    @Test
    fun getLicensesUseCase_returns_sorted_licenses() {
        val provider = FakeLicenseProvider(
            licensesResult = AppResult.Success(
                listOf(
                    LicenseInfo("room", "Room", "Apache License 2.0", "copyright"),
                    LicenseInfo("androidx-core", "AndroidX Core", "Apache License 2.0", "copyright")
                )
            )
        )
        val useCase = GetLicensesUseCase(provider)

        val actual = useCase()

        assertTrue(actual is AppResult.Success)
        val licenses = (actual as AppResult.Success).value
        assertEquals("AndroidX Core", licenses.first().name)
        assertEquals("Room", licenses.last().name)
    }

    @Test
    fun getLicenseDetailUseCase_blank_id_returns_validation_failure() {
        val useCase = GetLicenseDetailUseCase(FakeLicenseProvider())

        val actual = useCase(" ")

        assertTrue(actual is AppResult.Failure)
        assertTrue((actual as AppResult.Failure).error is AppError.Validation)
    }

    @Test
    fun getLicenseDetailUseCase_valid_id_returns_provider_detail() {
        val detail = LicenseDetail(
            id = "androidx-core",
            name = "AndroidX Core",
            licenseName = "Apache License 2.0",
            copyright = "copyright",
            licenseText = "license text"
        )
        val useCase = GetLicenseDetailUseCase(
            FakeLicenseProvider(detailResult = AppResult.Success(detail))
        )

        val actual = useCase("androidx-core")

        assertEquals(AppResult.Success(detail), actual)
    }

    private class FakeLicenseProvider(
        private val licensesResult: AppResult<List<LicenseInfo>> = AppResult.Success(emptyList()),
        private val detailResult: AppResult<LicenseDetail> = AppResult.Failure(
            AppError.LocalStorage(code = "NOT_FOUND")
        )
    ) : LicenseProvider {

        override fun getLicenses(): AppResult<List<LicenseInfo>> {
            return licensesResult
        }

        override fun getLicenseDetail(id: String): AppResult<LicenseDetail> {
            return detailResult
        }
    }
}






