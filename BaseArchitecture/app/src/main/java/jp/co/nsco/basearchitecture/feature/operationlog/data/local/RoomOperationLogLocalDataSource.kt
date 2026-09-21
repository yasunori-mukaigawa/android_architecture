package jp.co.nsco.basearchitecture.feature.operationlog.data.local

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.database.DatabaseExecutor
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.feature.operationlog.data.mapper.toDomain
import jp.co.nsco.basearchitecture.feature.operationlog.data.mapper.toEntity
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLog
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogId
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Room を利用する OperationLogLocalDataSource 実装。
 *
 * 本クラスは、OperationLogDao を利用して操作ログを保存・取得し、
 * Entity / Domain 変換と DB例外処理を行う。
 *
 * ■ 提供する責務
 *   Room Dao 呼び出し
 *   操作ログの購読
 *   操作ログの取得
 *   操作ログの保存
 *   操作ログの削除
 *   Entity / Domain 変換
 *   DB例外の AppResult 化
 *
 * ■ 設計上の意図
 *   Repository に Room / Dao / Entity / DB例外処理を漏らさない。
 *
 *   DB処理の try-catch、Flow.catch、AppError.LocalStorage 変換は
 *   core.database の DatabaseExecutor に委譲する。
 *
 *   本クラスは Feature 固有の Dao 呼び出しと Entity / Domain 変換に集中する。
 *
 * @param dao 操作ログテーブルへアクセスする Dao。
 * @param databaseExecutor DB処理を安全実行する Executor。
 */
class RoomOperationLogLocalDataSource @Inject constructor(
    private val dao: OperationLogDao,
    private val databaseExecutor: DatabaseExecutor
) : OperationLogLocalDataSource {

    /**
     * 直近の操作ログを購読する。
     *
     * Dao から取得した Entity の Flow を Domain Model の Flow へ変換し、
     * DatabaseExecutor により AppResult の Flow として返す。
     *
     * @param limit 取得上限件数。
     * @return 操作ログ一覧の購読結果。
     */
    override fun observeRecent(
        limit: Int
    ): Flow<AppResult<List<OperationLog>>> {
        return databaseExecutor.observe(OperationLogDatabaseErrorCode.ObserveFailed) {
            dao.observeRecent(limit)
                .map { entities ->
                    entities.map { entity -> entity.toDomain() }
                }
        }
    }

    /**
     * 直近の操作ログを取得する。
     *
     * @param limit 取得上限件数。
     * @return 操作ログ一覧の取得結果。
     */
    override suspend fun findRecent(
        limit: Int
    ): AppResult<List<OperationLog>> {
        return databaseExecutor.execute(OperationLogDatabaseErrorCode.GetRecentFailed) {
            dao.findRecent(limit)
                .map { entity -> entity.toDomain() }
        }
    }

    /**
     * 指定種別の操作ログを取得する。
     *
     * @param type 操作ログ種別。
     * @param limit 取得上限件数。
     * @return 操作ログ一覧の取得結果。
     */
    override suspend fun findByType(
        type: OperationLogType,
        limit: Int
    ): AppResult<List<OperationLog>> {
        return databaseExecutor.execute(OperationLogDatabaseErrorCode.GetByTypeFailed) {
            dao.findByType(
                type = type.name,
                limit = limit
            ).map { entity ->
                entity.toDomain()
            }
        }
    }

    /**
     * 操作ログを保存する。
     *
     * Domain Model を Entity へ変換して保存し、
     * 保存された OperationLogId を返す。
     *
     * ■ 注意
     *   Dao.insert が Long を返す場合は、DB採番されたIDを OperationLogId として返す。
     *   既存の log.id をそのまま使う設計の場合は、戻り値の組み立てを log.id に変更する。
     *
     * @param log 保存する操作ログ。
     * @return 保存された操作ログID。
     */
    override suspend fun save(
        log: OperationLog
    ): AppResult<OperationLogId> {
        return databaseExecutor.execute(OperationLogDatabaseErrorCode.SaveFailed) {
            OperationLogId(dao.insert(log.toEntity()))
        }
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
        return databaseExecutor.execute(OperationLogDatabaseErrorCode.DeleteOldFailed) {
            dao.deleteOlderThan(thresholdMillis)
        }
    }

    /**
     * 操作ログを全件削除する。
     *
     * @return 削除件数。
     */
    override suspend fun clear(): AppResult<Int> {
        return databaseExecutor.execute(OperationLogDatabaseErrorCode.ClearFailed) {
            dao.deleteAll()
        }
    }
}