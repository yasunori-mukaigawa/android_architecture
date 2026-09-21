package jp.co.nsco.basearchitecture.feature.sample.presentation.common

import javax.inject.Inject
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.error.AppError
import jp.co.nsco.basearchitecture.core.error.UiError
import jp.co.nsco.basearchitecture.core.resource.StringProvider
import jp.co.nsco.basearchitecture.core.validation.FieldValidationError

private const val SampleNameRequired = "SAMPLE-NAME-REQUIRED"
private const val SampleNameTooLong = "SAMPLE-NAME-TOO-LONG"

/**
 * Sample Feature 向けのエラー表示 Mapper。
 *
 * SampleUiErrorMapper は、AppError や FieldValidationError を
 * Sample Feature で表示する UiError / 表示メッセージへ変換する責務を持つ。
 *
 * ■ 提供する責務
 *   AppError から UiError への変換
 *   FieldValidationError から表示メッセージへの変換
 *   Sample Feature 向けエラー文言の解決
 *
 * ■ 設計上の意図
 *   AppError はアプリ内の失敗理由を表すモデルであり、
 *   そのまま画面表示文言として扱わない。
 *
 *   Presentation 層の Mapper で UiError へ変換することで、
 *   エラー分類と画面表示の責務を分離する。
 *
 *   ViewModel が string resource ID や Context を直接扱わないようにし、
 *   文言解決を本 Mapper に集約する。
 *
 * @param stringProvider 文字列リソース取得を行う Provider。
 */
class SampleUiErrorMapper @Inject constructor(
    private val stringProvider: StringProvider
) {

    /**
     * AppError を Sample Feature 向けの UiError へ変換する。
     *
     * @param error 変換対象の AppError。
     * @return 画面表示用の UiError。
     */
    fun toUiError(error: AppError): UiError {
        return when (error) {
            is AppError.Validation -> {
                UiError(
                    code = error.code,
                    title = stringProvider.getString(R.string.sample_error_title_input),
                    message = toDisplayMessage(
                        FieldValidationError(
                            field = error.field,
                            code = error.code,
                            reason = error.reason
                        )
                    ),
                    supportMessage = error.field
                )
            }

            is AppError.Business -> {
                UiError(
                    code = error.code,
                    title = stringProvider.getString(R.string.sample_error_title_business),
                    message = stringProvider.getString(R.string.sample_error_message_business),
                    supportMessage = error.reason
                )
            }

            is AppError.LocalStorage -> {
                UiError(
                    code = error.code,
                    title = stringProvider.getString(R.string.sample_error_title_storage),
                    message = stringProvider.getString(R.string.sample_error_message_storage)
                )
            }

            is AppError.Network -> {
                UiError(
                    code = error.code,
                    title = stringProvider.getString(R.string.sample_error_title_network),
                    message = stringProvider.getString(R.string.sample_error_message_network)
                )
            }

            is AppError.Api -> {
                UiError(
                    code = error.code,
                    title = stringProvider.getString(R.string.sample_error_title_api),
                    message = error.reason
                        ?: stringProvider.getString(R.string.sample_error_message_api),
                    supportMessage = stringProvider.getString(
                        R.string.sample_error_support_status_code,
                        error.statusCode
                    )
                )
            }

            is AppError.ApiInvalidResponse -> {
                UiError(
                    code = error.code,
                    title = stringProvider.getString(R.string.sample_error_title_response),
                    message = error.reason.ifBlank {
                        stringProvider.getString(R.string.sample_error_message_response)
                    }
                )
            }

            is AppError.Unexpected -> {
                UiError(
                    code = error.code,
                    title = stringProvider.getString(R.string.sample_error_title_unexpected),
                    message = stringProvider.getString(R.string.sample_error_message_unexpected)
                )
            }
        }
    }

    /**
     * FieldValidationError を表示メッセージへ変換する。
     *
     * 既知の validation code は string resource から表示文言を取得する。
     * 未知の validation code は、error.reason を fallback として使用する。
     *
     * @param error 変換対象の FieldValidationError。
     * @return 画面表示用メッセージ。
     */
    fun toDisplayMessage(error: FieldValidationError): String {
        return when (error.code) {
            SampleNameRequired -> {
                stringProvider.getString(R.string.sample_validation_name_required)
            }

            SampleNameTooLong -> {
                stringProvider.getString(R.string.sample_validation_name_too_long)
            }

            else -> {
                error.reason
            }
        }
    }

    /**
     * AppError を簡易表示メッセージへ変換する。
     *
     * Dialog や Snackbar などでタイトル付き UiError までは不要な場合に利用する。
     *
     * @param error 変換対象の AppError。
     * @return 画面表示用メッセージ。
     */
    fun toDisplayMessage(error: AppError): String {
        return when (error) {
            is AppError.LocalStorage -> {
                stringProvider.getString(R.string.sample_error_message_storage)
            }

            else -> {
                stringProvider.getString(R.string.sample_error_message_unexpected)
            }
        }
    }
}