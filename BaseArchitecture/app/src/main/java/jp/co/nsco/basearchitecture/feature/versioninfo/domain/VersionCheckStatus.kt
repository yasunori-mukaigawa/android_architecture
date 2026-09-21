package jp.co.nsco.basearchitecture.feature.versioninfo.domain

/**
 * バージョン確認結果の状態。
 *
 * VersionCheckStatus は、現在のアプリバージョンと最新バージョン情報を比較した結果、
 * VersionInfo 画面でどの状態として扱うかを表す。
 *
 * ■ 提供する責務
 *   最新状態の表現
 *   更新可能状態の表現
 *   バージョン確認失敗状態の表現
 *
 * ■ 設計上の意図
 *   バージョン比較結果を Boolean や文字列ではなく enum として表すことで、
 *   Presentation 層が状態ごとの表示を明確に分岐できるようにする。
 */
enum class VersionCheckStatus {

    /**
     * 現在のアプリが最新である。
     */
    Latest,

    /**
     * 現在のアプリより新しいバージョンが存在する。
     */
    UpdateAvailable,

    /**
     * 最新バージョン確認に失敗した。
     *
     * 現在のアプリ情報は表示可能だが、
     * 最新バージョン情報は取得できていない状態。
     */
    CheckFailed
}