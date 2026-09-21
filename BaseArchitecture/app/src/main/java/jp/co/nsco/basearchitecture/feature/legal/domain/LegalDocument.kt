package jp.co.nsco.basearchitecture.feature.legal.domain

/**
 * 法務文書を表す Domain Model。
 *
 * LegalDocument は、利用規約やプライバシーポリシーなど、
 * アプリ内で表示する法務文書の内容を表す。
 *
 * ■ 提供する責務
 *   法務文書種別の保持
 *   表示タイトルの保持
 *   Markdown本文の保持
 *
 * ■ 設計上の意図
 *   法務文書を単なる文字列として扱うのではなく、
 *   type / title / markdown を持つ Domain Model として表現する。
 *
 *   これにより、Presentation 層は取得した LegalDocument をもとに、
 *   画面タイトルや Markdown 表示内容を一貫した形で扱える。
 *
 * ■ 注意
 *   markdown は表示用本文であり、Markdown の描画処理そのものは持たない。
 *   MarkdownDocumentView などの Presentation / UI 部品で表示する。
 *
 * @property type 法務文書種別。
 * @property title 画面表示用タイトル。
 * @property markdown Markdown形式の本文。
 */
data class LegalDocument(
    val type: LegalDocumentType,
    val title: String,
    val markdown: String
)