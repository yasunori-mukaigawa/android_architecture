package jp.co.nsco.basearchitecture.feature.legal.data

import javax.inject.Inject
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.resource.RawResourceReader
import jp.co.nsco.basearchitecture.core.resource.StringProvider
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.core.result.map
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocument
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocumentRepository
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocumentType

/**
 * raw resource を利用する LegalDocumentRepository 実装。
 *
 * 本クラスは、アプリに同梱された raw resource から法務文書の Markdown を読み取り、
 * Domain Model である LegalDocument に変換する責務を持つ。
 *
 * ■ 提供する責務
 *   法務文書 Markdown の読み取り
 *   法務文書タイトルの解決
 *   LegalDocument の組み立て
 *   LegalDocumentRepository 契約の実装
 *
 * ■ 設計上の意図
 *   Domain / Application 層が、法務文書の保存場所を知らないようにする。
 *
 *   本実装では raw resource を文書の保存先として利用するが、
 *   UseCase や ViewModel はその詳細を意識せず、
 *   LegalDocumentRepository 契約だけに依存する。
 *
 *   raw resource の読み取りは RawResourceReader に委譲し、
 *   Android Context / Resources への直接依存を避ける。
 *
 *   表示タイトルの取得も StringProvider に委譲し、
 *   Repository 内で Context を直接扱わない。
 *
 * ■ 注意
 *   本 Repository は Markdown の取得と LegalDocument への組み立てを担当する。
 *   Markdown の描画、スクロール状態、リンククリック処理などは Presentation 層の責務とする。
 *
 * @param rawResourceReader raw resource のテキスト読み取りを行う Reader。
 * @param rawResourceMapper 法務文書種別から raw resource ID へ変換する Mapper。
 * @param stringProvider 文字列リソース取得を行う Provider。
 */
class RawLegalDocumentRepository @Inject constructor(
    private val rawResourceReader: RawResourceReader,
    private val rawResourceMapper: LegalDocumentRawResourceMapper,
    private val stringProvider: StringProvider
) : LegalDocumentRepository {

    /**
     * 指定された種別の法務文書を取得する。
     *
     * raw resource から Markdown 本文を読み取り、
     * 文書種別、表示タイトル、Markdown 本文を持つ LegalDocument として返す。
     *
     * raw resource 読み取りに失敗した場合は、
     * RawResourceReader が返す AppResult.Failure をそのまま上位へ返す。
     *
     * @param type 取得対象の法務文書種別。
     * @return 法務文書の取得結果。
     */
    override suspend fun getDocument(type: LegalDocumentType): AppResult<LegalDocument> {
        return rawResourceReader.readText(rawResourceMapper.toRawRes(type))
            .map { markdown ->
                LegalDocument(
                    type = type,
                    title = titleOf(type),
                    markdown = markdown
                )
            }
    }

    /**
     * 法務文書種別に対応する表示タイトルを取得する。
     *
     * @param type 法務文書種別。
     * @return 指定された法務文書種別に対応する表示タイトル。
     */
    private fun titleOf(type: LegalDocumentType): String {
        return when (type) {
            LegalDocumentType.PrivacyPolicy -> {
                stringProvider.getString(R.string.legal_privacy_policy_title)
            }

            LegalDocumentType.TermsOfService -> {
                stringProvider.getString(R.string.legal_terms_of_service_title)
            }
        }
    }
}