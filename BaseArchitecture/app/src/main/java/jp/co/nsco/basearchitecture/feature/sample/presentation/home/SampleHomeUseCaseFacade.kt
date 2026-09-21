package jp.co.nsco.basearchitecture.feature.sample.presentation.home

import javax.inject.Inject
import jp.co.nsco.basearchitecture.feature.operationlog.application.SaveOperationLogUseCase
import jp.co.nsco.basearchitecture.feature.sample.application.GetSampleHomeHeaderUseCase

/**
 * Sample Home 画面で利用する UseCase をまとめる Facade。
 *
 * SampleHomeUseCaseFacade は、Sample Home の ViewModel が利用する
 * Application 層の UseCase を束ねる。
 *
 * ■ 提供する責務
 *   Homeヘッダー取得UseCaseの集約
 *   操作ログ保存UseCaseの集約
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
 * @property getSampleHomeHeader Sample Home ヘッダー情報を取得する UseCase。
 * @property saveOperationLog 操作ログを保存する UseCase。
 */
class SampleHomeUseCaseFacade @Inject constructor(
    val getSampleHomeHeader: GetSampleHomeHeaderUseCase,
    val saveOperationLog: SaveOperationLogUseCase
)