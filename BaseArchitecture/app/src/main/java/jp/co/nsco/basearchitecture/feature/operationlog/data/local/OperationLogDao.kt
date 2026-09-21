package jp.co.nsco.basearchitecture.feature.operationlog.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * 操作ログテーブルへアクセスする Room Dao。
 *
 * OperationLogDao は、operation_logs テーブルに対する
 * 取得・購読・追加・削除の SQL を定義する。
 *
 * ■ 提供する責務
 *   直近操作ログの購読
 *   直近操作ログの取得
 *   種別指定による操作ログ取得
 *   操作ログの追加
 *   古い操作ログの削除
 *   操作ログの全削除
 *
 * ■ 設計上の意図
 *   本 Dao は Room / SQL によるデータアクセスのみを担当する。
 *
 *   Entity / Domain 変換、AppResult 化、DB例外処理は担当せず、
 *   RoomOperationLogLocalDataSource と DatabaseExecutor に委譲する。
 *
 *   これにより、Dao は SQL 定義に集中し、
 *   DataSource は Feature 固有のデータアクセス手順を扱える。
 */
@Dao
interface OperationLogDao {

    /**
     * 直近の操作ログを購読する。
     *
     * operation_logs テーブルが更新されると、
     * Room により最新の一覧が再emitされる。
     *
     * 並び順は発生日時の降順とし、同一時刻の場合は id の降順で安定化する。
     *
     * @param limit 取得上限件数。
     * @return 直近操作ログEntity一覧の Flow。
     */
    @Query(
        """
        SELECT * FROM operation_logs
        ORDER BY occurred_at_millis DESC, id DESC
        LIMIT :limit
        """
    )
    fun observeRecent(limit: Int): Flow<List<OperationLogEntity>>

    /**
     * 直近の操作ログを一度だけ取得する。
     *
     * 並び順は発生日時の降順とし、同一時刻の場合は id の降順で安定化する。
     *
     * @param limit 取得上限件数。
     * @return 直近操作ログEntity一覧。
     */
    @Query(
        """
        SELECT * FROM operation_logs
        ORDER BY occurred_at_millis DESC, id DESC
        LIMIT :limit
        """
    )
    suspend fun findRecent(limit: Int): List<OperationLogEntity>

    /**
     * 指定された操作ログ種別のログを取得する。
     *
     * type は OperationLogType.name を保存した値を想定する。
     * 並び順は発生日時の降順とし、同一時刻の場合は id の降順で安定化する。
     *
     * @param type 操作ログ種別を表す保存値。
     * @param limit 取得上限件数。
     * @return 指定種別に一致する操作ログEntity一覧。
     */
    @Query(
        """
        SELECT * FROM operation_logs
        WHERE type = :type
        ORDER BY occurred_at_millis DESC, id DESC
        LIMIT :limit
        """
    )
    suspend fun findByType(
        type: String,
        limit: Int
    ): List<OperationLogEntity>

    /**
     * 操作ログを追加する。
     *
     * id は autoGenerate で採番される。
     *
     * @param entity 追加する操作ログEntity。
     * @return Room により採番された row id。
     */
    @Insert
    suspend fun insert(entity: OperationLogEntity): Long

    /**
     * 指定された境界時刻より古い操作ログを削除する。
     *
     * occurred_at_millis が thresholdMillis 未満のログを削除対象とする。
     *
     * @param thresholdMillis 削除対象とする境界時刻。
     * @return 削除件数。
     */
    @Query(
        """
        DELETE FROM operation_logs
        WHERE occurred_at_millis < :thresholdMillis
        """
    )
    suspend fun deleteOlderThan(thresholdMillis: Long): Int

    /**
     * 操作ログを全件削除する。
     *
     * @return 削除件数。
     */
    @Query("DELETE FROM operation_logs")
    suspend fun deleteAll(): Int
}