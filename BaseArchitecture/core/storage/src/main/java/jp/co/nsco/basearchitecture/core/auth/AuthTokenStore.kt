package jp.co.nsco.basearchitecture.core.auth

import kotlinx.coroutines.flow.Flow

/**
 * 認証トークンを保存・取得するための契約。
 *
 * 本インターフェースは、API 認証に使用するアクセストークンを
 * 永続化するための入口を定義する。
 *
 * ■ 提供する責務
 *   認証トークンの購読
 *   認証トークンの同期取得
 *   認証トークンの保存
 *   認証トークンの削除
 *
 * ■ 設計上の意図
 *   認証トークンの保存先を呼び出し側から隠蔽する。
 *
 *   ViewModel / UseCase / Interceptor は DataStore や暗号化保存の詳細を知らず、
 *   AuthTokenStore / AuthTokenProvider の契約だけを参照する。
 *
 * ■ 注意
 *   本契約はアクセストークンの保存・取得のみを扱う。
 *   トークン更新、リフレッシュトークン、401 応答時の再認証は別責務として扱う。
 */
interface AuthTokenStore {

    /**
     * 認証トークンを購読する。
     *
     * ログイン状態の監視や、認証状態に応じた UI 切り替えに使用する。
     *
     * @return 認証トークンを流す Flow。未保存の場合は null。
     */
    fun observeToken(): Flow<String?>

    /**
     * 現在保存されている認証トークンを同期的に取得する。
     *
     * OkHttp Interceptor など、suspend 関数を直接呼び出せない場所で使用する。
     *
     * @return 現在保存されている認証トークン。未保存の場合は null。
     */
    fun getToken(): String?

    /**
     * 認証トークンを保存する。
     *
     * @param token 保存する認証トークン。
     */
    suspend fun saveToken(token: String)

    /**
     * 保存されている認証トークンを削除する。
     *
     * ログアウトや認証失効時に使用する。
     */
    suspend fun clearToken()
}