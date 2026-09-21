package jp.co.nsco.basearchitecture.feature.sample.presentation.history

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.logging.AppLogger
import jp.co.nsco.basearchitecture.core.resource.StringProvider
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleUiErrorMapper

/**
 * Sample History 画面の Presentation 補助部品をまとめる Facade。
 *
 * SampleHistoryPresentationFacade は、Sample History の ViewModel が利用する
 * Presentation 層の補助クラスを束ねる。
 *
 * ■ 提供する責務
 *   文字列リソース取得Providerの集約
 *   エラー表示Mapperの集約
 *   履歴Item変換Mapperの集約
 *   Sample History の技術ログ出力契約の集約
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
 * @property stringProvider 文字列リソース取得を行う Provider。
 * @property errorMapper AppError を Sample Feature 向け表示文言へ変換する Mapper。
 * @property itemMapper OperationLog を履歴Item表示状態へ変換する Mapper。
 * @property logger Sample History の処理状況や失敗を記録する Logger。
 */
class SampleHistoryPresentationFacade @Inject constructor(
    val stringProvider: StringProvider,
    val errorMapper: SampleUiErrorMapper,
    val itemMapper: SampleHistoryItemMapper,
    val logger: AppLogger
)
