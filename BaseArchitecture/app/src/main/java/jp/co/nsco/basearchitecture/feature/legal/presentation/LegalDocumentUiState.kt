package jp.co.nsco.basearchitecture.feature.legal.presentation

import jp.co.nsco.basearchitecture.core.architecture.UiState

/**
 * 法務文書画面の UI 状態。
 *
 * LegalDocumentUiState は、法務文書画面を描画するために必要な
 * 状態を保持する。
 *
 * ■ 提供する責務
 *   画面状態の保持
 *   表示タイトルの保持
 *   Markdown本文の保持
 *   エラーメッセージの保持
 *
 * ■ 設計上の意図
 *   UI は LegalDocumentUiState の値だけを参照して画面を描画する。
 *
 *   Navigation や Dialog 表示などの一回性の副作用は、
 *   LegalDocumentUiEffect として分離し、本 State には含めない。
 *
 * @property screenState 画面全体の表示状態。
 * @property title 表示タイトル。
 * @property markdown Markdown形式の本文。
 * @property errorMessage エラー時に画面上へ表示するメッセージ。
 */
data class LegalDocumentUiState(
    val screenState: LegalDocumentScreenState = LegalDocumentScreenState.Loading,
    val title: String = "",
    val markdown: String = "",
    val errorMessage: String? = null
) : UiState