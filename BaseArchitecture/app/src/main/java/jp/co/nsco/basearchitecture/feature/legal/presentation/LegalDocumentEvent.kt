package jp.co.nsco.basearchitecture.feature.legal.presentation

import jp.co.nsco.basearchitecture.core.architecture.UiEvent
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocumentType

/**
 * 法務文書画面で発生する UI イベント。
 *
 * LegalDocumentEvent は、View から ViewModel へ通知する
 * 「画面で何が起きたか」を表す。
 *
 * ■ 提供する責務
 *   初期表示イベントの表現
 *   戻る操作イベントの表現
 *   再試行操作イベントの表現
 *
 * ■ 設計上の意図
 *   View は UseCase や State 更新処理を直接呼び出さず、
 *   Event として BaseViewModel の sendEvent へ通知する。
 *
 *   ViewModel は受け取った Event を handleEvent で解釈し、
 *   UseCase 呼び出し、Message dispatch、Effect 発行を行う。
 */
sealed interface LegalDocumentEvent : UiEvent {

    /**
     * 画面表示時イベント。
     *
     * 指定された法務文書種別の読み込みを開始するために利用する。
     *
     * @property type 表示対象の法務文書種別。
     */
    data class OnAppear(
        val type: LegalDocumentType
    ) : LegalDocumentEvent

    /**
     * 戻る操作イベント。
     *
     * 画面上の戻るボタンなどから通知される。
     */
    data object BackClicked : LegalDocumentEvent

    /**
     * 再試行操作イベント。
     *
     * 読み込み失敗後に、直前に読み込もうとした文書種別で再読み込みする。
     */
    data object RetryClicked : LegalDocumentEvent
}
