package jp.co.nsco.basearchitecture.feature.operationlog.application

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLog
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogQueryPolicy
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogRepository
import kotlinx.coroutines.flow.Flow

/**
 * 操作ログ一覧を購読する UseCase。
 *
 * 本 UseCase は、直近の操作ログ一覧を Flow として購読する
 * アプリケーション操作を表す。
 *
 * ■ 提供する責務
 *   操作ログ一覧購読処理の入口
 *   取得件数の補正
 *   Repository への直近ログ購読委譲
 *   購読結果の Flow<AppResult<List<OperationLog>>> 返却
 *
 * ■ 設計上の意図
 *   操作ログ一覧画面など、DB更新に追従して表示を更新したい画面では、
 *   一度きりの取得ではなく Flow による購読が必要になる。
 *
 *   取得件数の正規化は OperationLogQueryPolicy に委譲し、
 *   UseCase は補正済み条件で Repository の購読処理を呼び出す。
 *
 * ■ 注意
 *   本 UseCase は Flow を返すため、実際の購読開始タイミングは
 *   呼び出し側が collect した時点になる。
 *
 *   一度だけ取得したい場合は GetOperationLogsUseCase を使用する。
 *
 * @param repository 操作ログの永続化操作を行う Repository。
 * @param queryPolicy 操作ログ取得条件を補正する Policy。
 */
class ObserveOperationLogsUseCase @Inject constructor(
    private val repository: OperationLogRepository,
    private val queryPolicy: OperationLogQueryPolicy
) {

    /**
     * 直近の操作ログ一覧を購読する。
     *
     * @param limit 取得上限件数。未指定時は DefaultOperationLogLimit を使用する。
     * @return 操作ログ一覧の購読結果。
     */
    operator fun invoke(
        limit: Int = DefaultOperationLogLimit
    ): Flow<AppResult<List<OperationLog>>> {
        return repository.observeRecent(
            limit = queryPolicy.normalizeLimit(limit)
        )
    }
}