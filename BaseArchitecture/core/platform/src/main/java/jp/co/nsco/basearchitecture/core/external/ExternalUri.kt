package jp.co.nsco.basearchitecture.core.external

/**
 * 外部アプリで開く URI 情報。
 *
 * 本クラスは、外部ブラウザや外部アプリで開く URI と、
 * 起動失敗時に使用する代替 URI を保持する。
 *
 * ■ 提供する責務
 *   primary URI の保持
 *   fallback URI の保持
 *
 * ■ 設計上の意図
 *   外部 URI 起動時に、呼び出し側が fallback 処理を個別に実装しないようにする。
 *
 *   primaryUri がアプリ固有スキームで、端末に対応アプリが存在しない場合でも、
 *   fallbackUri に Web URL などを指定しておくことで復旧しやすくする。
 *
 * @property primaryUri 最初に起動を試みる URI。
 * @property fallbackUri primaryUri の起動に失敗した場合に起動を試みる URI。
 *                       fallback が不要な場合は null。
 */
data class ExternalUri(
    val primaryUri: String,
    val fallbackUri: String? = null
)