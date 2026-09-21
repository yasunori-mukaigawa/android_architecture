package jp.co.nsco.basearchitecture.feature.operationlog.domain

import jp.co.nsco.basearchitecture.core.result.AppResult
import kotlinx.coroutines.flow.Flow

/**
 * 操作ログ永続化の Repository 契約。
 *
 * OperationLogRepository は、操作ログの保存・取得・購読・削除を行うための
 * Domain 層の契約を表す。
 *
 * ■ 提供する責務
 *   操作ログ一覧の購読契約
 *   操作ログ一覧の取得契約
 *   種別指定による取得契約
 *   操作ログ保存契約
 *   古い操作ログ削除契約
 *   操作ログ全削除契約
 *
 * ■ 設計上の意図
 *   Application 層は、操作ログが Room、DataStore、ファイル、API など
 *   どの保存方式で管理されているかを知らない。
 *
 *   Repository 契約を挟むことで、保存方式の変更を
 *   UseCase や Presentation 層へ波及させない。
 *
 *   失敗は例外を直接投げず AppResult.Failure として返し、
 *   呼び出し側が成功・失敗を一貫して扱えるようにする。
 */
interface OperationLogRepository {

    /**
     * 直近の操作ログ一覧を購読する。
     *
     * 保存内容の変更に追従して画面更新したい場合に利用する。
     *
     * @param limit 取得上限件数。
     * @return 操作ログ一覧の購読結果。
     */
    fun observeRecent(limit: Int): Flow<AppResult<List<OperationLog>>>

    /**
     * 直近の操作ログ一覧を一度だけ取得する。
     *
     * @param limit 取得上限件数。
     * @return 操作ログ一覧の取得結果。
     */
    suspend fun getRecent(limit: Int): AppResult<List<OperationLog>>

    /**
     * 指定された操作ログ種別のログを取得する。
     *
     * @param type 操作ログ種別。
     * @param limit 取得上限件数。
     * @return 指定種別に一致する操作ログ一覧。
     */
    suspend fun getByType(
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
     * 指定された境界時刻より古い操作ログを削除する。
     *
     * @param thresholdMillis 削除対象とする境界時刻。epoch millis。
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