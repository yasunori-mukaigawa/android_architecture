package jp.co.nsco.basearchitecture.feature.legal.presentation

import javax.inject.Inject
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.error.AppError
import jp.co.nsco.basearchitecture.core.resource.StringProvider

/**
 * 法務文書画面向けのエラー表示文言 Mapper。
 *
 * LegalDocumentUiErrorMapper は、AppError を法務文書画面で表示する
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
class LegalDocumentUiErrorMapper @Inject constructor(
    private val stringProvider: StringProvider
) {

    /**
     * エラーDialogのタイトルを取得する。
     *
     * @return 法務文書画面で使用するエラータイトル。
     */
    fun title(): String {
        return stringProvider.getString(R.string.legal_error_title)
    }

    /**
     * AppError を法務文書画面向けの表示メッセージへ変換する。
     *
     * LocalStorage は raw resource 読み取り失敗などを想定し、
     * 文書読み込み失敗メッセージへ変換する。
     *
     * その他のエラーは予期しないエラーとして扱う。
     *
     * @param error 変換対象の AppError。
     * @return 画面表示用メッセージ。
     */
    fun message(error: AppError): String {
        return when (error) {
            is AppError.LocalStorage -> {
                stringProvider.getString(R.string.legal_error_read_failed)
            }

            else -> {
                stringProvider.getString(R.string.legal_error_unexpected)
            }
        }
    }
}