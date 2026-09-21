package jp.co.nsco.basearchitecture.feature.sample.presentation.history

import jp.co.nsco.basearchitecture.core.architecture.UiMessage

/**
 * Sample History 画面の State 更新理由。
 *
 * SampleHistoryUiMessage は、ViewModel から Reducer へ渡す
 * State 更新のためのメッセージを表す。
 *
 * ■ 提供する責務
 *   読み込み開始の通知
 *   読み込み成功の通知
 *   読み込み失敗の通知
 *   検索文字列更新の通知
 *   フィルタ更新の通知
 *
 * ■ 設計上の意図
 *   Event と State 更新を直接結びつけず、
 *   Message を経由して Reducer に状態更新を依頼する。
 *
 *   これにより、ViewModel は処理の流れを制御し、
 *   Reducer は Message に基づく純粋な State 更新に集中できる。
 */
sealed interface SampleHistoryUiMessage : UiMessage {

    /**
     * 操作ログ読み込み開始。
     */
    data object LoadStarted : SampleHistoryUiMessage

    /**
     * 操作ログ読み込み成功。
     *
     * @property items 画面表示用に変換済みの履歴Item一覧。
     */
    data class LoadSucceeded(
        val items: List<SampleHistoryItemUiState>
    ) : SampleHistoryUiMessage

    /**
     * 操作ログ読み込み失敗。
     *
     * @property message 画面に表示するエラーメッセージ。
     */
    data class LoadFailed(
        val message: String
    ) : SampleHistoryUiMessage

    /**
     * 検索文字列更新。
     *
     * @property query 更新後の検索文字列。
     */
    data class SearchQueryUpdated(
        val query: String
    ) : SampleHistoryUiMessage

    /**
     * フィルタ更新。
     *
     * @property filter 更新後の履歴フィルタ。
     */
    data class FilterUpdated(
        val filter: SampleHistoryFilter
    ) : SampleHistoryUiMessage
}