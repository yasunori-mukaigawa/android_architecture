package jp.co.nsco.basearchitecture.feature.versioninfo.presentation

import jp.co.nsco.basearchitecture.core.architecture.UiEffect
import jp.co.nsco.basearchitecture.core.external.ExternalUri

/**
 * VersionInfo 画面で発生する一回性の副作用。
 *
 * VersionInfoUiEffect は、State として保持しない
 * Navigation、外部URI起動、Dialog 表示などの一度きりの処理を表す。
 *
 * ■ 提供する責務
 *   戻る遷移要求
 *   外部URI起動要求
 *   Dialog 表示要求
 *
 * ■ 設計上の意図
 *   画面遷移や外部URI起動を UiState に含めると、
 *   再描画や State 復元時に意図せず再実行される可能性がある。
 *
 *   UiEffect として分離することで、
 *   ViewModel から Route へ一回性の処理として通知する。
 */
sealed interface VersionInfoUiEffect : UiEffect {

    /**
     * 前の画面へ戻る副作用。
     */
    data object NavigateBack : VersionInfoUiEffect

    /**
     * 外部URIを開く副作用。
     *
     * 実際の URI 起動処理は Android Context に近い責務であるため、
     * ViewModel では実行せず Route 側へ依頼する。
     *
     * @property uri 開く対象の外部URI。
     */
    data class OpenExternalUri(
        val uri: ExternalUri
    ) : VersionInfoUiEffect

    /**
     * Dialog 表示副作用。
     *
     * @property title Dialog タイトル。
     * @property message Dialog 本文。
     */
    data class ShowDialog(
        val title: String,
        val message: String
    ) : VersionInfoUiEffect
}