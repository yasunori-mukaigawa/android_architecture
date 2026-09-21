package jp.co.nsco.basearchitecture.core.theme

/**
 * アプリの明暗テーマ切り替えモード。
 *
 * 本 enum は、端末設定に追従するか、
 * アプリ側で Light / Dark を固定するかを表す。
 *
 * ■ 設計上の意図
 *   OS のテーマ設定とアプリ独自設定を分離し、
 *   画面側が boolean の darkMode フラグだけに依存しないようにする。
 *
 *   System / Light / Dark を明示的な選択肢として扱うことで、
 *   設定画面やテーマ適用処理で分岐しやすくする。
 */
enum class AppThemeMode {

    /**
     * 端末のシステムテーマ設定に追従する。
     */
    System,

    /**
     * Light テーマを使用する。
     */
    Light,

    /**
     * Dark テーマを使用する。
     */
    Dark
}