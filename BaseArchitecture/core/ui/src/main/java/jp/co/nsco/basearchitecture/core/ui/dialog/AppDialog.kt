package jp.co.nsco.basearchitecture.core.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * アプリ共通の標準ダイアログを表示する Composable。
 *
 * 本 Composable は、DialogUiState に定義された表示情報をもとに、
 * Material3 の AlertDialog を表示する責務を持つ。
 *
 * ■ 提供する責務
 *   ダイアログタイトルの表示
 *   ダイアログメッセージの表示
 *   Positive ボタンの表示
 *   Negative ボタンの任意表示
 *   ダイアログ閉じる操作の通知
 *
 * ■ 設計上の意図
 *   各画面で AlertDialog を直接組み立てると、
 *   ボタン配置、文言表示、dismiss 処理の扱いにばらつきが出やすくなる。
 *
 *   AppDialog に標準的なダイアログ表現を集約することで、
 *   Feature 側は DialogUiState とクリックイベントだけを渡せばよい構造にする。
 *
 *   また、DialogUiState は表示内容のみを保持し、
 *   ボタン押下時の処理は onPositiveClick / onNegativeClick として外側から渡す。
 *   これにより、UI 表示状態と業務処理を分離する。
 *
 * ■ 注意
 *   本 Composable は標準的なテキストダイアログを対象とする。
 *   入力欄付き、一覧付き、独自レイアウト付きのダイアログが必要な場合は、
 *   別の専用 Composable として定義する。
 *
 * @param dialogUiState ダイアログに表示する文言情報。
 * @param onPositiveClick Positive ボタン押下時に呼び出す処理。
 * @param onDismissRequest ダイアログ外タップや戻る操作などで閉じる要求が発生した時の処理。
 * @param onNegativeClick Negative ボタン押下時に呼び出す処理。
 *                        negativeButtonText が null、または本引数が null の場合、
 *                        Negative ボタンは表示されない。
 */
@Composable
fun AppDialog(
    dialogUiState: DialogUiState,
    onPositiveClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onNegativeClick: (() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = dialogUiState.title)
        },
        text = {
            Text(text = dialogUiState.message)
        },
        confirmButton = {
            TextButton(onClick = onPositiveClick) {
                Text(text = dialogUiState.positiveButtonText)
            }
        },
        dismissButton = {
            val negativeButtonText = dialogUiState.negativeButtonText
            if (negativeButtonText != null && onNegativeClick != null) {
                TextButton(onClick = onNegativeClick) {
                    Text(text = negativeButtonText)
                }
            }
        }
    )
}