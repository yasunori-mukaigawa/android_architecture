package jp.co.nsco.basearchitecture.feature.operationlog.application

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.error.AppError
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.core.time.AppClock
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLog
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogId
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogRepository
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogResult
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogType

private const val OperationLogTitleBlank = "operation_log_title_blank"
private const val OperationLogTitleField = "title"
private const val UnsavedOperationLogId = 0L

/**
 * 操作ログを保存する UseCase。
 *
 * 本 UseCase は、操作種別・結果・タイトルなどの入力値から
 * OperationLog を生成し、Repository へ保存するアプリケーション操作を表す。
 *
 * ■ 提供する責務
 *   操作ログ保存処理の入口
 *   必須項目の検証
 *   発生時刻の付与
 *   OperationLog Domain Model の生成
 *   Repository への保存委譲
 *   保存結果の AppResult 返却
 *
 * ■ 設計上の意図
 *   操作ログ保存時に必要となる共通処理を UseCase に集約する。
 *
 *   呼び出し側は、操作種別・結果・タイトルなどの意味のある情報だけを渡し、
 *   発生時刻の取得や OperationLog の組み立ては本 UseCase に任せる。
 *
 *   現在時刻は AppClock から取得し、
 *   System.currentTimeMillis() を直接参照しない。
 *
 * ■ 注意
 *   title は操作ログの識別・表示に使うため必須とする。
 *   空白のみの場合は保存せず、AppError.Validation を返す。
 *
 *   id は保存前の仮IDとして 0L を設定する。
 *   DB採番されたIDを最終的な OperationLogId として扱う場合、
 *   Repository / LocalDataSource / Dao 側で insert 結果からIDを返す。
 *
 * @param repository 操作ログの永続化操作を行う Repository。
 * @param appClock 現在時刻を取得する Clock。
 */
class SaveOperationLogUseCase @Inject constructor(
    private val repository: OperationLogRepository,
    private val appClock: AppClock
) {

    /**
     * 操作ログを保存する。
     *
     * @param type 操作ログ種別。
     * @param result 操作結果。
     * @param title 操作ログタイトル。
     * @param summary 操作ログ概要。
     * @param detail 操作ログ詳細。
     * @param correlationId 関連処理を追跡するためのID。
     * @return 保存された操作ログID。
     */
    suspend operator fun invoke(
        type: OperationLogType,
        result: OperationLogResult,
        title: String,
        summary: String? = null,
        detail: String? = null,
        correlationId: String? = null
    ): AppResult<OperationLogId> {
        if (title.isBlank()) {
            return AppResult.Failure(
                AppError.Validation(
                    code = OperationLogTitleBlank,
                    field = OperationLogTitleField,
                    reason = "Operation log title must not be blank."
                )
            )
        }

        val log = OperationLog(
            id = OperationLogId(UnsavedOperationLogId),
            type = type,
            result = result,
            title = title,
            summary = summary,
            occurredAtMillis = appClock.nowMillis(),
            detail = detail,
            correlationId = correlationId
        )

        return repository.save(log)
    }
}