package jp.co.nsco.basearchitecture.feature.license.presentation.detail

import jp.co.nsco.basearchitecture.core.architecture.UiEvent

/**
 * ライセンス詳細画面で発生するユーザー操作・画面イベント。
 *
 * LicenseDetailUiEvent は、View から ViewModel へ通知する
 * 「画面で何が起きたか」を表す。
 *
 * ■ 提供する責務
 *   初期表示イベントの表現
 *   戻る操作の表現
 *
 * ■ 設計上の意図
 *   View は UseCase や State 更新処理を直接呼び出さず、
 *   Event として ViewModel へ通知する。
 *
 *   ViewModel は受け取った Event を解釈し、
 *   UseCase 呼び出し、Message dispatch、Effect 発行を行う。
 */
sealed interface LicenseDetailUiEvent : UiEvent {

    /**
     * 画面初期化イベント。
     *
     * route argument から渡されたライセンスIDをもとに、
     * ライセンス詳細の読み込みを開始する。
     *
     * @property id 表示対象のライセンスID。
     */
    data class Initialize(
        val id: String
    ) : LicenseDetailUiEvent

    /**
     * 戻る操作イベント。
     *
     * 画面上の戻るボタンなどから通知される。
     */
    data object BackClicked : LicenseDetailUiEvent
}