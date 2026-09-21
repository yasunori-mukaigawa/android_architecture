package jp.co.nsco.basearchitecture.core.theme

/**
 * アプリ内で利用するテーマ種別を識別する ID。
 *
 * AppThemeId は、色定義や表示トーンなど、
 * アプリ独自テーマを切り替えるための識別子として使用する。
 *
 * ■ 提供する責務
 *   テーマ ID の型付け
 *   既定テーマ ID の提供
 *   サンプルテーマ ID の提供
 *
 * ■ 設計上の意図
 *   テーマ ID を String のまま扱うと、
 *   他の文字列値との取り違えや typo が発生しやすくなる。
 *
 *   AppThemeId として型付けすることで、
 *   テーマ識別子であることをコード上で明確にする。
 *
 * @property value テーマを識別する文字列。
 */
@JvmInline
value class AppThemeId(
    val value: String
) {
    companion object {

        /**
         * アプリ標準のテーマ ID。
         *
         * 保存済みテーマ ID が存在しない場合や、
         * 不正な値を補正する場合の既定値として使用する。
         */
        val Default = AppThemeId("default")

        /**
         * ダッシュボード向けテーマ ID。
         *
         * サンプルまたは用途別テーマ切り替えの例として使用する。
         */
        val Dashboard = AppThemeId("dashboard")
    }
}