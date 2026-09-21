package jp.co.nsco.basearchitecture.core.platform

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.ActivityNotFoundException
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.co.nsco.basearchitecture.core.appinfo.AndroidAppInfoProvider
import jp.co.nsco.basearchitecture.core.error.AppError
import jp.co.nsco.basearchitecture.core.external.AndroidExternalUriOpener
import jp.co.nsco.basearchitecture.core.external.ExternalUri
import jp.co.nsco.basearchitecture.core.logging.DefaultAppLoggingConfiguration
import jp.co.nsco.basearchitecture.core.navigation.asRouteArgument
import jp.co.nsco.basearchitecture.core.resource.AndroidRawResourceReader
import jp.co.nsco.basearchitecture.core.resource.AndroidStringProvider
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Context / Resources / Intent を使用するPlatform実装を確認する。
 */
@RunWith(AndroidJUnit4::class)
class PlatformAndroidTest {

    private val targetContext: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun androidStringProvider_readsAndroidStringResource() {
        val provider = AndroidStringProvider(targetContext)

        assertEquals(targetContext.getString(android.R.string.ok), provider.getString(android.R.string.ok))
    }

    @Test
    fun androidAppInfoProvider_returnsApplicationVersionInfo() {
        val result = AndroidAppInfoProvider(targetContext).getAppVersionInfo()

        assertTrue(result is jp.co.nsco.basearchitecture.core.result.AppResult.Success)
        val info = (result as jp.co.nsco.basearchitecture.core.result.AppResult.Success).value
        assertEquals(targetContext.packageName, info.packageName)
        assertNotNull(info.appName)
        assertNotNull(info.versionName)
        assertEquals("Production", info.channel)
    }

    @Test
    fun androidRawResourceReader_readsTestResourceAndReturnsFailureForUnknownId() = runBlocking {
        val testContext = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().context
        val resourceId = testContext.resources.getIdentifier(
            "core_test_text",
            "raw",
            testContext.packageName
        )
        val reader = AndroidRawResourceReader(testContext)

        val success = reader.readText(resourceId)
        val failure = reader.readText(0)

        assertEquals("Core raw resource test", (success as jp.co.nsco.basearchitecture.core.result.AppResult.Success).value)
        assertTrue(failure is jp.co.nsco.basearchitecture.core.result.AppResult.Failure)
        assertEquals(
            "RAW_RESOURCE_READ_FAILED",
            (failure as jp.co.nsco.basearchitecture.core.result.AppResult.Failure).error.code
        )
    }

    @Test
    fun routeArgumentEncoder_roundTripsReservedCharacters() {
        val source = "sample/id?name=日本語&value=1"
        val encoded = source.asRouteArgument()

        assertTrue('/' !in encoded)
        assertTrue('?' !in encoded)
        assertTrue('&' !in encoded)
        assertEquals(source, android.net.Uri.decode(encoded))
    }

    @Test
    fun externalUriOpener_returnsStructuredFailureWhenStartActivityFails() {
        val context = object : ContextWrapper(targetContext) {
            override fun startActivity(intent: Intent) {
                throw ActivityNotFoundException("not found")
            }
        }

        val result = AndroidExternalUriOpener(context).open(
            ExternalUri("app://primary", "app://fallback")
        )

        assertTrue(result is jp.co.nsco.basearchitecture.core.result.AppResult.Failure)
        val error = (result as jp.co.nsco.basearchitecture.core.result.AppResult.Failure).error
        assertTrue(error is AppError.Unexpected)
        assertEquals("EXTERNAL_URI_OPEN_FAILED", error.code)
    }

    @Test
    fun androidLogger_canWriteAllLogLevels() {
        val logger = jp.co.nsco.basearchitecture.core.logging.AndroidAppLogger(
            DefaultAppLoggingConfiguration()
        )

        logger.debug("debug")
        logger.info("info")
        logger.warn("warn")
        logger.error("error", IllegalStateException("test"))
    }
}
