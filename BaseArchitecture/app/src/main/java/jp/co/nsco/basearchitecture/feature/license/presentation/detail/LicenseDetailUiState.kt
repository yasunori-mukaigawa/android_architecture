package jp.co.nsco.basearchitecture.feature.license.presentation.detail

import jp.co.nsco.basearchitecture.core.architecture.UiState

/**
 * ライセンス詳細画面の UI 状態。
 *
 * LicenseDetailUiState は、ライセンス詳細画面を描画するために必要な
 * 状態を保持する。
 *
 * ■ 提供する責務
 *   読み込み中状態の保持
 *   ライブラリ名の保持
 *   ライセンス種別名の保持
 *   著作権表記の保持
 *   ライセンス本文の保持
 *   エラーメッセージの保持
 *
 * ■ 設計上の意図
 *   UI は LicenseDetailUiState の値だけを参照して画面を描画する。
 *
 *   Navigation や Dialog 表示などの一回性の副作用は、
 *   LicenseDetailUiEffect として分離し、本 State には含めない。
 *
 * @property isLoading 読み込み中かどうか。
 * @property name ライブラリ名。
 * @property licenseName ライセンス種別名。
 * @property copyright 著作権表記。
 * @property licenseText ライセンス本文。
 * @property errorMessage エラー時に画面上へ表示するメッセージ。
 */
data class LicenseDetailUiState(
    val isLoading: Boolean = true,
    val name: String = "",
    val licenseName: String = "",
    val copyright: String = "",
    val licenseText: String = "",
    val errorMessage: String? = null
) : UiState