package jp.co.nsco.basearchitecture.feature.sample.presentation.home

import jp.co.nsco.basearchitecture.core.architecture.UiEffect

/**
 * Sample Home 画面で発生する一回性の副作用。
 *
 * SampleUiEffect は、State として保持しない
 * Dialog 表示などの一度きりの処理を表す。
 *
 * ■ 提供する責務
 *   Dialog 表示要求
 *
 * ■ 設計上の意図
 *   Dialog 表示を UiState に含めると、
 *   再描画や State 復元時に意図せず再表示される可能性がある。
 *
 *   UiEffect として分離することで、
 *   ViewModel から Route へ一回性の処理として通知する。
 */
sealed interface SampleUiEffect : UiEffect {

    /**
     * Dialog 表示副作用。
     *
     * @property title Dialog タイトル。
     * @property message Dialog 本文。
     */
    data class ShowDialog(
        val title: String,
        val message: String
    ) : SampleUiEffect
}