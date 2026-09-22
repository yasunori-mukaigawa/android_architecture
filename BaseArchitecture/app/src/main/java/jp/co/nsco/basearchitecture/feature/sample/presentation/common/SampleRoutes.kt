package jp.co.nsco.basearchitecture.feature.sample.presentation.common

/**
 * Sample Featureで利用するNavigation Routeを定義する。
 *
 * Route文字列を画面やGraphへ直書きせず、このオブジェクトへ集約する。
 */
object SampleRoutes {
    /** Sample Home画面のRoute。 */
    const val List = "sample"

    /** Sample History画面のRoute。 */
    const val History = "sample/history"

    /** Sample Settings画面のRoute。 */
    const val Settings = "sample/settings"
}
