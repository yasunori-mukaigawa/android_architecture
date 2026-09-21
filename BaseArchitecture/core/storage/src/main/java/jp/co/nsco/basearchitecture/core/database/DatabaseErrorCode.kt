package jp.co.nsco.basearchitecture.core.database

/**
 * Database 処理で使用する共通エラーコード。
 *
 * 本 object は、DBアクセス失敗を AppError.LocalStorage へ変換する際の
 * エラーコードを集約する。
 *
 * ■ 設計上の意図
 *   DB関連のエラーコードを文字列直書きすると、
 *   typo や分類ゆれが発生しやすくなる。
 *
 *   DatabaseErrorCode に集約することで、
 *   LocalDataSource / Repository / TransactionRunner / Executor で
 *   同じエラーコードを利用できるようにする。
 */
object DatabaseErrorCode {

    /**
     * Database 読み取り失敗。
     */
    const val ReadFailed = "DATABASE_READ_FAILED"

    /**
     * Database 書き込み失敗。
     */
    const val WriteFailed = "DATABASE_WRITE_FAILED"

    /**
     * Database 削除失敗。
     */
    const val DeleteFailed = "DATABASE_DELETE_FAILED"

    /**
     * Database トランザクション失敗。
     */
    const val TransactionFailed = "DATABASE_TRANSACTION_FAILED"

    /**
     * Database 初期化失敗。
     */
    const val InitializeFailed = "DATABASE_INITIALIZE_FAILED"

    /**
     * Database 操作種別から標準エラーコードへ変換する。
     *
     * @param operation DB操作種別。
     * @return 操作種別に対応する標準エラーコード。
     */
    fun fromOperation(operation: DatabaseOperation): String {
        return when (operation) {
            DatabaseOperation.Read -> ReadFailed
            DatabaseOperation.Write -> WriteFailed
            DatabaseOperation.Delete -> DeleteFailed
            DatabaseOperation.Transaction -> TransactionFailed
            DatabaseOperation.Initialize -> InitializeFailed
        }
    }
}