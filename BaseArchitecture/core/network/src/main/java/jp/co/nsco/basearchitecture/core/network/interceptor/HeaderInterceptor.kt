package jp.co.nsco.basearchitecture.core.network.interceptor

import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

/**
 * API リクエストへ共通 Header を付与する OkHttp Interceptor。
 *
 * 本クラスは、アプリ内の HTTP リクエストに対して、
 * 認証状態に依存しない固定 Header を付与する責務を持つ。
 *
 * ■ 提供する責務
 *   固定共通 Header の付与
 *   Header 設定処理の集約
 *   OkHttp Interceptor としてのリクエスト加工
 *
 * ■ 設計上の意図
 *   API 呼び出しごとに Header を個別設定すると、
 *   設定漏れや表記ゆれが発生しやすくなる。
 *
 *   HeaderInterceptor に固定 Header を集約することで、
 *   Retrofit / Gateway / Repository 側は Header 設定を意識せず、
 *   API 呼び出しの責務に集中できるようにする。
 *
 * ■ 注意
 *   本 Interceptor は認証状態に依存しない固定 Header のみを扱う。
 *   Authorization などの動的 Header は AuthHeaderInterceptor で扱う。
 */
class HeaderInterceptor @Inject constructor() : Interceptor {

    /**
     * HTTP リクエストに固定共通 Header を付与して次の処理へ渡す。
     *
     * 現在は JSON API を前提として Accept: application/json を設定する。
     *
     * @param chain OkHttp の Interceptor Chain。
     * @return 固定 Header 付与後のリクエストに対するレスポンス。
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header(ACCEPT_HEADER_NAME, APPLICATION_JSON)
            .build()

        return chain.proceed(request)
    }

    private companion object {

        /**
         * レスポンスとして受け入れる Content-Type を指定する Header 名。
         */
        private const val ACCEPT_HEADER_NAME = "Accept"

        /**
         * JSON API を利用するための Header 値。
         */
        private const val APPLICATION_JSON = "application/json"
    }
}