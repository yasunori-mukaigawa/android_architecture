package jp.co.nsco.basearchitecture.feature.license.presentation.detail

import jp.co.nsco.basearchitecture.core.architecture.UiMessage

/**
 * ライセンス詳細画面の State 更新理由。
 *
 * LicenseDetailUiMessage は、ViewModel から Reducer へ渡す
 * State 更新のためのメッセージを表す。
 *
 * ■ 提供する責務
 *   読み込み開始の通知
 *   読み込み成功の通知
 *   読み込み失敗の通知
 *
 * ■ 設計上の意図
 *   Event と State 更新を直接結びつけず、
 *   Message を経由して Reducer に状態更新を依頼する。
 *
 *   これにより、ViewModel は処理の流れを制御し、
 *   Reducer は Message に基づく純粋な State 更新に集中できる。
 */
sealed interface LicenseDetailUiMessage : UiMessage {

    /**
     * ライセンス詳細の読み込み開始。
     */
    data object LoadStarted : LicenseDetailUiMessage

    /**
     * ライセンス詳細の読み込み成功。
     *
     * @property name ライブラリ名。
     * @property licenseName ライセンス種別名。
     * @property copyright 著作権表記。
     * @property licenseText ライセンス本文。
     */
    data class LoadSucceeded(
        val name: String,
        val licenseName: String,
        val copyright: String,
        val licenseText: String
    ) : LicenseDetailUiMessage

    /**
     * ライセンス詳細の読み込み失敗。
     *
     * @property message 画面に表示するエラーメッセージ。
     */
    data class LoadFailed(
        val message: String
    ) : LicenseDetailUiMessage
}