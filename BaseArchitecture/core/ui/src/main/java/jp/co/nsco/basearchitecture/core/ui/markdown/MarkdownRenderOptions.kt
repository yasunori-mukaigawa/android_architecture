package jp.co.nsco.basearchitecture.core.ui.markdown

/**
 * Markdown 表示時の描画オプション。
 *
 * 本クラスは、MarkdownDocumentView で表示する Markdown の
 * 操作可否に関する設定を保持する。
 *
 * ■ 提供する責務
 *   Link 有効/無効設定の保持
 *   テキスト選択可否設定の保持
 *   既定オプションの提供
 *
 * ■ 設計上の意図
 *   Markdown 表示の振る舞いを Composable の引数として個別に増やしすぎず、
 *   MarkdownRenderOptions としてまとめて扱う。
 *
 *   これにより、将来的に画像表示、表表示、見出しスタイルなどの設定が増えても、
 *   呼び出し側の引数肥大化を抑えられる。
 *
 * @property enableLinks true の場合、Markdown 内のリンクをタップ可能にする。
 * @property selectable true の場合、表示テキストを選択可能にする。
 */
data class MarkdownRenderOptions(
    val enableLinks: Boolean = true,
    val selectable: Boolean = true
) {
    companion object {

        /**
         * 標準の Markdown 表示オプション。
         *
         * 既定ではリンクを有効にし、テキスト選択も可能にする。
         */
        val Default = MarkdownRenderOptions()
    }
}