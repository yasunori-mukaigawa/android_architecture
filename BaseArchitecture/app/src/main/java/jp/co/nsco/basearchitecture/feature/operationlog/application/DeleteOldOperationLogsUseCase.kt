package jp.co.nsco.basearchitecture.feature.operationlog.application

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.core.time.AppClock
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogRepository
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogRetentionPolicy

/**
 * 古い操作ログを削除する UseCase。
 *
 * 本 UseCase は、指定された保持日数をもとに削除境界時刻を算出し、
 * その時刻より古い操作ログを削除するアプリケーション操作を表す。
 *
 * ■ 提供する責務
 *   操作ログ削除処理の入口
 *   現在時刻の取得
 *   保持日数から削除境界時刻への変換
 *   Repository への古いログ削除委譲
 *   削除件数の AppResult 返却
 *
 * ■ 設計上の意図
 *   「何日前より古いログを削除するか」というアプリケーション操作を
 *   UseCase として表現する。
 *
 *   現在時刻の取得は AppClock に委譲し、
 *   System.currentTimeMillis() を直接参照しない。
 *
 *   保持日数から削除境界時刻を算出するルールは
 *   OperationLogRetentionPolicy に委譲することで、
 *   UseCase は処理手順の組み立てに集中する。
 *
 * ■ 注意
 *   retentionDays の補正や不正値の扱いは OperationLogRetentionPolicy の責務とする。
 *   本 UseCase は算出された thresholdMillis を Repository へ渡す。
 *
 * @param repository 操作ログの永続化操作を行う Repository。
 * @param appClock 現在時刻を取得する Clock。
 * @param retentionPolicy 操作ログ保持期間に関する Policy。
 */
class DeleteOldOperationLogsUseCase @Inject constructor(
    private val repository: OperationLogRepository,
    private val appClock: AppClock,
    private val retentionPolicy: OperationLogRetentionPolicy
) {

    /**
     * 指定された保持日数より古い操作ログを削除する。
     *
     * @param retentionDays 操作ログを保持する日数。
     * @return 削除された操作ログ件数。
     */
    suspend operator fun invoke(retentionDays: Int): AppResult<Int> {
        val thresholdMillis = retentionPolicy.calculateThresholdMillis(
            nowMillis = appClock.nowMillis(),
            retentionDays = retentionDays
        )

        return repository.deleteOlderThan(thresholdMillis)
    }
}