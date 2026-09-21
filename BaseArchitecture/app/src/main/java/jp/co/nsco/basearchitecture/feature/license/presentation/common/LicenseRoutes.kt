package jp.co.nsco.basearchitecture.feature.license.presentation.common

import jp.co.nsco.basearchitecture.core.navigation.asRouteArgument

/**
 * License Feature の Navigation route 定義。
 *
 * LicenseRoutes は、ライセンス一覧画面・ライセンス詳細画面に関する
 * route 文字列と route 生成処理を集約する。
 *
 * ■ 提供する責務
 *   ライセンス一覧画面 route の定義
 *   ライセンス詳細画面 route pattern の定義
 *   ライセンスID route argument 名の定義
 *   ライセンスIDから詳細画面 route への変換
 *
 * ■ 設計上の意図
 *   route 文字列を呼び出し側に直書きすると、
 *   typo や argument 名の不一致が発生しやすくなる。
 *
 *   LicenseRoutes に集約することで、
 *   Navigation 定義と遷移先生成の対応関係を一箇所で管理する。
 *
 * ■ 注意
 *   licenseId は route argument に埋め込むため、asRouteArgument() で encode する。
 *   これにより、スラッシュやクエリ文字などが混入した場合でも
 *   route 構文が壊れることを避けられる。
 */
object LicenseRoutes {

    /**
     * ライセンス一覧画面の route。
     */
    const val List = "license"

    /**
     * ライセンスIDを受け渡す route argument 名。
     */
    const val ArgLicenseId = "licenseId"

    /**
     * ライセンス詳細画面の route pattern。
     */
    const val Detail = "license/{$ArgLicenseId}"

    /**
     * 指定されたライセンスIDの詳細画面へ遷移するための route を生成する。
     *
     * @param licenseId 表示対象のライセンスID。
     * @return Navigation に渡す route 文字列。
     */
    fun detail(licenseId: String): String {
        return "license/${licenseId.asRouteArgument()}"
    }
}