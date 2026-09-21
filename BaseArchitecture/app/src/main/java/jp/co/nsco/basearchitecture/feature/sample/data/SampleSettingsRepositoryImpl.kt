package jp.co.nsco.basearchitecture.feature.sample.data

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.datastore.PreferenceDataStore
import jp.co.nsco.basearchitecture.core.error.AppError
import jp.co.nsco.basearchitecture.core.logging.AppLogger
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.feature.sample.domain.SampleSettings
import jp.co.nsco.basearchitecture.feature.sample.domain.SampleSettingsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

private const val KeyNotificationEnabled = "sample_notification_enabled"
private const val KeyConfirmOnLoginEnabled = "sample_confirm_on_login_enabled"
private const val KeyKeepCacheEnabled = "sample_keep_cache_enabled"

private const val ErrorObserveSettingsFailed = "sample_settings_observe_failed"
private const val ErrorNotificationSaveFailed = "sample_notification_save_failed"
private const val ErrorLoginConfirmationSaveFailed = "sample_login_confirmation_save_failed"
private const val ErrorKeepCacheSaveFailed = "sample_keep_cache_save_failed"
private const val LogTag = "SampleSettingsRepository"

/**
 * Sample Feature の設定値 Repository 実装。
 *
 * SampleSettingsRepositoryImpl は、SampleSettingsRepository 契約に対して、
 * PreferenceDataStore を利用した永続化処理を提供する。
 *
 * ■ 提供する責務
 *   Sample設定値の購読
 *   通知設定の保存
 *   ログイン時確認設定の保存
 *   キャッシュ保持設定の保存
 *   DataStore例外のAppResult変換
 *
 * ■ 設計上の意図
 *   Application / Presentation 層が PreferenceDataStore のキー名や保存方式を
 *   直接知らないようにする。
 *
 *   Repository 契約の内側で DataStore の読み書きを行い、
 *   上位層には SampleSettings という Domain Model と AppResult のみを返す。
 *
 *   これにより、将来的に保存方式を DataStore 以外へ変更する場合でも、
 *   UseCase / ViewModel への影響を抑えられる。
 *
 * ■ 注意
 *   本クラスは Sample Feature 用の設定保存実装であり、
 *   アプリ全体共通の設定管理は担当しない。
 *
 * @param preferenceDataStore Key-Value形式の設定値を永続化する DataStore 抽象。
 * @param logger DataStore例外を技術ログへ記録する Logger。
 */
class SampleSettingsRepositoryImpl @Inject constructor(
    private val preferenceDataStore: PreferenceDataStore,
    private val logger: AppLogger
) : SampleSettingsRepository {

    /**
     * Sample Feature の設定値を購読する。
     *
     * 複数の Boolean 設定値を combine し、SampleSettings としてまとめて返す。
     * 読み取り中に例外が発生した場合は AppResult.Failure として通知する。
     *
     * @return Sample設定値の購読結果。
     */
    override fun observeSettings(): Flow<AppResult<SampleSettings>> {
        val initialSettings = SampleSettings.initial()

        return combine(
            preferenceDataStore.observeBoolean(
                key = KeyNotificationEnabled,
                defaultValue = initialSettings.notificationEnabled
            ),
            preferenceDataStore.observeBoolean(
                key = KeyConfirmOnLoginEnabled,
                defaultValue = initialSettings.confirmOnLoginEnabled
            ),
            preferenceDataStore.observeBoolean(
                key = KeyKeepCacheEnabled,
                defaultValue = initialSettings.keepCacheEnabled
            )
        ) { notificationEnabled, confirmOnLoginEnabled, keepCacheEnabled ->
            SampleSettings(
                notificationEnabled = notificationEnabled,
                confirmOnLoginEnabled = confirmOnLoginEnabled,
                keepCacheEnabled = keepCacheEnabled
            )
        }.map<SampleSettings, AppResult<SampleSettings>> { settings ->
            AppResult.Success(settings)
        }.catch { throwable ->
            if (throwable is CancellationException) {
                throw throwable
            }

            logger.error(
                message = "Failed to observe sample settings: code=$ErrorObserveSettingsFailed",
                throwable = throwable,
                tag = LogTag
            )
            emit(
                AppResult.Failure(
                    AppError.LocalStorage(
                        code = ErrorObserveSettingsFailed,
                        cause = throwable
                    )
                )
            )
        }
    }

    /**
     * 通知設定を保存する。
     *
     * @param enabled true の場合、通知設定を有効にする。
     * @return 保存結果。
     */
    override suspend fun setNotificationEnabled(enabled: Boolean): AppResult<Unit> {
        return putBoolean(
            key = KeyNotificationEnabled,
            value = enabled,
            errorCode = ErrorNotificationSaveFailed
        )
    }

    /**
     * ログイン時確認設定を保存する。
     *
     * @param enabled true の場合、ログイン時確認を有効にする。
     * @return 保存結果。
     */
    override suspend fun setConfirmOnLoginEnabled(enabled: Boolean): AppResult<Unit> {
        return putBoolean(
            key = KeyConfirmOnLoginEnabled,
            value = enabled,
            errorCode = ErrorLoginConfirmationSaveFailed
        )
    }

    /**
     * キャッシュ保持設定を保存する。
     *
     * @param enabled true の場合、キャッシュ保持を有効にする。
     * @return 保存結果。
     */
    override suspend fun setKeepCacheEnabled(enabled: Boolean): AppResult<Unit> {
        return putBoolean(
            key = KeyKeepCacheEnabled,
            value = enabled,
            errorCode = ErrorKeepCacheSaveFailed
        )
    }

    /**
     * Boolean設定値を保存する。
     *
     * PreferenceDataStore の例外を AppError.LocalStorage に変換し、
     * Repository 契約として AppResult を返す。
     *
     * @param key 保存先キー。
     * @param value 保存する値。
     * @param errorCode 保存失敗時のエラーコード。
     * @return 保存結果。
     */
    private suspend fun putBoolean(
        key: String,
        value: Boolean,
        errorCode: String
    ): AppResult<Unit> {
        return try {
            preferenceDataStore.putBoolean(key = key, value = value)
            AppResult.Success(Unit)
        } catch (throwable: Throwable) {
            if (throwable is CancellationException) {
                throw throwable
            }

            logger.error(
                message = "Failed to save sample setting: key=$key, code=$errorCode",
                throwable = throwable,
                tag = LogTag
            )
            AppResult.Failure(
                AppError.LocalStorage(
                    code = errorCode,
                    cause = throwable
                )
            )
        }
    }
}
