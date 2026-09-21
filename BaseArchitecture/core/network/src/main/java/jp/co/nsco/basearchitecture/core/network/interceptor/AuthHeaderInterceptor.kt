package jp.co.nsco.basearchitecture.core.network.interceptor

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.auth.AuthTokenProvider
import okhttp3.Interceptor
import okhttp3.Response

/**
 * API リクエストへ認証 Header を付与する OkHttp Interceptor。
 *
 * 本クラスは、AuthTokenProvider から取得した認証トークンを使用して、
 * HTTP リクエストに Authorization Header を付与する責務を持つ。
 *
 * ■ 提供する責務
 *   認証トークンの取得
 *   Authorization Header の付与
 *   未認証時の Header 付与スキップ
 *
 * ■ 設計上の意図
 *   固定 Header と認証 Header を分離することで、
 *   認証要否やトークン取得方式の変更が HeaderInterceptor に波及しないようにする。
 *
 *   トークンの保存先や取得方法は AuthTokenProvider に委譲し、
 *   本クラスは Header 付与処理に専念する。
 *
 * ■ 注意
 *   本 Interceptor は現在保持しているトークンを Header に付与するだけであり、
 *   トークン更新、再認証、401 応答時のリトライは扱わない。
 *
 *   401 応答時の再取得やリトライが必要な場合は、
 *   OkHttp Authenticator または専用の認証制御として分離する。
 *
 * @param tokenProvider 認証トークンを提供する契約。
 */
class AuthHeaderInterceptor @Inject constructor(
    private val tokenProvider: AuthTokenProvider
) : Interceptor {

    /**
     * HTTP リクエストに Authorization Header を付与して次の処理へ渡す。
     *
     * トークンが null または空文字の場合は、
     * Authorization Header を付与せず元のリクエストをそのまま送信する。
     *
     * @param chain OkHttp の Interceptor Chain。
     * @return 認証 Header 付与後、または未付与のリクエストに対するレスポンス。
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider.getToken()

        val request = if (token.isNullOrBlank()) {
            chain.request()
        } else {
            chain.request().newBuilder()
                .header(AUTHORIZATION_HEADER_NAME, "$BEARER_PREFIX $token")
                .build()
        }

        return chain.proceed(request)
    }

    private companion object {

        /**
         * 認証 Header 名。
         */
        private const val AUTHORIZATION_HEADER_NAME = "Authorization"

        /**
         * Bearer token 認証で使用する Header prefix。
         */
        private const val BEARER_PREFIX = "Bearer"
    }
}
