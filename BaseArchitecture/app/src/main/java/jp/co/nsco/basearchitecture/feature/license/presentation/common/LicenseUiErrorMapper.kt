package jp.co.nsco.basearchitecture.feature.license.presentation.common

import javax.inject.Inject
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.error.AppError
import jp.co.nsco.basearchitecture.core.resource.StringProvider

/**
 * License Feature 向けのエラー表示文言 Mapper。
 *
 * LicenseUiErrorMapper は、AppError を License 画面群で表示する
 * タイトル・メッセージへ変換する責務を持つ。
 *
 * ■ 提供する責務
 *   エラーDialogタイトルの解決
 *   AppError から画面表示メッセージへの変換
 *   文言リソース参照の集約
 *
 * ■ 設計上の意図
 *   ViewModel が string resource ID や Context を直接扱わないようにする。
 *
 *   AppError はアプリ内の失敗理由を表すモデルであり、
 *   そのまま表示文言として扱わない。
 *
 *   Presentation 層の Mapper で表示文言へ変換することで、
 *   エラー分類と画面表示の責務を分離する。
 *
 * @param stringProvider 文字列リソース取得を行う Provider。
 */
class LicenseUiErrorMapper @Inject constructor(
    private val stringProvider: StringProvider
) {

    /**
     * License Feature で使用するエラーDialogタイトルを取得する。
     *
     * @return エラータイトル。
     */
    fun title(): String {
        return stringProvider.getString(R.string.license_error_title)
    }

    /**
     * AppError を License Feature 向けの表示メッセージへ変換する。
     *
     * Validation は route argument や licenseId 不正などを想定する。
     * LocalStorage はライセンス情報の読み取り失敗や詳細未検出を想定する。
     * その他のエラーは予期しないエラーとして扱う。
     *
     * @param error 変換対象の AppError。
     * @return 画面表示用メッセージ。
     */
    fun message(error: AppError): String {
        return when (error) {
            is AppError.Validation -> {
                stringProvider.getString(R.string.license_error_invalid_argument)
            }

            is AppError.LocalStorage -> {
                stringProvider.getString(R.string.license_error_not_found)
            }

            else -> {
                stringProvider.getString(R.string.license_error_unexpected)
            }
        }
    }
}