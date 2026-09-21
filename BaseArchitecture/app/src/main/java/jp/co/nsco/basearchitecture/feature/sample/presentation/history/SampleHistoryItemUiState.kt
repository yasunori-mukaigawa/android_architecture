package jp.co.nsco.basearchitecture.feature.sample.presentation.history

/**
 * Sample History 画面の履歴1件分の表示状態。
 *
 * SampleHistoryItemUiState は、OperationLog を画面表示しやすい形に変換した
 * Presentation 層の ItemState。
 *
 * ■ 提供する責務
 *   履歴IDの保持
 *   表示タイトルの保持
 *   表示概要の保持
 *   表示時刻の保持
 *   日付グループラベルの保持
 *   フィルタ分類の保持
 *   エラー表示対象かどうかの保持
 *
 * ■ 設計上の意図
 *   Composable が OperationLog の日時変換や分類判定を直接行わず、
 *   画面表示に必要な値だけを参照できるようにする。
 *
 * @property id 履歴ID。
 * @property title 表示タイトル。
 * @property summary 表示概要。
 * @property timeText 表示用時刻文字列。
 * @property groupLabel 日付グループラベル。
 * @property filter History画面上のフィルタ分類。
 * @property isError エラー表示として強調するかどうか。
 */
data class SampleHistoryItemUiState(
    val id: Long,
    val title: String,
    val summary: String,
    val timeText: String,
    val groupLabel: String,
    val filter: SampleHistoryFilter,
    val isError: Boolean
)