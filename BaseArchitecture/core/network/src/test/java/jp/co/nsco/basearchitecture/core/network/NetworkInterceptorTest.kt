package jp.co.nsco.basearchitecture.core.network

import jp.co.nsco.basearchitecture.core.network.interceptor.AuthHeaderInterceptor
import jp.co.nsco.basearchitecture.core.auth.AuthTokenProvider
import jp.co.nsco.basearchitecture.core.network.interceptor.HeaderInterceptor
import jp.co.nsco.basearchitecture.core.network.interceptor.HttpLoggingInterceptorProvider
import java.util.concurrent.TimeUnit
import okhttp3.Call
import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * OkHttp Interceptor が実際の送信リクエストへ契約どおり適用されることを確認する。
 */
class NetworkInterceptorTest {

    @Test
    fun interceptors_addCommonAndBearerHeaders() {
        val original = Request.Builder()
            .url("https://example.com/test")
            .build()
        val headerChain = RecordingChain(original)
        val authChain = RecordingChain(original)

        HeaderInterceptor().intercept(headerChain)
        AuthHeaderInterceptor(FakeTokenProvider("token")).intercept(authChain)

        assertEquals("application/json", headerChain.proceededRequest.header("Accept"))
        assertEquals("Bearer token", authChain.proceededRequest.header("Authorization"))
    }

    @Test
    fun authInterceptor_skipsHeaderForBlankToken() {
        val chain = RecordingChain(
            Request.Builder().url("https://example.com/test").build()
        )

        AuthHeaderInterceptor(FakeTokenProvider("  ")).intercept(chain)

        assertNull(chain.proceededRequest.header("Authorization"))
    }

    @Test
    fun loggingProvider_selectsBasicOrNone() {
        val provider = HttpLoggingInterceptorProvider()

        assertEquals(
            HttpLoggingInterceptor.Level.BASIC,
            provider.create(enabled = true).level
        )
        assertEquals(
            HttpLoggingInterceptor.Level.NONE,
            provider.create(enabled = false).level
        )
    }

    private class FakeTokenProvider(
        private val token: String?
    ) : AuthTokenProvider {
        override fun getToken(): String? = token
    }

    private class RecordingChain(
        private val originalRequest: Request
    ) : Interceptor.Chain {
        lateinit var proceededRequest: Request

        override fun request(): Request = originalRequest

        override fun proceed(request: Request): Response {
            proceededRequest = request
            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .build()
        }

        override fun connection(): Connection? = null

        override fun call(): Call = error("call is not used by this test")

        override fun connectTimeoutMillis(): Int = 10_000

        override fun withConnectTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain = this

        override fun readTimeoutMillis(): Int = 10_000

        override fun withReadTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain = this

        override fun writeTimeoutMillis(): Int = 10_000

        override fun withWriteTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain = this
    }
}
