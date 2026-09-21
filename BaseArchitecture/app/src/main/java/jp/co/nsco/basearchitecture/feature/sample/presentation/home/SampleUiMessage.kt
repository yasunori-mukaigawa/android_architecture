package jp.co.nsco.basearchitecture.feature.sample.presentation.home

import jp.co.nsco.basearchitecture.core.architecture.UiMessage
import jp.co.nsco.basearchitecture.core.error.UiError

/**
 * Sample Home 画面の State 更新理由。
 *
 * SampleUiMessage は、ViewModel から Reducer へ渡す
 * State 更新のためのメッセージを表す。
 *
 * ■ 提供する責務
 *   読み込み開始の通知
 *   Homeヘッダー更新の通知
 *   読み込み成功の通知
 *   読み込み失敗の通知
 *
 * ■ 設計上の意図
 *   Event と State 更新を直接結びつけず、
 *   Message を経由して Reducer に状態更新を依頼する。
 *
 *   これにより、ViewModel は処理の流れを制御し、
 *   Reducer は Message に基づく純粋な State 更新に集中できる。
 */
sealed interface SampleUiMessage : UiMessage {

    /**
     * Sample Home ヘッダー読み込み開始。
     */
    data object LoadStarted : SampleUiMessage

    /**
     * Sample Home ヘッダー表示値の更新。
     *
     * @property greetingMessage 表示用の挨拶文言。
     * @property dateText 表示用の日付文字列。
     */
    data class HomeHeaderUpdated(
        val greetingMessage: String,
        val dateText: String
    ) : SampleUiMessage

    /**
     * Sample Home ヘッダー読み込み成功。
     */
    data object LoadSucceeded : SampleUiMessage

    /**
     * Sample Home ヘッダー読み込み失敗。
     *
     * @property error 画面表示用に変換済みのエラー情報。
     */
    data class LoadFailed(
        val error: UiError
    ) : SampleUiMessage
}