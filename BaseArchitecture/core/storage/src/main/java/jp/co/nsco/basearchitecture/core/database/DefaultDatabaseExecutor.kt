package jp.co.nsco.basearchitecture.core.database

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.result.AppResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * DatabaseExecutor の標準実装。
 *
 * 本クラスは、DatabaseErrorMapper と DatabaseTransactionRunner を利用し、
 * DB処理を AppResult として安全に実行する責務を持つ。
 *
 * ■ 提供する責務
 *   DB処理の try-catch
 *   DB Flow の AppResult 化
 *   成功時の AppResult.Success 変換
 *   失敗時の AppResult.Failure 変換
 *   トランザクション処理の安全実行
 *
 * ■ 設計上の意図
 *   DBアクセス時の例外処理を LocalDataSource / Repository に散らさず、
 *   共通の Executor に閉じ込める。
 *
 *   これにより、Feature 側は DAO 呼び出しの本質的な処理だけを書けばよくなる。
 *
 * @param errorMapper DB例外を AppError へ変換する Mapper。
 * @param transactionRunner DBトランザクションを実行する契約。
 */
class DefaultDatabaseExecutor @Inject constructor(
    private val errorMapper: DatabaseErrorMapper,
    private val transactionRunner: DatabaseTransactionRunner
) : DatabaseExecutor {

    /**
     * DB処理を実行し、結果を AppResult として返す。
     *
     * @param operation DB操作種別。
     * @param block 実行するDB処理。
     * @return 成功時は block の戻り値、失敗時は AppError.LocalStorage。
     */
    override suspend fun <T> execute(
        operation: DatabaseOperation,
        block: suspend () -> T
    ): AppResult<T> {
        return try {
            AppResult.Success(block())
        } catch (throwable: Throwable) {
            if (throwable is CancellationException) {
                throw throwable
            }

            AppResult.Failure(
                errorMapper.fromThrowable(
                    throwable = throwable,
                    operation = operation
                )
            )
        }
    }

    /**
     * 指定したエラーコードで DB処理を実行し、結果を AppResult として返す。
     *
     * @param errorCode 失敗時に使用するエラーコード。
     * @param block 実行するDB処理。
     * @return 成功時は block の戻り値、失敗時は AppError.LocalStorage。
     */
    override suspend fun <T> execute(
        errorCode: String,
        block: suspend () -> T
    ): AppResult<T> {
        return try {
            AppResult.Success(block())
        } catch (throwable: Throwable) {
            if (throwable is CancellationException) {
                throw throwable
            }

            AppResult.Failure(
                errorMapper.fromThrowable(
                    throwable = throwable,
                    code = errorCode
                )
            )
        }
    }

    /**
     * DB購読処理を AppResult の Flow として返す。
     *
     * @param operation DB操作種別。
     * @param source 購読対象のDB Flow。
     * @return AppResult に変換された Flow。
     */
    override fun <T> observe(
        operation: DatabaseOperation,
        source: () -> Flow<T>
    ): Flow<AppResult<T>> {
        return source()
            .map<T, AppResult<T>> { value ->
                AppResult.Success(value)
            }
            .catch { throwable ->
                if (throwable is CancellationException) {
                    throw throwable
                }

                emit(
                    AppResult.Failure(
                        errorMapper.fromThrowable(
                            throwable = throwable,
                            operation = operation
                        )
                    )
                )
            }
    }

    /**
     * 指定したエラーコードで DB購読処理を AppResult の Flow として返す。
     *
     * @param errorCode 失敗時に使用するエラーコード。
     * @param source 購読対象のDB Flow。
     * @return AppResult に変換された Flow。
     */
    override fun <T> observe(
        errorCode: String,
        source: () -> Flow<T>
    ): Flow<AppResult<T>> {
        return source()
            .map<T, AppResult<T>> { value ->
                AppResult.Success(value)
            }
            .catch { throwable ->
                if (throwable is CancellationException) {
                    throw throwable
                }

                emit(
                    AppResult.Failure(
                        errorMapper.fromThrowable(
                            throwable = throwable,
                            code = errorCode
                        )
                    )
                )
            }
    }

    /**
     * DBトランザクション内で処理を実行し、結果を AppResult として返す。
     *
     * 標準の Transaction エラーコードを使用する。
     *
     * @param block トランザクション内で実行するDB処理。
     * @return 成功時は block の戻り値、失敗時は AppError.LocalStorage。
     */
    override suspend fun <T> executeInTransaction(
        block: suspend () -> T
    ): AppResult<T> {
        return execute(DatabaseOperation.Transaction) {
            transactionRunner.runInTransaction {
                block()
            }
        }
    }

    /**
     * 指定したエラーコードで DBトランザクション内の処理を実行する。
     *
     * @param errorCode 失敗時に使用するエラーコード。
     * @param block トランザクション内で実行するDB処理。
     * @return 成功時は block の戻り値、失敗時は AppError.LocalStorage。
     */
    override suspend fun <T> executeInTransaction(
        errorCode: String,
        block: suspend () -> T
    ): AppResult<T> {
        return execute(errorCode) {
            transactionRunner.runInTransaction {
                block()
            }
        }
    }
}
