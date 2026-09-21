package jp.co.nsco.basearchitecture.core.network.interceptor

import javax.inject.Inject
import okhttp3.logging.HttpLoggingInterceptor

/**
 * HTTP 通信ログ用 Interceptor を生成する Provider。
 *
 * 本クラスは、HTTP 通信ログを出力するための HttpLoggingInterceptor を、
 * 有効/無効設定に応じて生成する責務を持つ。
 *
 * ■ 提供する責務
 *   HttpLoggingInterceptor の生成
 *   通信ログ出力レベルの制御
 *   OkHttp ログ設定の集約
 *
 * ■ 設計上の意図
 *   通信ログの有効/無効を OkHttpClient 生成箇所へ直接書き込まず、
 *   Provider として切り出す。
 *
 *   これにより、BuildConfig.DEBUG や環境設定に応じたログ制御を
 *   DI Module 側から明示的に指定できる。
 *
 * ■ 注意
 *   BODY レベルのログはリクエスト/レスポンス本文を出力するため、
 *   個人情報や認証情報を含む API では原則使用しない。
 *
 *   本 Provider の標準設定では、有効時も BASIC に留める。
 */
class HttpLoggingInterceptorProvider @Inject constructor() {

    /**
     * HTTP 通信ログ用 Interceptor を生成する。
     *
     * enabled が true の場合は BASIC レベルで通信ログを出力する。
     * enabled が false の場合は NONE レベルとし、通信ログを出力しない。
     *
     * @param enabled 通信ログを有効にする場合は true。
     * @return 設定済みの HttpLoggingInterceptor。
     */
    fun create(enabled: Boolean): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (enabled) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
}