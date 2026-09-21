package jp.co.nsco.basearchitecture.feature.operationlog.data.local

import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLog
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogId
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogType
import kotlinx.coroutines.flow.Flow

/**
 * 操作ログのローカルデータアクセス契約。
 *
 * 本インターフェースは、操作ログをローカル永続化領域から
 * 取得・保存・削除するための契約を表す。
 *
 * ■ 提供する責務
 *   操作ログの購読契約
 *   操作ログの取得契約
 *   操作ログの保存契約
 *   操作ログの削除契約
 *
 * ■ 設計上の意図
 *   Repository が Room / Dao / Entity を直接知らないようにする。
 *
 *   Repository は OperationLogLocalDataSource 契約に依存し、
 *   実際の Room 実装、DB例外処理、Entity / Domain 変換は
 *   実装クラスである RoomOperationLogLocalDataSource に閉じ込める。
 *
 *   これにより、将来的に保存方式を Room 以外へ差し替える場合でも、
 *   Repository 側への影響を抑えられる。
 */
interface OperationLogLocalDataSource {

    /**
     * 直近の操作ログを購読する。
     *
     * @param limit 取得上限件数。
     * @return 操作ログ一覧の購読結果。
     */
    fun observeRecent(limit: Int): Flow<AppResult<List<OperationLog>>>

    /**
     * 直近の操作ログを取得する。
     *
     * @param limit 取得上限件数。
     * @return 操作ログ一覧の取得結果。
     */
    suspend fun findRecent(limit: Int): AppResult<List<OperationLog>>

    /**
     * 指定種別の操作ログを取得する。
     *
     * @param type 操作ログ種別。
     * @param limit 取得上限件数。
     * @return 操作ログ一覧の取得結果。
     */
    suspend fun findByType(
        type: OperationLogType,
        limit: Int
    ): AppResult<List<OperationLog>>

    /**
     * 操作ログを保存する。
     *
     * @param log 保存する操作ログ。
     * @return 保存された操作ログID。
     */
    suspend fun save(log: OperationLog): AppResult<OperationLogId>

    /**
     * 指定時刻より古い操作ログを削除する。
     *
     * @param thresholdMillis 削除対象とする境界時刻。
     * @return 削除件数。
     */
    suspend fun deleteOlderThan(thresholdMillis: Long): AppResult<Int>

    /**
     * 操作ログを全件削除する。
     *
     * @return 削除件数。
     */
    suspend fun clear(): AppResult<Int>
}