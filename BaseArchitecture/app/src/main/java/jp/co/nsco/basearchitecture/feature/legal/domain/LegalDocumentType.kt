package jp.co.nsco.basearchitecture.feature.legal.domain

/**
 * Navigationで扱う法務文書の種類。
 *
 * Step 01ではRoute引数を型付きで扱うために利用する。
 * 文書本文の取得や表示は後続課題で追加する。
 */
enum class LegalDocumentType(
    private val routeValue: String
) {
    /** プライバシーポリシー。 */
    PrivacyPolicy("privacy_policy"),

    /** 利用規約。 */
    TermsOfService("terms_of_service");

    /** Route引数に使用する安定した文字列を返す。 */
    fun toRouteValue(): String = routeValue

    companion object {
        /** Route引数を法務文書種別へ変換する。 */
        fun fromRouteValue(value: String): LegalDocumentType? {
            return entries.firstOrNull { it.routeValue == value }
        }
    }
}
