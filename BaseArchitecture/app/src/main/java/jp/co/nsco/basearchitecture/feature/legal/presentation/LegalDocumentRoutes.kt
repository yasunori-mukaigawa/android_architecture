package jp.co.nsco.basearchitecture.feature.legal.presentation

import jp.co.nsco.basearchitecture.core.navigation.asRouteArgument
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocumentType

/**
 * Legal Feature の Navigation route 定義。
 *
 * LegalDocumentRoutes は、法務文書画面に関する route 文字列と
 * route 生成処理を集約する。
 *
 * ■ 提供する責務
 *   route argument 名の定義
 *   法務文書画面 route pattern の定義
 *   LegalDocumentType から遷移先 route への変換
 *
 * ■ 設計上の意図
 *   route 文字列を呼び出し側に直書きすると、
 *   typo や argument 名の不一致が発生しやすくなる。
 *
 *   LegalDocumentRoutes に集約することで、
 *   Navigation 定義と遷移先生成の対応関係を一箇所で管理する。
 *
 * ■ 注意
 *   route argument に埋め込む値は asRouteArgument() で encode する。
 *   これにより、route 構文を壊す文字が混入した場合の事故を避ける。
 */
object LegalDocumentRoutes {

    /**
     * 法務文書種別を受け渡す route argument 名。
     */
    const val ArgType = "legalDocumentType"

    /**
     * 法務文書画面の route pattern。
     */
    const val Document = "legal/{$ArgType}"

    /**
     * 指定された法務文書種別を表示するための route を生成する。
     *
     * @param type 表示対象の法務文書種別。
     * @return Navigation に渡す route 文字列。
     */
    fun document(type: LegalDocumentType): String {
        return "legal/${type.toRouteValue().asRouteArgument()}"
    }
}