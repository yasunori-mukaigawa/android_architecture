package jp.co.nsco.basearchitecture.feature.legal.domain

import jp.co.nsco.basearchitecture.core.result.AppResult

/**
 * 法務文書取得の Repository 契約。
 *
 * LegalDocumentRepository は、利用規約、プライバシーポリシー、
 * OSSライセンスなどの法務文書を取得するための契約を表す。
 *
 * ■ 提供する責務
 *   法務文書取得契約の定義
 *   文書保存場所の隠蔽
 *   文書取得失敗時の AppResult 返却
 *
 * ■ 設計上の意図
 *   Application 層は、法務文書が raw resource、asset、DB、API など
 *   どの保存方式で管理されているかを知らない。
 *
 *   Repository 契約を挟むことで、文書取得方法の変更を
 *   UseCase / ViewModel / UI に波及させない。
 *
 *   取得失敗は例外を直接投げず、AppResult.Failure として返すことで、
 *   Presentation 層がエラー表示や復旧導線を一貫して扱えるようにする。
 */
interface LegalDocumentRepository {

    /**
     * 指定された種別の法務文書を取得する。
     *
     * @param type 取得対象の法務文書種別。
     * @return 法務文書の取得結果。
     */
    suspend fun getDocument(type: LegalDocumentType): AppResult<LegalDocument>
}