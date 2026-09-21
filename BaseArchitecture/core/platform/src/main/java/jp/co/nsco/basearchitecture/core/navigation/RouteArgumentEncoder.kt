package jp.co.nsco.basearchitecture.core.navigation

import android.net.Uri

/**
 * 文字列を Navigation route argument として安全に利用できる形へ変換する。
 *
 * 本関数は、画面遷移時に route parameter として渡す文字列を
 * Uri.encode によりエンコードする。
 *
 * ■ 提供する責務
 *   route argument 用文字列のエンコード
 *   Navigation route の構文破壊防止
 *   呼び出し側での Uri.encode 直書き防止
 *
 * ■ 設計上の意図
 *   Navigation route は `/`、`?`、`&`、`=` などの文字を構文として扱う。
 *   そのため、ユーザー入力値や外部データをそのまま route に埋め込むと、
 *   意図しない route 分割や parameter 解釈が発生する可能性がある。
 *
 *   route に埋め込む値は本関数を通してエンコードし、
 *   Navigation Graph 側では decode された値として扱う前提にする。
 *
 * @return Navigation route argument として安全に埋め込めるエンコード済み文字列。
 */
fun String.asRouteArgument(): String {
    return Uri.encode(this)
}