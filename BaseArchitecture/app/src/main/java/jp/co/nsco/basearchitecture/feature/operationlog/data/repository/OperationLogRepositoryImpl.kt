package jp.co.nsco.basearchitecture.feature.operationlog.data.repository

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.feature.operationlog.data.local.OperationLogLocalDataSource
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLog
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogId
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogRepository
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogType
import kotlinx.coroutines.flow.Flow

/**
 * OperationLogRepository の実装。
 *
 * 本クラスは、操作ログに関する Domain Repository 契約を満たし、
 * ローカルデータソースへ処理を委譲する責務を持つ。
 *
 * ■ 提供する責務
 *   操作ログの購読
 *   操作ログの取得
 *   操作ログの保存
 *   操作ログの削除
 *   Domain Repository 契約の実装
 *
 * ■ 設計上の意図
 *   本 Repository は、Domain 層から見た操作ログの永続化入口である。
 *
 *   DBアクセス、Room例外処理、AppError変換は OperationLogLocalDataSource と
 *   core.database の DatabaseExecutor に委譲し、
 *   Repository 自身は Domain Repository としての委譲に集中する。
 *
 *   Repository が try-catch や AppError.LocalStorage 生成を持たないことで、
 *   DB処理のエラーハンドリング方針を core.database に統一できる。
 *
 * @param localDataSource 操作ログのローカルデータアクセスを行う DataSource。
 */
class OperationLogRepositoryImpl @Inject constructor(
    private val localDataSource: OperationLogLocalDataSource
) : OperationLogRepository {

    /**
     * 直近の操作ログを購読する。
     *
     * @param limit 取得上限件数。
     * @return 操作ログ一覧の購読結果。
     */
    override fun observeRecent(
        limit: Int
    ): Flow<AppResult<List<OperationLog>>> {
        return localDataSource.observeRecent(limit)
    }

    /**
     * 直近の操作ログを取得する。
     *
     * @param limit 取得上限件数。
     * @return 操作ログ一覧の取得結果。
     */
    override suspend fun getRecent(
        limit: Int
    ): AppResult<List<OperationLog>> {
        return localDataSource.findRecent(limit)
    }

    /**
     * 指定種別の操作ログを取得する。
     *
     * @param type 操作ログ種別。
     * @param limit 取得上限件数。
     * @return 操作ログ一覧の取得結果。
     */
    override suspend fun getByType(
        type: OperationLogType,
        limit: Int
    ): AppResult<List<OperationLog>> {
        return localDataSource.findByType(
            type = type,
            limit = limit
        )
    }

    /**
     * 操作ログを保存する。
     *
     * @param log 保存する操作ログ。
     * @return 保存された操作ログID。
     */
    override suspend fun save(
        log: OperationLog
    ): AppResult<OperationLogId> {
        return localDataSource.save(log)
    }

    /**
     * 指定時刻より古い操作ログを削除する。
     *
     * @param thresholdMillis 削除対象とする境界時刻。
     * @return 削除件数。
     */
    override suspend fun deleteOlderThan(
        thresholdMillis: Long
    ): AppResult<Int> {
        return localDataSource.deleteOlderThan(thresholdMillis)
    }

    /**
     * 操作ログを全件削除する。
     *
     * @return 削除件数。
     */
    override suspend fun clear(): AppResult<Int> {
        return localDataSource.clear()
    }
}