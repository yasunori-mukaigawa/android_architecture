package jp.co.nsco.basearchitecture.feature.operationlog.data.local

/**
 * 操作ログDB処理で使用するエラーコード。
 *
 * 本 object は、操作ログのローカル永続化処理で発生したエラーを
 * AppError.LocalStorage へ変換する際のコードを集約する。
 *
 * ■ 設計上の意図
 *   エラーコードを LocalDataSource 内に文字列直書きすると、
 *   typo や分類ゆれ、修正漏れが発生しやすくなる。
 *
 *   OperationLogDatabaseErrorCode に集約することで、
 *   操作ログDB処理の失敗理由を一箇所で管理する。
 */
object OperationLogDatabaseErrorCode {

    /**
     * 操作ログ購読失敗。
     */
    const val ObserveFailed = "operation_log_observe_failed"

    /**
     * 直近操作ログ取得失敗。
     */
    const val GetRecentFailed = "operation_log_get_recent_failed"

    /**
     * 種別指定の操作ログ取得失敗。
     */
    const val GetByTypeFailed = "operation_log_get_by_type_failed"

    /**
     * 操作ログ保存失敗。
     */
    const val SaveFailed = "operation_log_save_failed"

    /**
     * 古い操作ログ削除失敗。
     */
    const val DeleteOldFailed = "operation_log_delete_old_failed"

    /**
     * 操作ログ全削除失敗。
     */
    const val ClearFailed = "operation_log_clear_failed"
}