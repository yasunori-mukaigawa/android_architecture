package jp.co.nsco.basearchitecture.feature.versioninfo.presentation

import jp.co.nsco.basearchitecture.core.architecture.UiState
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.VersionCheckStatus

/**
 * VersionInfo 画面の UI 状態。
 *
 * VersionInfoUiState は、VersionInfo 画面を描画するために必要な
 * 状態を保持する。
 *
 * ■ 提供する責務
 *   表示フェーズの保持
 *   アプリ基本情報の保持
 *   現在バージョン情報の保持
 *   最新バージョン情報の保持
 *   バージョン確認状態の保持
 *   状態表示文言の保持
 *   ボタン表示条件の保持
 *   画面表示用エラーメッセージの保持
 *
 * ■ 設計上の意図
 *   UI は VersionInfoUiState の値だけを参照して画面を描画する。
 *
 *   Domain Model である VersionInfo をそのまま画面へ渡さず、
 *   Presentation 層で表示用文字列やボタン表示条件へ変換した結果を State に保持する。
 *
 *   Navigation、外部URI起動、Dialog 表示などの一回性副作用は、
 *   VersionInfoUiEffect として分離し、本 State には含めない。
 *
 * @property screenState 画面の表示フェーズ。
 * @property appName アプリ名。
 * @property description アプリ説明文。
 * @property channel 配信チャネル。
 * @property currentVersion 現在バージョン名。
 * @property latestVersion 最新バージョン名。取得できない場合は null。
 * @property buildNumber ビルド番号。
 * @property lastCheckedAt 最終確認時刻の表示文字列。
 * @property status バージョン確認状態。
 * @property statusTitle バージョン確認状態タイトル。
 * @property statusMessage バージョン確認状態メッセージ。
 * @property showOpenStoreButton ストアを開くボタンを表示するかどうか。
 * @property showRetryButton 再試行ボタンを表示するかどうか。
 * @property showOpenAppInfoButton アプリ情報ページを開くボタンを表示するかどうか。
 * @property errorMessage 画面上に表示するエラーメッセージ。
 */
data class VersionInfoUiState(
    val screenState: VersionInfoScreenState = VersionInfoScreenState.Loading,
    val appName: String = "",
    val description: String = "",
    val channel: String = "",
    val currentVersion: String = "",
    val latestVersion: String? = null,
    val buildNumber: String = "",
    val lastCheckedAt: String = "",
    val status: VersionCheckStatus = VersionCheckStatus.Latest,
    val statusTitle: String = "",
    val statusMessage: String = "",
    val showOpenStoreButton: Boolean = false,
    val showRetryButton: Boolean = false,
    val showOpenAppInfoButton: Boolean = true,
    val errorMessage: String? = null
) : UiState