package jp.co.nsco.basearchitecture.feature.versioninfo.presentation

import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.external.ExternalUri
import jp.co.nsco.basearchitecture.core.resource.StringProvider
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.AppExternalLinks
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.VersionCheckStatus
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.VersionInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VersionInfoUiMapperTest {
    private val mapper = VersionInfoUiMapper(FakeStringProvider())

    @Test
    fun map_latest_sets_latest_display() {
        val actual = mapper.map(versionInfo(VersionCheckStatus.Latest))

        assertEquals("最新版です", actual.statusTitle)
        assertFalse(actual.showOpenStoreButton)
        assertFalse(actual.showRetryButton)
    }

    @Test
    fun map_updateAvailable_shows_store_button() {
        val actual = mapper.map(versionInfo(VersionCheckStatus.UpdateAvailable))

        assertEquals("最新版ではありません", actual.statusTitle)
        assertTrue(actual.showOpenStoreButton)
    }

    @Test
    fun map_checkFailed_shows_retry_button() {
        val actual = mapper.map(versionInfo(VersionCheckStatus.CheckFailed))

        assertEquals("バージョン確認に失敗しました", actual.statusTitle)
        assertTrue(actual.showRetryButton)
    }

    private fun versionInfo(status: VersionCheckStatus) = VersionInfo(
        appName = "App",
        description = "desc",
        currentVersionName = "v1.2.3",
        currentVersionCode = 1L,
        latestVersionName = "v1.2.4",
        latestVersionCode = 2L,
        buildNumber = "10203",
        channel = "Production",
        lastCheckedAtMillis = 1_000L,
        status = status,
        externalLinks = AppExternalLinks(ExternalUri("market://sample"), ExternalUri("https://sample"))
    )

    private class FakeStringProvider : StringProvider {
        override fun getString(resId: Int): String = when (resId) {
            R.string.version_info_status_latest_title -> "最新版です"
            R.string.version_info_status_update_available_title -> "最新版ではありません"
            R.string.version_info_status_check_failed_title -> "バージョン確認に失敗しました"
            R.string.version_info_status_latest_message -> "latest"
            R.string.version_info_status_update_available_message -> "update"
            R.string.version_info_status_check_failed_message -> "failed"
            else -> error("Unexpected resId=$resId")
        }

        override fun getString(resId: Int, vararg args: Any): String = getString(resId)
    }
}





