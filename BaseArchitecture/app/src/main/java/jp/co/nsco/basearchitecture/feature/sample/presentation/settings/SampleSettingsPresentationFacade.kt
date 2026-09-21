package jp.co.nsco.basearchitecture.feature.sample.presentation.settings

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.logging.AppLogger
import jp.co.nsco.basearchitecture.core.resource.StringProvider
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleOperationLogMessageFactory
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleUiErrorMapper

/**
 * Sample Settings 画面の Presentation 補助部品をまとめる Facade。
 *
 * SampleSettingsPresentationFacade は、Sample Settings の ViewModel が利用する
 * Presentation 層の補助クラスを束ねる。
 *
 * ■ 提供する責務
 *   文字列リソース取得Providerの集約
 *   エラー表示Mapperの集約
 *   操作ログ文言Factoryの集約
 *   テーマ選択肢Mapperの集約
 *   Sample Settings の技術ログ出力契約の集約
 *
 * ■ 設計上の意図
 *   Sample Settings 画面は、設定保存、テーマ変更、操作ログ保存、
 *   Snackbar / Dialog 表示など複数の表示補助処理を持つ。
 *
 *   ViewModel のコンストラクタ引数が増えすぎると依存関係の見通しが悪くなるため、
 *   Presentation 層の補助部品を Facade としてまとめる。
 *
 * ■ 注意
 *   本 Facade は Presentation 層の補助依存をまとめるだけであり、
 *   UseCase 呼び出しや State 更新処理は持たない。
 *
 * @property stringProvider 文字列リソース取得を行う Provider。
 * @property errorMapper AppError を Sample Feature 向け表示文言へ変換する Mapper。
 * @property operationLogMessageFactory 操作ログ保存用の表示文言を生成する Factory。
 * @property themeOptionMapper テーマ設定を画面表示用選択肢へ変換する Mapper。
 * @property logger Sample Settings の処理状況や失敗を記録する Logger。
 */
class SampleSettingsPresentationFacade @Inject constructor(
    val stringProvider: StringProvider,
    val errorMapper: SampleUiErrorMapper,
    val operationLogMessageFactory: SampleOperationLogMessageFactory,
    val themeOptionMapper: SampleThemeOptionMapper,
    val logger: AppLogger
)
