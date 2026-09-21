package jp.co.nsco.basearchitecture.feature.versioninfo.presentation

/**
 * VersionInfo Feature の Navigation route 定義。
 *
 * ■ 提供する責務
 *   VersionInfo 画面 route の一元管理
 *
 * ■ 設計上の意図
 *   route 文字列を直接各所に書かず、定数としてまとめることで、
 *   Navigation 定義や遷移元での typo を防ぐ。
 */
object VersionInfoRoutes {

    /**
     * VersionInfo 画面 route。
     */
    const val VersionInfo = "version_info"
}