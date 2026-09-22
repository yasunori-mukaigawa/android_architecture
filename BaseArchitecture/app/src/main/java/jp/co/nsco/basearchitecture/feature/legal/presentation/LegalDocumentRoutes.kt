package jp.co.nsco.basearchitecture.feature.legal.presentation

import jp.co.nsco.basearchitecture.core.navigation.asRouteArgument
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocumentType

/** 法務文書FeatureのNavigation Routeを定義する。 */
object LegalDocumentRoutes {
    /** 法務文書種別のRoute引数名。 */
    const val ArgType = "legalDocumentType"

    /** 法務文書画面のRouteパターン。 */
    const val Document = "legal/{$ArgType}"

    /** 指定した法務文書種別のRouteを生成する。 */
    fun document(type: LegalDocumentType): String {
        return "legal/${type.toRouteValue().asRouteArgument()}"
    }
}
