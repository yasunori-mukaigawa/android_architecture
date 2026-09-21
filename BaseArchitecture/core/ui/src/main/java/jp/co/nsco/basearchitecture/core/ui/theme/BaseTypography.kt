package jp.co.nsco.basearchitecture.core.ui.theme

import androidx.compose.material3.Typography

/**
 * アプリ標準の Typography。
 *
 * 本定義は、Material3 の既定 Typography をベースとして使用する。
 *
 * ■ 設計上の意図
 *   文字スタイル定義を直接 MaterialTheme に埋め込まず、
 *   テーマ定義の一部として参照できるようにする。
 *
 *   将来的にフォント、サイズ、行間、見出しスタイルなどを調整する場合は、
 *   本定義を拡張する。
 */
val DefaultTypography = Typography()