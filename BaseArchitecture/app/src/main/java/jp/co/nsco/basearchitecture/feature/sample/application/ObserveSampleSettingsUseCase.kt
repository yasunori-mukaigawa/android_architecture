package jp.co.nsco.basearchitecture.feature.sample.application

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.feature.sample.domain.SampleSettings
import jp.co.nsco.basearchitecture.feature.sample.domain.SampleSettingsRepository
import kotlinx.coroutines.flow.Flow

/**
 * Sample Feature の設定値を購読する UseCase。
 *
 * 本 UseCase は、Sample 画面で利用する設定値を Flow として購読する
 * アプリケーション操作を表す。
 *
 * ■ 提供する責務
 *   Sample設定購読処理の入口
 *   Repository への設定購読委譲
 *   購読結果の Flow<AppResult<SampleSettings>> 返却
 *
 * ■ 設計上の意図
 *   ViewModel が設定値の保存先や購読方法を直接知らないようにする。
 *
 *   SampleSettingsRepository 契約に委譲することで、
 *   DataStore などの具体的な保存方式を Application / Presentation 層から隠蔽する。
 *
 * ■ 注意
 *   本 UseCase は Flow を返すため、実際の購読開始タイミングは
 *   呼び出し側が collect した時点になる。
 *
 * @param repository Sample設定値の永続化操作を行う Repository。
 */
class ObserveSampleSettingsUseCase @Inject constructor(
    private val repository: SampleSettingsRepository
) {

    /**
     * Sample Feature の設定値を購読する。
     *
     * @return Sample設定値の購読結果。
     */
    operator fun invoke(): Flow<AppResult<SampleSettings>> {
        return repository.observeSettings()
    }
}