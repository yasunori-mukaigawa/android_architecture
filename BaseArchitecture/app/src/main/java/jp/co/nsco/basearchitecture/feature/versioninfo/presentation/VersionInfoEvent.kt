package jp.co.nsco.basearchitecture.feature.versioninfo.presentation

import jp.co.nsco.basearchitecture.core.architecture.UiEvent

/**
 * VersionInfo 画面で発生する UI イベント。
 *
 * VersionInfoEvent は、View から ViewModel へ通知する
 * 「画面で何が起きたか」を表す。
 *
 * ■ 提供する責務
 *   初期表示イベントの表現
 *   戻る操作イベントの表現
 *   再試行操作イベントの表現
 *   ストアページを開く操作イベントの表現
 *   アプリ情報ページを開く操作イベントの表現
 *
 * ■ 設計上の意図
 *   View は UseCase や State 更新処理を直接呼び出さず、
 *   Event として BaseViewModel の sendEvent へ通知する。
 *
 *   ViewModel は受け取った Event を handleEvent で解釈し、
 *   UseCase 呼び出し、Message dispatch、Effect 発行を行う。
 */
sealed interface VersionInfoEvent : UiEvent {

    /**
     * 画面表示時イベント。
     *
     * VersionInfo の取得を ViewModel へ依頼する。
     */
    data object OnAppear : VersionInfoEvent

    /**
     * 戻るボタン押下イベント。
     */
    data object BackClicked : VersionInfoEvent

    /**
     * 再試行ボタン押下イベント。
     */
    data object RetryClicked : VersionInfoEvent

    /**
     * ストアページを開くボタン押下イベント。
     */
    data object OpenStoreClicked : VersionInfoEvent

    /**
     * アプリ情報ページを開くボタン押下イベント。
     */
    data object OpenAppInfoPageClicked : VersionInfoEvent
}
