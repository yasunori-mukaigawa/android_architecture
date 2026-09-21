package jp.co.nsco.basearchitecture.feature.sample.presentation.home

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.logging.AppLogger
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleOperationLogMessageFactory
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleUiErrorMapper

/**
 * Sample Home 画面の Presentation 補助部品をまとめる Facade。
 *
 * SampleHomePresentationFacade は、Sample Home の ViewModel が利用する
 * Presentation 層の補助クラスを束ねる。
 *
 * ■ 提供する責務
 *   エラー表示Mapperの集約
 *   Homeヘッダー表示Formatterの集約
 *   操作ログ文言Factoryの集約
 *   Sample Home の技術ログ出力契約の集約
 *
 * ■ 設計上の意図
 *   ViewModel のコンストラクタ引数が増えすぎると、
 *   依存関係の見通しが悪くなる。
 *
 *   Presentation 層の補助部品を Facade として束ねることで、
 *   ViewModel は画面制御に必要な依存をまとまった単位で受け取れる。
 *
 * ■ 注意
 *   本 Facade は Presentation 層の補助依存をまとめるだけであり、
 *   UseCase 呼び出しや State 更新処理は持たない。
 *
 * @property errorMapper AppError を Sample Feature 向け表示文言へ変換する Mapper。
 * @property homeHeaderFormatter Homeヘッダー表示値を整形する Formatter。
 * @property operationLogMessageFactory 操作ログ保存用の表示文言を生成する Factory。
 * @property logger Sample Home の処理状況や失敗を記録する Logger。
 */
class SampleHomePresentationFacade @Inject constructor(
    val errorMapper: SampleUiErrorMapper,
    val homeHeaderFormatter: SampleHomeHeaderFormatter,
    val operationLogMessageFactory: SampleOperationLogMessageFactory,
    val logger: AppLogger
)
