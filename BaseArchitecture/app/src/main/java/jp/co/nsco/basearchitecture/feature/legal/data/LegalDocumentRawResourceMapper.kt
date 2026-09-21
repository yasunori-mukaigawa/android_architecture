package jp.co.nsco.basearchitecture.feature.legal.data

import androidx.annotation.RawRes
import javax.inject.Inject
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocumentType

/**
 * 法務文書種別から raw resource ID へ変換する Mapper。
 *
 * 本クラスは、LegalDocumentType とアプリ内 raw resource の対応関係を管理する。
 *
 * ■ 提供する責務
 *   法務文書種別に対応する raw resource ID の解決
 *   raw resource 参照の集約
 *
 * ■ 設計上の意図
 *   Repository が LegalDocumentType ごとの raw resource ID を直接判定すると、
 *   文書追加や resource 名変更時の修正箇所が Repository 内に散らばりやすくなる。
 *
 *   LegalDocumentRawResourceMapper に対応関係を集約することで、
 *   「どの文書種別がどの raw resource に対応するか」を一箇所で管理できるようにする。
 *
 * ■ 注意
 *   本クラスは raw resource ID への変換のみを担当する。
 *   raw resource の読み取り、Markdown の解釈、表示用タイトルの解決は担当しない。
 */
class LegalDocumentRawResourceMapper @Inject constructor() {

    /**
     * 法務文書種別に対応する raw resource ID を取得する。
     *
     * @param type 法務文書種別。
     * @return 指定された法務文書種別に対応する raw resource ID。
     */
    @RawRes
    fun toRawRes(type: LegalDocumentType): Int {
        return when (type) {
            LegalDocumentType.PrivacyPolicy -> R.raw.privacy_policy
            LegalDocumentType.TermsOfService -> R.raw.terms_of_service
        }
    }
}