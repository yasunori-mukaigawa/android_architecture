package jp.co.nsco.basearchitecture.core.network

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

/**
 * NetworkConfig と ApiError の値契約を確認する。
 */
class NetworkModelTest {

    @Test
    fun networkConfig_keepsDefaultsAndRejectsInvalidValues() {
        val config = NetworkConfig(baseUrl = "https://example.com/")

        assertEquals(15, config.connectTimeoutSeconds)
        assertEquals(30, config.readTimeoutSeconds)
        assertEquals(30, config.writeTimeoutSeconds)
        assertEquals(false, config.enableHttpLogging)
        assertIllegalArgument { NetworkConfig("") }
        assertIllegalArgument {
            NetworkConfig("https://example.com/", connectTimeoutSeconds = 0)
        }
    }

    @Test
    fun apiError_exposesCodesAndCauses() {
        val cause = IllegalStateException("cause")

        assertEquals("API_NETWORK_UNAVAILABLE", ApiError.NetworkUnavailable(cause).code)
        assertEquals(cause, ApiError.Timeout(cause).cause)
        assertEquals("API_HTTP_ERROR", ApiError.HttpError(500).code)
        assertEquals("API_EMPTY_BODY", ApiError.EmptyBody.code)
        assertEquals(cause, ApiError.ParseError(cause).cause)
        assertEquals("API_UNKNOWN", ApiError.Unknown(cause).code)
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
