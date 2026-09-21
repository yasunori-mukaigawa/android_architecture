package jp.co.nsco.basearchitecture.core.ui.markdown

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.Markwon

/**
 * Markdown 文字列を表示するための Composable。
 *
 * 本 Composable は、Markdown 形式の文字列を Markwon で描画し、
 * Compose UI 上に表示する責務を持つ。
 *
 * ■ 提供する責務
 *   Markdown 文字列の描画
 *   Link 有効/無効の切り替え
 *   テキスト選択可否の切り替え
 *   MaterialTheme に基づく文字色・文字サイズの反映
 *   Markwon / TextView 依存の隠蔽
 *
 * ■ 設計上の意図
 *   Markdown 描画は Compose 標準 Text だけでは扱いづらいため、
 *   Markwon と TextView を AndroidView 経由で利用する。
 *
 *   各画面が Markwon / TextView / LinkMovementMethod を直接扱うと、
 *   Markdown 表示方法やリンク設定にばらつきが出やすくなる。
 *
 *   MarkdownDocumentView に集約することで、
 *   Feature 側は Markdown 文字列と表示オプションだけを渡せばよい構造にする。
 *
 * ■ 注意
 *   本 Composable は Markdown の表示を目的とする。
 *   Markdown の取得、整形、業務上の文言選択は呼び出し側で行う。
 *
 *   空文字を Markwon に渡すと表示領域が不安定になる場合があるため、
 *   markdown が blank の場合は空白文字を渡す。
 *
 * @param markdown 表示する Markdown 文字列。
 * @param modifier この Composable 全体に適用する Modifier。
 * @param options Markdown 表示時のオプション。
 */
@Composable
fun MarkdownDocumentView(
    markdown: String,
    modifier: Modifier = Modifier,
    options: MarkdownRenderOptions = MarkdownRenderOptions.Default
) {
    val context = LocalContext.current
    val markwon = remember(context) { Markwon.create(context) }
    val textColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val textStyle = MaterialTheme.typography.bodyLarge

    AndroidView(
        modifier = modifier,
        factory = {
            TextView(it).apply {
                includeFontPadding = true
                setLineSpacing(0f, 1.15f)
            }
        },
        update = { textView ->
            textView.setTextIsSelectable(options.selectable)
            textView.setTextColor(textColor)
            textView.applyTextSize(textStyle)
            textView.movementMethod = if (options.enableLinks) {
                LinkMovementMethod.getInstance()
            } else {
                null
            }
            markwon.setMarkdown(textView, markdown.ifBlank { " " })
        }
    )
}

/**
 * MaterialTheme の TextStyle から TextView へ文字サイズを反映する。
 *
 * TextStyle.fontSize が未指定の場合は、TextView 側の既定サイズを維持する。
 *
 * ■ 設計上の意図
 *   Markdown 表示は TextView を利用するが、
 *   見た目は Compose の MaterialTheme にできるだけ寄せる。
 *
 *   ただし TextStyle には fontSize 未指定のケースがあるため、
 *   未指定値をそのまま TextView.textSize に設定しないようにする。
 *
 * @param textStyle Compose 側の文字スタイル。
 */
private fun TextView.applyTextSize(textStyle: TextStyle) {
    if (!textStyle.fontSize.isUnspecified) {
        textSize = textStyle.fontSize.value
    }
}