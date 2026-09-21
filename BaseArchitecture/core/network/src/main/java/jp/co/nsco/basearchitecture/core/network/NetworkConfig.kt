package jp.co.nsco.basearchitecture.core.network

/**
 * API 通信基盤の設定値。
 *
 * 本クラスは、Retrofit / OkHttpClient を構成するために必要な
 * 通信設定を保持する。
 *
 * ■ 提供する責務
 *   API base URL の保持
 *   接続タイムアウトの保持
 *   読込タイムアウトの保持
 *   書込タイムアウトの保持
 *   HTTP 通信ログ有効/無効の保持
 *
 * ■ 設計上の意図
 *   baseUrl や timeout 値を NetworkModule 内に直接埋め込まず、
 *   NetworkConfig としてまとめて扱う。
 *
 *   app 側の DI Module で BuildConfig や環境設定から NetworkConfig を生成し、
 *   core.network 側の通信基盤は NetworkConfig の値だけを参照する。
 *
 * @property baseUrl API のベース URL。
 *                   Retrofit の baseUrl として使用するため、末尾は `/` を含める。
 * @property connectTimeoutSeconds 接続タイムアウト秒数。
 * @property readTimeoutSeconds 読込タイムアウト秒数。
 * @property writeTimeoutSeconds 書込タイムアウト秒数。
 * @property enableHttpLogging HTTP 通信ログを有効にする場合は true。
 *
 * @throws IllegalArgumentException baseUrl が空、または timeout が 0 以下の場合。
 */
data class NetworkConfig(
    val baseUrl: String,
    val connectTimeoutSeconds: Long = 15,
    val readTimeoutSeconds: Long = 30,
    val writeTimeoutSeconds: Long = 30,
    val enableHttpLogging: Boolean = false
) {
    init {
        require(baseUrl.isNotBlank()) {
            "baseUrl must not be blank."
        }
        require(connectTimeoutSeconds > 0) {
            "connectTimeoutSeconds must be greater than 0."
        }
        require(readTimeoutSeconds > 0) {
            "readTimeoutSeconds must be greater than 0."
        }
        require(writeTimeoutSeconds > 0) {
            "writeTimeoutSeconds must be greater than 0."
        }
    }
}