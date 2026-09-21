package jp.co.nsco.basearchitecture.feature.versioninfo.presentation

/**
 * VersionInfo 画面の表示状態。
 *
 * VersionInfoScreenState は、VersionInfo 画面が現在どの表示フェーズにあるかを表す。
 *
 * ■ 提供する責務
 *   読み込み中状態の表現
 *   読み込み完了状態の表現
 *   エラー状態の表現
 *
 * ■ 設計上の意図
 *   Loading / Loaded / Error を enum として明示することで、
 *   Screen 側の表示分岐を分かりやすくする。
 */
enum class VersionInfoScreenState {

    /**
     * 読み込み中。
     */
    Loading,

    /**
     * 読み込み完了。
     */
    Loaded,

    /**
     * 読み込み失敗。
     */
    Error
}