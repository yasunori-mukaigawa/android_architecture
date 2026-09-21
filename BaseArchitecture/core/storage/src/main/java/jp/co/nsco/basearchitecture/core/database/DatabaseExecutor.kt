package jp.co.nsco.basearchitecture.core.database

import jp.co.nsco.basearchitecture.core.result.AppResult
import kotlinx.coroutines.flow.Flow

/**
 * Database 処理を安全に実行するための契約。
 *
 * DatabaseExecutor は、DBアクセス時の try-catch、
 * Flow購読時の例外処理、AppResult変換、トランザクション実行を共通化する。
 *
 * ■ 提供する責務
 *   DB処理の安全実行
 *   DB例外の AppResult.Failure 変換
 *   DB Flow の AppResult 化
 *   トランザクション付きDB処理の安全実行
 *
 * ■ 設計上の意図
 *   各 LocalDataSource / Repository が毎回 try-catch や Flow.catch を書くと、
 *   エラーコード、失敗時の戻り値、トランザクション境界がばらつきやすくなる。
 *
 *   DatabaseExecutor に集約することで、
 *   DBアクセスの書き方を統一する。
 */
interface DatabaseExecutor {

    /**
     * DB処理を実行し、結果を AppResult として返す。
     *
     * @param operation DB操作種別。
     * @param block 実行するDB処理。
     * @return 成功時は block の戻り値、失敗時は AppError.LocalStorage。
     */
    suspend fun <T> execute(
        operation: DatabaseOperation,
        block: suspend () -> T
    ): AppResult<T>

    /**
     * 指定したエラーコードで DB処理を実行し、結果を AppResult として返す。
     *
     * Feature 固有のエラーコードを使いたい場合に利用する。
     *
     * @param errorCode 失敗時に使用するエラーコード。
     * @param block 実行するDB処理。
     * @return 成功時は block の戻り値、失敗時は AppError.LocalStorage。
     */
    suspend fun <T> execute(
        errorCode: String,
        block: suspend () -> T
    ): AppResult<T>

    /**
     * DB購読処理を AppResult の Flow として返す。
     *
     * Flow の値は AppResult.Success として流し、
     * 購読中に例外が発生した場合は AppResult.Failure を emit する。
     *
     * @param operation DB操作種別。
     * @param source 購読対象のDB Flow。
     * @return AppResult に変換された Flow。
     */
    fun <T> observe(
        operation: DatabaseOperation,
        source: () -> Flow<T>
    ): Flow<AppResult<T>>

    /**
     * 指定したエラーコードで DB購読処理を AppResult の Flow として返す。
     *
     * Feature 固有のエラーコードを使いたい場合に利用する。
     *
     * @param errorCode 失敗時に使用するエラーコード。
     * @param source 購読対象のDB Flow。
     * @return AppResult に変換された Flow。
     */
    fun <T> observe(
        errorCode: String,
        source: () -> Flow<T>
    ): Flow<AppResult<T>>

    /**
     * DBトランザクション内で処理を実行し、結果を AppResult として返す。
     *
     * 標準の Transaction エラーコードを使用する。
     *
     * @param block トランザクション内で実行するDB処理。
     * @return 成功時は block の戻り値、失敗時は AppError.LocalStorage。
     */
    suspend fun <T> executeInTransaction(
        block: suspend () -> T
    ): AppResult<T>

    /**
     * 指定したエラーコードで DBトランザクション内の処理を実行する。
     *
     * Feature 固有のトランザクション失敗コードを使いたい場合に利用する。
     *
     * @param errorCode 失敗時に使用するエラーコード。
     * @param block トランザクション内で実行するDB処理。
     * @return 成功時は block の戻り値、失敗時は AppError.LocalStorage。
     */
    suspend fun <T> executeInTransaction(
        errorCode: String,
        block: suspend () -> T
    ): AppResult<T>
}