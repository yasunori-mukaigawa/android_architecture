package jp.co.nsco.basearchitecture.feature.sample.presentation.settings

import jp.co.nsco.basearchitecture.core.architecture.UiEffect

/**
 * Sample Settings 画面で発生する一回性の副作用。
 *
 * SampleSettingsUiEffect は、State として保持しない
 * Navigation / Snackbar / Dialog 表示などの一度きりの処理を表す。
 *
 * ■ 提供する責務
 *   Home 画面への遷移要求
 *   Snackbar 表示要求
 *   Dialog 表示要求
 *
 * ■ 設計上の意図
 *   Navigation、Snackbar、Dialog 表示を UiState に含めると、
 *   再描画や State 復元時に副作用が再実行される可能性がある。
 *
 *   UiEffect として分離することで、
 *   ViewModel から Route へ一回性の処理として通知する。
 */
sealed interface SampleSettingsUiEffect : UiEffect {

    /**
     * Sample Home 画面へ遷移する副作用。
     */
    data object NavigateHome : SampleSettingsUiEffect

    /**
     * Snackbar 表示副作用。
     *
     * @property message Snackbar に表示するメッセージ。
     */
    data class ShowSnackbar(
        val message: String
    ) : SampleSettingsUiEffect

    /**
     * Dialog 表示副作用。
     *
     * @property title Dialog タイトル。
     * @property message Dialog 本文。
     */
    data class ShowDialog(
        val title: String,
        val message: String
    ) : SampleSettingsUiEffect
}