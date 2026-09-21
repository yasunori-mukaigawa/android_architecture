package jp.co.nsco.basearchitecture.feature.legal.application

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocument
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocumentRepository
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocumentType

/**
 * 法務文書を取得する UseCase。
 *
 * 本 UseCase は、指定された LegalDocumentType に対応する法務文書を取得する
 * アプリケーション操作を表す。
 *
 * ■ 提供する責務
 *   法務文書取得処理の入口
 *   取得対象文書種別の受け取り
 *   Repository への文書取得委譲
 *   取得結果の AppResult 返却
 *
 * ■ 設計上の意図
 *   画面や ViewModel が、法務文書の保存場所や取得方法を直接知らないようにする。
 *
 *   文書が raw resource、asset、ローカルDB、API のどこに存在するかは
 *   Repository 実装側に閉じ込める。
 *
 *   UseCase は「指定された種別の法務文書を取得する」という
 *   アプリケーション上の操作だけを公開する。
 *
 * ■ 注意
 *   本 UseCase は文書内容の解釈や Markdown 描画は行わない。
 *   取得した LegalDocument をどのように表示するかは Presentation 層の責務とする。
 *
 * @param repository 法務文書取得を抽象化する Repository。
 */
class GetLegalDocumentUseCase @Inject constructor(
    private val repository: LegalDocumentRepository
) {

    /**
     * 指定された種別の法務文書を取得する。
     *
     * @param type 取得対象の法務文書種別。
     * @return 法務文書の取得結果。
     */
    suspend operator fun invoke(type: LegalDocumentType): AppResult<LegalDocument> {
        return repository.getDocument(type)
    }
}