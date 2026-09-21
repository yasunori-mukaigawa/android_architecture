package jp.co.nsco.basearchitecture.feature.sample.presentation.home

import jp.co.nsco.basearchitecture.core.architecture.UiState

/**
 * Sample Home 画面の UI 状態。
 *
 * SampleUiState は、Sample Home 画面を描画するために必要な
 * 状態を保持する。
 *
 * ■ 提供する責務
 *   読み込み中状態の保持
 *   挨拶表示文言の保持
 *   日付表示文字列の保持
 *
 * ■ 設計上の意図
 *   UI は SampleUiState の値だけを参照して画面を描画する。
 *
 *   Domain Model である SampleHomeHeader をそのまま画面へ渡さず、
 *   Presentation 層で表示用文字列へ変換した結果を State に保持する。
 *
 *   Navigation や Dialog 表示などの一回性の副作用は、
 *   SampleUiEffect として分離し、本 State には含めない。
 *
 * @property isLoading 読み込み中かどうか。
 * @property greetingMessage 表示用の挨拶文言。
 * @property dateText 表示用の日付文字列。
 */
data class SampleUiState(
    val isLoading: Boolean,
    val greetingMessage: String = "",
    val dateText: String = ""
) : UiState {

    companion object {

        /**
         * Sample Home 画面の初期状態を生成する。
         *
         * 初期状態では、まだ表示用ヘッダー情報を取得していないため、
         * 挨拶文言と日付文字列は空文字として扱う。
         *
         * @return 初期状態。
         */
        fun initial(): SampleUiState {
            return SampleUiState(
                isLoading = false,
                greetingMessage = "",
                dateText = ""
            )
        }
    }
}