package jp.co.nsco.basearchitecture.feature.sample.application

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.feature.sample.domain.SampleSettingsRepository

/**
 * 通知設定を更新する UseCase。
 *
 * 本 UseCase は、Sample Feature の「通知を有効にする」設定を更新する
 * アプリケーション操作を表す。
 *
 * ■ 提供する責務
 *   通知設定更新の入口
 *   Repository への設定保存委譲
 *   更新結果の AppResult 返却
 *
 * ■ 設計上の意図
 *   ViewModel が設定値の保存方式を直接知らないようにする。
 *
 *   設定更新は SampleSettingsRepository 契約に委譲し、
 *   DataStore などの具体的な保存方式を Presentation 層から隠蔽する。
 *
 * @param repository Sample設定値の永続化操作を行う Repository。
 */
class UpdateSampleNotificationEnabledUseCase @Inject constructor(
    private val repository: SampleSettingsRepository
) {

    /**
     * 通知設定を更新する。
     *
     * @param enabled true の場合、通知を有効にする。
     * @return 設定更新結果。
     */
    suspend operator fun invoke(enabled: Boolean): AppResult<Unit> {
        return repository.setNotificationEnabled(enabled)
    }
}