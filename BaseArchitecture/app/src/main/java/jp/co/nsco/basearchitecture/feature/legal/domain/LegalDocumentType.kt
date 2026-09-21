package jp.co.nsco.basearchitecture.feature.legal.domain

/**
 * 法務文書種別。
 *
 * LegalDocumentType は、アプリで扱う法務文書の種類を表す。
 *
 * ■ 提供する責務
 *   プライバシーポリシー種別の表現
 *   利用規約種別の表現
 *   Navigation route 用文字列への変換
 *   Navigation route 用文字列からの復元
 *
 * ■ 設計上の意図
 *   法務文書を文字列だけで扱うと、文書種別の typo や不正値混入が起きやすい。
 *
 *   enum class として型付けすることで、
 *   UseCase / Repository / Presentation 間で安全に文書種別を受け渡せるようにする。
 *
 *   また、Navigation route に enum の name を直接使うと、
 *   enum 名の変更が route value の変更になってしまう。
 *
 *   そのため、route 用の値は routeValue として明示的に定義し、
 *   enum 名と route 互換性を分離する。
 *
 * ■ 注意
 *   routeValue は Navigation route 引数として使われる安定値である。
 *   外部リンク、保存済み状態、DeepLink などと連携する可能性がある場合、
 *   安易に変更しないこと。
 *
 * @property routeValue Navigation route 引数として使用する安定した文字列。
 */
enum class LegalDocumentType(
    private val routeValue: String
) {

    /**
     * プライバシーポリシー。
     */
    PrivacyPolicy("privacy_policy"),

    /**
     * 利用規約。
     */
    TermsOfService("terms_of_service");

    /**
     * Navigation route 引数として利用する文字列へ変換する。
     *
     * enum の name ではなく、明示的に定義した routeValue を返す。
     *
     * @return route 引数用の文字列。
     */
    fun toRouteValue(): String {
        return routeValue
    }

    companion object {

        /**
         * Navigation route 引数の文字列から LegalDocumentType を復元する。
         *
         * 不明な値が渡された場合は null を返す。
         *
         * @param value route 引数から取得した文字列。
         * @return 対応する LegalDocumentType。存在しない場合は null。
         */
        fun fromRouteValue(value: String): LegalDocumentType? {
            return entries.firstOrNull { type ->
                type.routeValue == value
            }
        }
    }
}