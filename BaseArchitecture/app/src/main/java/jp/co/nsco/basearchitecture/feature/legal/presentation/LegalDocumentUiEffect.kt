package jp.co.nsco.basearchitecture.feature.legal.presentation

import jp.co.nsco.basearchitecture.core.architecture.UiEffect

/**
 * 法務文書画面で発生する一回性の副作用。
 *
 * LegalDocumentUiEffect は、State として保持しない
 * Navigation や Dialog 表示などの一度きりの処理を表す。
 *
 * ■ 提供する責務
 *   戻る遷移の表現
 *   Dialog 表示要求の表現
 *
 * ■ 設計上の意図
 *   Navigation や Dialog 表示を UiState に含めると、
 *   再描画や State 復元時に副作用が再実行される可能性がある。
 *
 *   UiEffect として分離することで、
 *   ViewModel から Route へ一回性の処理として通知する。
 */
sealed interface LegalDocumentUiEffect : UiEffect {

    /**
     * 前画面へ戻る副作用。
     */
    data object NavigateBack : LegalDocumentUiEffect

    /**
     * Dialog 表示副作用。
     *
     * @property title Dialog タイトル。
     * @property message Dialog 本文。
     */
    data class ShowDialog(
        val title: String,
        val message: String
    ) : LegalDocumentUiEffect
}