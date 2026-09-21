package jp.co.nsco.basearchitecture.core.network

/**
 * API 通信処理で発生した失敗理由を表すエラー契約。
 *
 * ApiError は、Retrofit / OkHttp / 通信例外 / HTTP ステータスなどを、
 * API 通信領域で扱いやすい失敗理由として分類したものである。
 *
 * ■ 提供する責務
 *   ネットワーク未接続の表現
 *   タイムアウトの表現
 *   HTTP エラーレスポンスの表現
 *   認証・認可エラーの表現
 *   レスポンスボディ不足の表現
 *   レスポンス解析失敗の表現
 *   想定外通信エラーの表現
 *
 * ■ 設計上の意図
 *   Retrofit / OkHttp の例外や Response をそのまま上位層へ漏らさず、
 *   API 通信としての失敗理由に変換する。
 *
 *   ApiError は通信領域の詳細な分類を表し、
 *   最終的に AppError へ変換して Application / Presentation 層へ返す。
 *
 * ■ 注意
 *   ApiError は API 通信内部の分類であり、画面表示用エラーではない。
 *   UI 表示には AppError から UiError へ変換した情報を使用する。
 */
sealed interface ApiError {

    /**
     * API エラーを識別するコード。
     *
     * AppError 変換、ログ出力、テスト判定、文言解決のキーとして使用する。
     */
    val code: String

    /**
     * 元になった例外。
     *
     * HTTP ステータスなど例外を伴わないエラーでは null を許容する。
     */
    val cause: Throwable?

    /**
     * ネットワーク利用不可による失敗。
     *
     * 端末の通信不可、DNS失敗、接続失敗など、
     * HTTP レスポンスを受け取る前の通信失敗を表す。
     *
     * @property cause 元になった例外。
     */
    data class NetworkUnavailable(
        override val cause: Throwable? = null
    ) : ApiError {
        override val code = "API_NETWORK_UNAVAILABLE"
    }

    /**
     * API 通信タイムアウト。
     *
     * 接続、読込、書込のいずれかでタイムアウトした場合に使用する。
     *
     * @property cause 元になった例外。
     */
    data class Timeout(
        override val cause: Throwable? = null
    ) : ApiError {
        override val code = "API_TIMEOUT"
    }

    /**
     * HTTP エラーレスポンス。
     *
     * 401 / 403 / 404 など専用分類していない HTTP エラー、
     * またはステータスコードを保持したい一般的な API エラーで使用する。
     *
     * @property statusCode HTTP ステータスコード。
     * @property errorBody API から返却されたエラーボディ。
     */
    data class HttpError(
        val statusCode: Int,
        val errorBody: String? = null
    ) : ApiError {
        override val code = "API_HTTP_ERROR"
        override val cause: Throwable? = null
    }

    /**
     * 認証エラー。
     *
     * HTTP 401 Unauthorized を表す。
     */
    data object Unauthorized : ApiError {
        override val code = "API_UNAUTHORIZED"
        override val cause: Throwable? = null
    }

    /**
     * 認可エラー。
     *
     * HTTP 403 Forbidden を表す。
     */
    data object Forbidden : ApiError {
        override val code = "API_FORBIDDEN"
        override val cause: Throwable? = null
    }

    /**
     * リソース未存在エラー。
     *
     * HTTP 404 Not Found を表す。
     */
    data object NotFound : ApiError {
        override val code = "API_NOT_FOUND"
        override val cause: Throwable? = null
    }

    /**
     * 成功レスポンスだが Body が存在しない状態。
     *
     * API 仕様上 Body が必要なエンドポイントで、
     * response.body() が null の場合に使用する。
     */
    data object EmptyBody : ApiError {
        override val code = "API_EMPTY_BODY"
        override val cause: Throwable? = null
    }

    /**
     * API レスポンス解析失敗。
     *
     * JSON 形式不正、型変換失敗、必須項目不足など、
     * レスポンスを期待するモデルへ変換できなかった場合に使用する。
     *
     * @property cause 元になった例外。
     */
    data class ParseError(
        override val cause: Throwable? = null
    ) : ApiError {
        override val code = "API_PARSE_ERROR"
    }

    /**
     * 既存分類に当てはまらない API 通信エラー。
     *
     * @property cause 元になった例外。
     */
    data class Unknown(
        override val cause: Throwable? = null
    ) : ApiError {
        override val code = "API_UNKNOWN"
    }
}