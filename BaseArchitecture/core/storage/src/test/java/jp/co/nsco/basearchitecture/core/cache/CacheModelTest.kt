package jp.co.nsco.basearchitecture.core.cache

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

/**
 * Cache のキー・期限・取得結果・利用方針を確認する。
 */
class CacheModelTest {

    @Test
    fun cacheMetadata_detectsExpirationAtBoundary() {
        val metadata = CacheMetadata(savedAtMillis = 100, expiresAtMillis = 200)

        assertFalse(metadata.isExpired(nowMillis = 199))
        assertTrue(metadata.isExpired(nowMillis = 200))
        assertFalse(CacheMetadata(100, null).isExpired(nowMillis = Long.MAX_VALUE))
    }

    @Test
    fun cacheModels_keepValueAndMissReason() {
        val key = CacheKey("sample")

        assertEquals("sample", key.value)
        assertEquals(CacheResult.Fresh("value"), CacheResult.Fresh("value"))
        assertEquals(CacheResult.Stale("value"), CacheResult.Stale("value"))
        assertEquals(CacheMissReason.NotFound, CacheResult.Miss(CacheMissReason.NotFound).reason)
        assertEquals(CacheMissReason.Expired, CacheResult.Miss(CacheMissReason.Expired).reason)
        assertEquals(CacheMissReason.Invalidated, CacheResult.Miss(CacheMissReason.Invalidated).reason)
    }

    @Test
    fun cachePolicy_exposesStrategiesAndValidatesTtl() {
        val policies = listOf<CachePolicy>(
            CachePolicy.CacheFirst,
            CachePolicy.NetworkFirst,
            CachePolicy.NetworkOnly,
            CachePolicy.CacheOnly
        )

        assertEquals(4, policies.size)
        assertEquals(1_000, CachePolicy.CacheFirstWithTtl(1_000).ttlMillis)
        assertIllegalArgument {
            CachePolicy.CacheFirstWithTtl(0)
        }
    }

    private fun assertIllegalArgument(block: () -> Unit) {
        try {
            block()
            fail("Expected IllegalArgumentException")
        } catch (_: IllegalArgumentException) {
            // Expected validation failure.
        }
    }
}
