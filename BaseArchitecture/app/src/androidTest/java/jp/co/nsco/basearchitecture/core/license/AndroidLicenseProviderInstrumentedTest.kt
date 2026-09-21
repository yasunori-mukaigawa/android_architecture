package jp.co.nsco.basearchitecture.core.license

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.co.nsco.basearchitecture.core.result.AppResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * アプリのInstrumentation環境でLicense fallbackと検索結果を確認する。
 */
@RunWith(AndroidJUnit4::class)
class AndroidLicenseProviderInstrumentedTest {

    private val provider = AndroidLicenseProvider(
        ApplicationProvider.getApplicationContext()
    )

    @Test
    fun getLicenses_returnsFallbackListWhenGeneratedResourcesAreUnavailable() {
        val result = provider.getLicenses()

        assertTrue(result is AppResult.Success)
        val licenses = (result as AppResult.Success).value
        assertFalse(licenses.isEmpty())
        assertEquals("androidx-core", licenses.first().id)
    }

    @Test
    fun getLicenseDetail_returnsDetailAndStructuredFailureForUnknownId() {
        val success = provider.getLicenseDetail("androidx-core")
        val failure = provider.getLicenseDetail("unknown-license")

        assertTrue(success is AppResult.Success)
        assertEquals("AndroidX Core", (success as AppResult.Success).value.name)
        assertTrue(failure is AppResult.Failure)
        assertEquals("LICENSE_DETAIL_NOT_FOUND", (failure as AppResult.Failure).error.code)
    }
}
