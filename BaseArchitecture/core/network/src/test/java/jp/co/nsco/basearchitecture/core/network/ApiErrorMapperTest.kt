package jp.co.nsco.basearchitecture.core.network

import java.io.IOException
import java.net.SocketTimeoutException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import retrofit2.HttpException

/**
 * ApiErrorMapper の HTTP / 例外分類を確認する。
 */
class ApiErrorMapperTest {

    private val mapper = ApiErrorMapper()

    @Test
    fun fromResponse_classifiesKnownHttpStatuses() {
        assertEquals(ApiError.Unauthorized, mapper.fromResponse(Response.error<Any>(401, "".toResponseBody())))
        assertEquals(ApiError.Forbidden, mapper.fromResponse(Response.error<Any>(403, "".toResponseBody())))
        assertEquals(ApiError.NotFound, mapper.fromResponse(Response.error<Any>(404, "".toResponseBody())))
    }

    @Test
    fun fromResponse_keepsStatusAndBodyForOtherErrors() {
        val error = mapper.fromResponse(
            Response.error<Any>(503, "service unavailable".toResponseBody())
        ) as ApiError.HttpError

        assertEquals(503, error.statusCode)
        assertEquals("service unavailable", error.errorBody)
    }

    @Test
    fun fromThrowable_classifiesTimeoutNetworkHttpAndUnknown() {
        val timeout = mapper.fromThrowable(SocketTimeoutException("timeout"))
        val network = mapper.fromThrowable(IOException("network"))
        val http = mapper.fromThrowable(HttpException(Response.error<Any>(500, "".toResponseBody())))
        val unknown = mapper.fromThrowable(IllegalStateException("unknown"))

        assertTrue(timeout is ApiError.Timeout)
        assertTrue(network is ApiError.NetworkUnavailable)
        assertEquals(500, (http as ApiError.HttpError).statusCode)
        assertTrue(unknown is ApiError.Unknown)
    }
}
