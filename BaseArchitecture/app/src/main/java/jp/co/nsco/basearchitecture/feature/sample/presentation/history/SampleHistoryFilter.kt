package jp.co.nsco.basearchitecture.feature.sample.presentation.history

/**
 * Sample History 画面の表示フィルタ。
 *
 * SampleHistoryFilter は、操作履歴一覧に表示する項目の絞り込み条件を表す。
 *
 * ■ 提供する責務
 *   全件表示条件
 *   操作ログ表示条件
 *   設定変更ログ表示条件
 *   エラーログ表示条件
 *
 * ■ 設計上の意図
 *   UI 上のフィルタ選択状態を enum として型付けし、
 *   文字列や数値による条件分岐を避ける。
 *
 *   Domain の OperationLogType をそのまま UI フィルタとして使わず、
 *   Sample History 画面で必要な表示単位に変換して扱う。
 */
enum class SampleHistoryFilter {

    /**
     * 全ての履歴を表示する。
     */
    All,

    /**
     * 操作系の履歴を表示する。
     */
    Action,

    /**
     * 設定変更系の履歴を表示する。
     */
    Setting,

    /**
     * エラー系の履歴を表示する。
     */
    Error
}