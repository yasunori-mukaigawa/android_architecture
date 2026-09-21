package jp.co.nsco.basearchitecture.feature.versioninfo.application

import jp.co.nsco.basearchitecture.core.appinfo.AppInfoProvider
import jp.co.nsco.basearchitecture.core.appinfo.AppVersionInfo
import jp.co.nsco.basearchitecture.core.error.AppError
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.core.time.AppClock
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.AppExternalLinksProvider
import jp.co.nsco.basearchitecture.feature.versioninfo.data.SampleAppExternalLinksProvider
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.LatestVersionInfo
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.LatestVersionRepository
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.VersionCheckStatus
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class GetVersionInfoUseCaseTest {
    @Test
    fun invoke_current_lower_than_latest_returns_updateAvailable() = runTest {
        val useCase = createUseCase(latestResult = AppResult.Success(LatestVersionInfo("v1.2.4", 2L)))

        val actual = useCase()

        assertEquals(VersionCheckStatus.UpdateAvailable, (actual as AppResult.Success).value.status)
    }

    @Test
    fun invoke_current_greater_or_equal_latest_returns_latest() = runTest {
        val useCase = createUseCase(
            appVersionCode = 2L,
            latestResult = AppResult.Success(LatestVersionInfo("v1.2.3", 2L))
        )

        val actual = useCase()

        assertEquals(VersionCheckStatus.Latest, (actual as AppResult.Success).value.status)
    }

    @Test
    fun invoke_latest_failure_returns_checkFailed() = runTest {
        val useCase = createUseCase(
            latestResult = AppResult.Failure(AppError.Network("TEST_NETWORK"))
        )

        val actual = useCase()

        assertEquals(VersionCheckStatus.CheckFailed, (actual as AppResult.Success).value.status)
    }

    @Test
    fun invoke_appInfo_failure_returns_failure() = runTest {
        val error = AppError.LocalStorage("APP_INFO_FAILED")
        val useCase = createUseCase(appInfoResult = AppResult.Failure(error))

        val actual = useCase()

        assertTrue(actual is AppResult.Failure)
        assertSame(error, (actual as AppResult.Failure).error)
    }

    private fun createUseCase(
        appVersionCode: Long = 1L,
        appInfoResult: AppResult<AppVersionInfo> = AppResult.Success(
            AppVersionInfo("App", "pkg", "1.0", appVersionCode, "Production")
        ),
        latestResult: AppResult<LatestVersionInfo> = AppResult.Success(LatestVersionInfo("v1.2.4", 2L))
    ): GetVersionInfoUseCase {
        return GetVersionInfoUseCase(
            appInfoProvider = object : AppInfoProvider {
                override fun getAppVersionInfo() = appInfoResult
            },
            latestVersionRepository = object : LatestVersionRepository {
                override suspend fun getLatestVersion() = latestResult
            },
            externalLinksProvider = SampleAppExternalLinksProvider(),
            appClock = object : AppClock {
                override fun nowMillis() = 1_000L
            }
        )
    }
}






