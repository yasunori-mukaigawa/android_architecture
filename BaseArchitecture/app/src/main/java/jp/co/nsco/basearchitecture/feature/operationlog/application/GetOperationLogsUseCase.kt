package jp.co.nsco.basearchitecture.feature.operationlog.application

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLog
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogQueryPolicy
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogRepository

/**
 * 操作ログ一覧を取得する UseCase。
 *
 * 本 UseCase は、直近の操作ログ一覧を取得する
 * アプリケーション操作を表す。
 *
 * ■ 提供する責務
 *   操作ログ一覧取得処理の入口
 *   取得件数の補正
 *   Repository への直近ログ取得委譲
 *   取得結果の AppResult 返却
 *
 * ■ 設計上の意図
 *   ViewModel や呼び出し側が、取得件数の上限・下限などの
 *   クエリ条件補正ルールを直接持たないようにする。
 *
 *   取得件数の正規化は OperationLogQueryPolicy に委譲し、
 *   UseCase は「補正済みの条件で Repository から取得する」という
 *   アプリケーション操作に集中する。
 *
 * ■ 注意
 *   本 UseCase は一度だけ現在の操作ログ一覧を取得する。
 *   DB更新に追従して購読したい場合は ObserveOperationLogsUseCase を使用する。
 *
 * @param repository 操作ログの永続化操作を行う Repository。
 * @param queryPolicy 操作ログ取得条件を補正する Policy。
 */
class GetOperationLogsUseCase @Inject constructor(
    private val repository: OperationLogRepository,
    private val queryPolicy: OperationLogQueryPolicy
) {

    /**
     * 直近の操作ログ一覧を取得する。
     *
     * @param limit 取得上限件数。未指定時は DefaultOperationLogLimit を使用する。
     * @return 操作ログ一覧の取得結果。
     */
    suspend operator fun invoke(
        limit: Int = DefaultOperationLogLimit
    ): AppResult<List<OperationLog>> {
        return repository.getRecent(
            limit = queryPolicy.normalizeLimit(limit)
        )
    }
}