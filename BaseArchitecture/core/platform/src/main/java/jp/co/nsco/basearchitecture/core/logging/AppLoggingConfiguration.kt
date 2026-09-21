package jp.co.nsco.basearchitecture.core.logging

/**
 * アプリ内ログの共通設定を保持する契約。
 *
 * 本契約は、Initializer で設定した既定 TAG を、
 * AppLogger の各ログ出力から参照するために使用する。
 *
 * ■ 提供する責務
 *   ログ出力時に使用する既定 TAG の提供
 *   Application 起動時の既定 TAG 設定口の提供
 *
 * ■ 設計上の意図
 *   TimberAppLoggingInitializer と AndroidAppLogger が別々に TAG を保持すると、
 *   初期化時の設定が実際のログ出力へ反映されない。
 *
 *   共通設定を一つの契約へ集約することで、Initializer で設定した TAG を
 *   各ログ出力へ一貫して適用する。
 */
interface AppLoggingConfiguration {

    /**
     * ログメソッドで TAG が指定されなかった場合に使用する既定 TAG。
     */
    val defaultTag: String

    /**
     * 既定 TAG を設定する。
     *
     * null または空白だけの値が渡された場合は、現在の設定を維持する。
     *
     * @param tag Application 起動時に設定する既定 TAG。
     */
    fun setDefaultTag(tag: String?)
}
