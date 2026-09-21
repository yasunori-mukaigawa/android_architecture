package jp.co.nsco.basearchitecture.core.platform

import java.time.ZonedDateTime
import jp.co.nsco.basearchitecture.core.appinfo.AppVersionInfo
import jp.co.nsco.basearchitecture.core.coroutine.AppCoroutineScopeProvider
import jp.co.nsco.basearchitecture.core.coroutine.AppDispatcherProvider
import jp.co.nsco.basearchitecture.core.external.ExternalUri
import jp.co.nsco.basearchitecture.core.time.AppDateTimeProvider
import jp.co.nsco.basearchitecture.core.time.SystemAppClock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlin.coroutines.ContinuationInterceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Android APIに依存しないPlatform実装と値モデルを確認する。
 */
class PlatformValueTest {

    @Test
    fun systemAppClock_returnsCurrentEpochMillis() {
        val before = System.currentTimeMillis()
        val actual = SystemAppClock().nowMillis()
        val after = System.currentTimeMillis()

        assertTrue(actual in before..after)
    }

    @Test
    fun appDateTimeProvider_returnsARecentDateTime() {
        val before = ZonedDateTime.now().minusSeconds(1)
        val actual = AppDateTimeProvider().now()
        val after = ZonedDateTime.now().plusSeconds(1)

        assertTrue(!actual.isBefore(before))
        assertTrue(!actual.isAfter(after))
    }

    @Test
    fun appDispatcherProvider_exposesStandardDispatchers() {
        val provider = AppDispatcherProvider()

        assertEquals(Dispatchers.Main, provider.main)
        assertEquals(Dispatchers.IO, provider.io)
        assertEquals(Dispatchers.Default, provider.default)
    }

    @Test
    fun appCoroutineScopeProvider_createsIoBackedApplicationScope() {
        val provider = AppCoroutineScopeProvider(AppDispatcherProvider())

        assertEquals(
            Dispatchers.IO,
            provider.scope.coroutineContext[ContinuationInterceptor]
        )
        assertNotNull(provider.scope.coroutineContext[kotlinx.coroutines.Job])

        provider.scope.cancel()
    }

    @Test
    fun platformValueModels_keepMeaningfulValues() {
        assertEquals(
            ExternalUri("app://sample", "https://example.com"),
            ExternalUri("app://sample", "https://example.com")
        )
        assertEquals(
            AppVersionInfo("Sample", "jp.co.sample", "1.2.3", 12, "Production"),
            AppVersionInfo("Sample", "jp.co.sample", "1.2.3", 12, "Production")
        )
    }
}
