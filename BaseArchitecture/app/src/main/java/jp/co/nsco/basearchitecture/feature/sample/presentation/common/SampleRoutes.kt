package jp.co.nsco.basearchitecture.feature.sample.presentation.common

/**
 * Sample Feature の Navigation route 定義。
 *
 * SampleRoutes は、Sample Feature 内の各画面に対応する
 * route 文字列を集約する。
 *
 * ■ 提供する責務
 *   Sample Home 画面 route の定義
 *   Sample History 画面 route の定義
 *   Sample Settings 画面 route の定義
 *
 * ■ 設計上の意図
 *   route 文字列を呼び出し側に直書きすると、
 *   typo や画面構成変更時の修正漏れが起きやすくなる。
 *
 *   SampleRoutes に集約することで、
 *   Navigation 定義と遷移先指定の対応関係を一箇所で管理する。
 */
object SampleRoutes {

    /**
     * Sample Home 画面の route。
     */
    const val List = "sample"

    /**
     * Sample History 画面の route。
     */
    const val History = "sample/history"

    /**
     * Sample Settings 画面の route。
     */
    const val Settings = "sample/settings"
}