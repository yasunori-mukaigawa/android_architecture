package jp.co.nsco.basearchitecture.core.resource

import androidx.annotation.StringRes

/**
 * 文字列リソースを取得するための契約。
 *
 * 本インターフェースは、Android の string resource 取得処理を抽象化し、
 * 呼び出し側が Context / Resources へ直接依存しないようにする。
 *
 * ■ 提供する責務
 *   string resource 取得の抽象化
 *   format argument 付き string resource 取得の抽象化
 *   Android Context 依存の隠蔽
 *
 * ■ 設計上の意図
 *   ViewModel、MessageBuilder、ErrorResolver などの Presentation ロジックが
 *   Android Context を直接保持しないようにする。
 *
 *   文字列取得を StringProvider 経由にすることで、
 *   Unit テスト時に FakeStringProvider を使って文言取得を制御できる。
 *
 * ■ 注意
 *   本契約は Android の string resource を取得するための入口であり、
 *   文言の選択ロジックそのものは MessageBuilder / ErrorResolver などに置く。
 */
interface StringProvider {

    /**
     * 指定された string resource を取得する。
     *
     * @param resId 取得対象の string resource ID。
     * @return string resource に定義された文字列。
     */
    fun getString(@StringRes resId: Int): String

    /**
     * format argument を適用して string resource を取得する。
     *
     * @param resId 取得対象の string resource ID。
     * @param args format 指定へ埋め込む値。
     * @return format argument 適用後の文字列。
     */
    fun getString(@StringRes resId: Int, vararg args: Any): String
}