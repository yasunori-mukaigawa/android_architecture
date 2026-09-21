package jp.co.nsco.basearchitecture.core.auth

/**
 * API 認証に使用するトークンを提供する契約。
 *
 * 本インターフェースは、AuthHeaderInterceptor が認証トークンの保存先を
 * 直接知らないようにするための境界である。
 *
 * ■ 提供する責務
 *   認証トークン取得の抽象化
 *   トークン保存方式の隠蔽
 *   認証 Header 付与処理からトークン取得処理を分離
 *
 * ■ 設計上の意図
 *   OkHttp Interceptor は同期処理で実行されるため、
 *   AuthHeaderInterceptor から suspend 関数を直接呼び出さない。
 *
 *   トークンの保存先や読取方法は AuthTokenProvider 実装に閉じ込め、
 *   Interceptor は現在利用可能なトークンを取得して Header に付与するだけにする。
 */
interface AuthTokenProvider {

    /**
     * API 認証に使用する現在のトークンを取得する。
     *
     * null または空文字の場合、AuthHeaderInterceptor は Authorization Header を付与しない。
     *
     * @return API 認証に使用するトークン。未ログインや未設定の場合は null。
     */
    fun getToken(): String?
}
