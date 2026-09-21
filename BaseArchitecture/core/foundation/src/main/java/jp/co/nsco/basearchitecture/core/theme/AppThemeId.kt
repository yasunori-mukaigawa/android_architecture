package jp.co.nsco.basearchitecture.core.theme

/**
 * アプリ内で利用するテーマ種別を識別する ID。
 *
 * AppThemeId は、アプリ独自の色定義や表示トーンを識別するための型である。
 * TemplateではDefaultのみを標準提供し、案件固有のテーマはアプリ側で追加する。
 *
 * @property value テーマを識別する文字列。
 */
@JvmInline
value class AppThemeId(
    val value: String
) {
    companion object {

        /** 標準テーマの識別子。 */
        val Default = AppThemeId("default")
    }
}
