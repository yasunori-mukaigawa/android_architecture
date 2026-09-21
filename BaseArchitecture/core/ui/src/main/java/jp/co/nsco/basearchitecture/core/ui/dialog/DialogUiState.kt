package jp.co.nsco.basearchitecture.core.ui.dialog

/**
 * ダイアログ表示に必要な UI 状態。
 *
 * DialogUiState は、ダイアログに表示する文言情報のみを保持する。
 *
 * ■ 提供する責務
 *   タイトル文言の保持
 *   メッセージ文言の保持
 *   Positive ボタン文言の保持
 *   Negative ボタン文言の任意保持
 *
 * ■ 設計上の意図
 *   ダイアログ表示に必要な情報を State としてまとめ、
 *   Composable 側が表示内容を判断しやすくする。
 *
 *   本 State は表示情報だけを持ち、
 *   ボタン押下時の処理や業務判断は保持しない。
 *   処理は ViewModel / Effect / 呼び出し元 Composable 側で扱う。
 *
 * ■ 注意
 *   negativeButtonText が null の場合、Negative ボタンは表示しない想定である。
 *   Negative ボタンを表示する場合は、AppDialog 側へ onNegativeClick も渡す必要がある。
 *
 * @property title ダイアログタイトル。
 * @property message ダイアログ本文。
 * @property positiveButtonText Positive ボタンに表示する文言。
 * @property negativeButtonText Negative ボタンに表示する文言。表示しない場合は null。
 */
data class DialogUiState(
    val title: String,
    val message: String,
    val positiveButtonText: String,
    val negativeButtonText: String? = null
)