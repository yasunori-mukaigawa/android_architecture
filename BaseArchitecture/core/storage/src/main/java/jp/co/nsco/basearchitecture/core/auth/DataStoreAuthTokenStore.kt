package jp.co.nsco.basearchitecture.core.auth

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.datastore.PreferenceDataStore
import jp.co.nsco.basearchitecture.core.auth.AuthTokenProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * PreferenceDataStore を利用して認証トークンを保存・取得する実装。
 *
 * 本クラスは、API 認証に使用するアクセストークンを
 * PreferenceDataStore に保存する責務を持つ。
 *
 * ■ 提供する責務
 *   認証トークンの購読
 *   認証トークンの同期取得
 *   認証トークンの保存
 *   認証トークンの削除
 *   AuthTokenProvider 実装の提供
 *
 * ■ 設計上の意図
 *   認証トークンの保存先を DataStore に閉じ込める。
 *
 *   AuthHeaderInterceptor は AuthTokenProvider の契約だけを参照し、
 *   DataStore / PreferenceDataStore / キー名を知らない。
 *
 *   Interceptor は同期処理で実行されるため、getToken では runBlocking を使用して
 *   DataStore の現在値を取得する。
 *
 * ■ 注意
 *   runBlocking は Interceptor から同期的にトークンを取得するために使用している。
 *   重い処理や通信処理はここで行わない。
 *
 * @param preferenceDataStore 軽量設定値を保存する DataStore 契約。
 */
class DataStoreAuthTokenStore @Inject constructor(
    private val preferenceDataStore: PreferenceDataStore
) : AuthTokenStore,
    AuthTokenProvider {

    /**
     * 認証トークンを購読する。
     *
     * 未保存状態は null として扱う。
     * 内部的には空文字を未保存値として扱い、外部へは null に変換する。
     *
     * @return 認証トークンを流す Flow。
     */
    override fun observeToken(): Flow<String?> {
        return preferenceDataStore
            .observeString(AUTH_TOKEN_KEY, EMPTY_TOKEN)
            .map { token -> token.ifBlank { null } }
    }

    /**
     * 現在保存されている認証トークンを同期的に取得する。
     *
     * OkHttp Interceptor から呼び出されることを想定しているため、
     * suspend ではなく同期関数として提供する。
     *
     * @return 現在保存されている認証トークン。未保存の場合は null。
     */
    override fun getToken(): String? {
        return runBlocking {
            preferenceDataStore
                .observeString(AUTH_TOKEN_KEY, EMPTY_TOKEN)
                .first()
                .ifBlank { null }
        }
    }

    /**
     * 認証トークンを保存する。
     *
     * @param token 保存する認証トークン。
     */
    override suspend fun saveToken(token: String) {
        preferenceDataStore.putString(AUTH_TOKEN_KEY, token)
    }

    /**
     * 保存されている認証トークンを削除する。
     */
    override suspend fun clearToken() {
        preferenceDataStore.remove(AUTH_TOKEN_KEY)
    }

    private companion object {

        /**
         * PreferenceDataStore に保存する認証トークンのキー。
         */
        private const val AUTH_TOKEN_KEY = "auth_token"

        /**
         * 未保存状態を表す内部用の空トークン。
         *
         * PreferenceDataStore の observeString は defaultValue が必要なため、
         * 未保存時は空文字として取得し、外部へ返す際に null へ変換する。
         */
        private const val EMPTY_TOKEN = ""
    }
}
