package jp.co.nsco.basearchitecture.core.network

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.error.AppError
import jp.co.nsco.basearchitecture.core.result.AppResult
import kotlinx.coroutines.CancellationException
import retrofit2.Response

/**
 * Retrofit の API 呼び出し結果を AppResult へ変換する Handler。
 *
 * 本クラスは、Retrofit の Response と通信例外を受け止め、
 * アプリ共通の結果型である AppResult へ変換する責務を持つ。
 *
 * ■ 提供する責務
 *   API 呼び出しの実行
 *   HTTP 成功/失敗判定
 *   空 Body 判定
 *   通信例外の捕捉
 *   ApiError から AppError への変換
 *
 * ■ 設計上の意図
 *   Repository / Gateway が毎回 try-catch や response.isSuccessful 判定を
 *   実装しなくて済むよう、API レスポンス処理を共通化する。
 *
 *   Retrofit 固有の Response / Throwable は本クラスで受け止め、
 *   上位層へは AppResult.Success / AppResult.Failure として返す。
 *
 * ■ 注意
 *   本 Handler は Response<T> を返す API を対象とする。
 *   Body が不要な API や、204 No Content を正常扱いしたい API は、
 *   別の handleUnit などを追加して責務を分けることを検討する。
 *
 * @param errorMapper Response / Throwable を ApiError へ変換する Mapper。
 */
class ApiResponseHandler @Inject constructor(
    private val errorMapper: ApiErrorMapper
) {

    /**
     * API 呼び出しを実行し、AppResult へ変換する。
     *
     * HTTP ステータスが成功ではない場合は AppResult.Failure を返す。
     * HTTP ステータスが成功でも Body が null の場合は EmptyBody として扱う。
     * 通信中に例外が発生した場合は、例外内容に応じた AppError を返す。
     *
     * @param call Retrofit API 呼び出し処理。
     * @return API 呼び出し結果。成功時は Body、失敗時は AppError を返す。
     */
    suspend fun <T : Any> handle(call: suspend () -> Response<T>): AppResult<T> {
        return try {
            val response = call()

            if (!response.isSuccessful) {
                return AppResult.Failure(
                    errorMapper.fromResponse(response).toAppError()
                )
            }

            val body = response.body()
                ?: return AppResult.Failure(ApiError.EmptyBody.toAppError())

            AppResult.Success(body)
        } catch (throwable: Throwable) {
            if (throwable is CancellationException) {
                throw throwable
            }

            AppResult.Failure(
                errorMapper.fromThrowable(throwable).toAppError()
            )
        }
    }

    /**
     * ApiError をアプリ共通の AppError へ変換する。
     *
     * ApiError は API 通信領域の分類であり、
     * 上位層へ返す際は AppError として扱う。
     *
     * @return ApiError に対応する AppError。
     */
    private fun ApiError.toAppError(): AppError {
        return when (this) {
            is ApiError.NetworkUnavailable -> AppError.Network(
                code = code,
                cause = cause
            )

            is ApiError.Timeout -> AppError.Network(
                code = code,
                cause = cause
            )

            is ApiError.HttpError -> AppError.Api(
                code = code,
                statusCode = statusCode,
                reason = errorBody
            )

            ApiError.Unauthorized -> AppError.Api(
                code = code,
                statusCode = 401
            )

            ApiError.Forbidden -> AppError.Api(
                code = code,
                statusCode = 403
            )

            ApiError.NotFound -> AppError.Api(
                code = code,
                statusCode = 404
            )

            ApiError.EmptyBody -> AppError.ApiInvalidResponse(
                code = code,
                reason = "Empty body"
            )

            is ApiError.ParseError -> AppError.ApiInvalidResponse(
                code = code,
                reason = cause?.message.orEmpty(),
                cause = cause
            )

            is ApiError.Unknown -> AppError.Unexpected(
                code = code,
                cause = cause
            )
        }
    }
}
