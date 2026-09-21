package jp.co.nsco.basearchitecture.feature.sample.presentation.settings

import javax.inject.Inject
import jp.co.nsco.basearchitecture.feature.operationlog.application.SaveOperationLogUseCase
import jp.co.nsco.basearchitecture.feature.sample.application.GetAvailableThemePresetsUseCase
import jp.co.nsco.basearchitecture.feature.sample.application.ObserveSampleSettingsUseCase
import jp.co.nsco.basearchitecture.feature.sample.application.ObserveThemeSettingsUseCase
import jp.co.nsco.basearchitecture.feature.sample.application.UpdateSampleConfirmOnLoginEnabledUseCase
import jp.co.nsco.basearchitecture.feature.sample.application.UpdateSampleKeepCacheEnabledUseCase
import jp.co.nsco.basearchitecture.feature.sample.application.UpdateSampleNotificationEnabledUseCase
import jp.co.nsco.basearchitecture.feature.sample.application.UpdateThemeIdUseCase
import jp.co.nsco.basearchitecture.feature.sample.application.UpdateThemeModeUseCase

/**
 * Sample Settings 画面で利用する UseCase をまとめる Facade。
 *
 * SampleSettingsUseCaseFacade は、Sample Settings の ViewModel が利用する
 * Application 層の UseCase を束ねる。
 *
 * ■ 提供する責務
 *   Sample設定購読UseCaseの集約
 *   テーマ設定購読UseCaseの集約
 *   利用可能テーマ取得UseCaseの集約
 *   テーマモード更新UseCaseの集約
 *   テーマID更新UseCaseの集約
 *   Sample設定更新UseCase群の集約
 *   操作ログ保存UseCaseの集約
 *
 * ■ 設計上の意図
 *   Sample Settings 画面は、Sample設定、テーマ設定、操作ログ保存という
 *   複数のアプリケーション操作を扱う。
 *
 *   ViewModel のコンストラクタ引数が増えすぎると依存関係の見通しが悪くなるため、
 *   画面で利用する UseCase を Facade としてまとめる。
 *
 * ■ 注意
 *   本 Facade は UseCase の集約のみを担当する。
 *   画面状態の更新、表示用変換、副作用発行は ViewModel 側の責務とする。
 */
class SampleSettingsUseCaseFacade @Inject constructor(
    val observeSampleSettings: ObserveSampleSettingsUseCase,
    val observeThemeSettings: ObserveThemeSettingsUseCase,
    val getAvailableThemePresets: GetAvailableThemePresetsUseCase,
    val updateThemeMode: UpdateThemeModeUseCase,
    val updateThemeId: UpdateThemeIdUseCase,
    val updateNotificationEnabled: UpdateSampleNotificationEnabledUseCase,
    val updateConfirmOnLoginEnabled: UpdateSampleConfirmOnLoginEnabledUseCase,
    val updateKeepCacheEnabled: UpdateSampleKeepCacheEnabledUseCase,
    val saveOperationLog: SaveOperationLogUseCase
)