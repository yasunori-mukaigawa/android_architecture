package jp.co.nsco.basearchitecture.core.network

import java.io.IOException
import java.net.SocketTimeoutException
import jp.co.nsco.basearchitecture.core.error.AppError
import jp.co.nsco.basearchitecture.core.result.AppResult
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.CancellationException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class ApiResponseHandlerTest {
    private val handler = ApiResponseHandler(ApiErrorMapper())

    @Test
    fun handle_2xx_body_returns_success() = runTest {
        val actual = handler.handle { Response.success("body") }

        assertEquals(AppResult.Success("body"), actual)
    }

    @Test
    fun handle_2xx_empty_body_returns_invalidResponse() = runTest {
        val actual = handler.handle<String> { Response.success(null) }

        assertTrue((actual as AppResult.Failure).error is AppError.ApiInvalidResponse)
    }

    @Test
    fun handle_401_returns_api_error() = runTest {
        val actual = handler.handle<String> { Response.error(401, "".toResponseBody()) }

        val error = (actual as AppResult.Failure).error as AppError.Api
        assertEquals(401, error.statusCode)
    }

    @Test
    fun handle_404_returns_api_error() = runTest {
        val actual = handler.handle<String> { Response.error(404, "".toResponseBody()) }

        val error = (actual as AppResult.Failure).error as AppError.Api
        assertEquals(404, error.statusCode)
    }

    @Test
    fun handle_ioException_returns_network() = runTest {
        val actual = handler.handle<String> { throw IOException("network") }

        assertTrue((actual as AppResult.Failure).error is AppError.Network)
    }

    @Test
    fun handle_timeout_returns_network() = runTest {
        val actual = handler.handle<String> { throw SocketTimeoutException("timeout") }

        assertTrue((actual as AppResult.Failure).error is AppError.Network)
    }

    @Test
    fun handle_cancellationException_isRethrown() = runTest {
        val cancellation = CancellationException("cancelled")

        try {
            handler.handle<String> { throw cancellation }
            fail("CancellationException should be rethrown")
        } catch (actual: CancellationException) {
            assertEquals(cancellation, actual)
        }
    }
}






