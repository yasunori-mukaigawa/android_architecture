package jp.co.nsco.basearchitecture.feature.sample.presentation.history

import javax.inject.Inject
import jp.co.nsco.basearchitecture.feature.operationlog.application.ObserveOperationLogsUseCase

/**
 * Sample History 画面で利用する UseCase をまとめる Facade。
 *
 * SampleHistoryUseCaseFacade は、Sample History の ViewModel が利用する
 * Application 層の UseCase を束ねる。
 *
 * ■ 提供する責務
 *   操作ログ購読 UseCase の集約
 *
 * ■ 設計上の意図
 *   ViewModel のコンストラクタ引数が増えすぎると、
 *   依存関係の見通しが悪くなる。
 *
 *   画面で使用する UseCase を Facade としてまとめることで、
 *   ViewModel は必要なアプリケーション操作をまとまった単位で受け取れる。
 *
 * ■ 注意
 *   本 Facade は UseCase の集約のみを担当する。
 *   画面状態の更新、表示用変換、副作用発行は ViewModel 側の責務とする。
 *
 * @property observeOperationLogs 操作ログ一覧を購読する UseCase。
 */
class SampleHistoryUseCaseFacade @Inject constructor(
    val observeOperationLogs: ObserveOperationLogsUseCase
)