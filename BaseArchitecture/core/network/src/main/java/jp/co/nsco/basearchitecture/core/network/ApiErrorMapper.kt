package jp.co.nsco.basearchitecture.core.network

import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import retrofit2.HttpException
import retrofit2.Response

/**
 * Retrofit / OkHttp の失敗情報を ApiError へ変換する Mapper。
 *
 * 本クラスは、HTTP レスポンスや通信例外を、
 * API 通信領域のエラー分類である ApiError に変換する責務を持つ。
 *
 * ■ 提供する責務
 *   HTTP エラーレスポンスの ApiError 変換
 *   通信例外の ApiError 変換
 *   HTTP ステータスコード別の分類
 *
 * ■ 設計上の意図
 *   Retrofit / OkHttp の Response / Throwable を上位層へ直接渡さず、
 *   API 通信として意味のある失敗理由へ変換する。
 *
 *   エラー分類を本クラスへ集約することで、
 *   ApiResponseHandler や Repository 側にステータスコード分岐を散らさない。
 */
class ApiErrorMapper @Inject constructor() {

    /**
     * HTTP エラーレスポンスを ApiError へ変換する。
     *
     * 代表的なステータスコードは専用の ApiError として扱い、
     * それ以外は HttpError としてステータスコードと errorBody を保持する。
     *
     * @param response Retrofit の HTTP レスポンス。
     * @return HTTP ステータスコードに対応する ApiError。
     */
    fun <T> fromResponse(response: Response<T>): ApiError {
        return when (response.code()) {
            401 -> ApiError.Unauthorized
            403 -> ApiError.Forbidden
            404 -> ApiError.NotFound
            else -> ApiError.HttpError(
                statusCode = response.code(),
                errorBody = response.errorBody()?.string()
            )
        }
    }

    /**
     * 通信処理中に発生した Throwable を ApiError へ変換する。
     *
     * SocketTimeoutException は Timeout、
     * IOException はネットワーク利用不可、
     * HttpException は HTTP エラーとして扱う。
     *
     * @param throwable 通信処理中に発生した例外。
     * @return 例外内容に対応する ApiError。
     */
    fun fromThrowable(throwable: Throwable): ApiError {
        return when (throwable) {
            is SocketTimeoutException -> ApiError.Timeout(throwable)
            is IOException -> ApiError.NetworkUnavailable(throwable)
            is HttpException -> ApiError.HttpError(
                statusCode = throwable.code(),
                errorBody = throwable.message()
            )
            else -> ApiError.Unknown(throwable)
        }
    }
}