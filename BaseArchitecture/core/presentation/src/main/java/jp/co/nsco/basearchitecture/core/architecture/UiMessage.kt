package jp.co.nsco.basearchitecture.core.architecture

/**
 * State 更新理由を表すマーカーインターフェース。
 *
 * UiMessage は、UseCase の処理結果や ViewModel 内の判断結果を、
 * Reducer が解釈できる状態更新イベントとして表現する。
 *
 * ■ 設計上の意図
 *   Event と State 更新を直接結び付けず、
 *   Message を経由させることで状態変更の理由を明確にする。
 *
 *   Reducer は UiMessage を受け取り、
 *   現在の State から次の State を生成する。
 *
 * ■ 例
 *   InitializeStarted
 *   InitializeSucceeded
 *   InitializeFailed
 *   ValidationUpdated
 */
interface UiMessage