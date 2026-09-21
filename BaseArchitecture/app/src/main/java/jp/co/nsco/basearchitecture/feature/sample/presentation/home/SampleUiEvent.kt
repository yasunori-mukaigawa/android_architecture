package jp.co.nsco.basearchitecture.feature.sample.presentation.home

import jp.co.nsco.basearchitecture.core.architecture.UiEvent

/**
 * Sample Home 画面で発生するユーザー操作・画面イベント。
 *
 * SampleUiEvent は、View から ViewModel へ通知する
 * 「画面で何が起きたか」を表す。
 *
 * ■ 提供する責務
 *   初期表示イベントの表現
 *
 * ■ 設計上の意図
 *   View は UseCase や State 更新処理を直接呼び出さず、
 *   Event として ViewModel へ通知する。
 *
 *   ViewModel は受け取った Event を解釈し、
 *   UseCase 呼び出し、Message dispatch、Effect 発行を行う。
 */
sealed interface SampleUiEvent : UiEvent {

    /**
     * 画面初期化イベント。
     *
     * Sample Home ヘッダー情報の取得を ViewModel へ依頼する。
     */
    data object Initialize : SampleUiEvent
}