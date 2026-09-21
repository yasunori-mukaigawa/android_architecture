package jp.co.nsco.basearchitecture.feature.sample.presentation.history

import jp.co.nsco.basearchitecture.core.architecture.UiEvent

/**
 * Sample History 画面で発生するユーザー操作・画面イベント。
 *
 * SampleHistoryUiEvent は、View から ViewModel へ通知する
 * 「画面で何が起きたか」を表す。
 *
 * ■ 提供する責務
 *   初期表示イベントの表現
 *   検索文字列変更イベントの表現
 *   フィルタ選択イベントの表現
 *   Home 画面遷移操作の表現
 *   Settings 画面遷移操作の表現
 *
 * ■ 設計上の意図
 *   View は UseCase や State 更新処理を直接呼び出さず、
 *   Event として ViewModel へ通知する。
 *
 *   ViewModel は受け取った Event を解釈し、
 *   UseCase 呼び出し、Message dispatch、Effect 発行を行う。
 */
sealed interface SampleHistoryUiEvent : UiEvent {

    /**
     * 画面初期化イベント。
     *
     * 操作ログの購読開始を ViewModel へ依頼する。
     */
    data object Initialize : SampleHistoryUiEvent

    /**
     * 検索文字列変更イベント。
     *
     * @property query 入力された検索文字列。
     */
    data class SearchQueryChanged(
        val query: String
    ) : SampleHistoryUiEvent

    /**
     * フィルタ選択イベント。
     *
     * @property filter 選択された履歴フィルタ。
     */
    data class FilterSelected(
        val filter: SampleHistoryFilter
    ) : SampleHistoryUiEvent

    /**
     * Home 画面への遷移操作イベント。
     */
    data object HomeClicked : SampleHistoryUiEvent

    /**
     * Settings 画面への遷移操作イベント。
     */
    data object SettingsClicked : SampleHistoryUiEvent
}