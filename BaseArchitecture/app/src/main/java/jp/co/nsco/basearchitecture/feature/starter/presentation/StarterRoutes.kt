package jp.co.nsco.basearchitecture.feature.starter.presentation

/**
 * Starter Featureで利用するRoute定義。
 *
 * StarterRoutesは、アプリ起動直後に表示する最小画面のRouteを保持する責務を持つ。
 * 案件開始時は、このRouteを初期画面のRoute定義へ置き換える。
 *
 * ■ 設計上の意図
 *   Route文字列をNavigation実装から分離し、Route名の変更箇所を限定する。
 *   引数付きRouteや画面固有の遷移は、Feature単位で追加する。
 */
object StarterRoutes {
    /** Starter画面のRoute。 */
    const val Start = "starter"
}
