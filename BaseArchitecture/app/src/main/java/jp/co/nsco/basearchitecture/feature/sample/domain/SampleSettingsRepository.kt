package jp.co.nsco.basearchitecture.feature.sample.domain

import jp.co.nsco.basearchitecture.core.result.AppResult
import kotlinx.coroutines.flow.Flow

/**
 * Sample Feature の設定値 Repository 契約。
 *
 * SampleSettingsRepository は、Sample 画面で扱う設定値の
 * 取得・購読・更新を行うための Domain 層の契約を表す。
 *
 * ■ 提供する責務
 *   Sample設定値の購読契約
 *   通知設定の更新契約
 *   ログイン時確認設定の更新契約
 *   キャッシュ保持設定の更新契約
 *
 * ■ 設計上の意図
 *   Application 層は、設定値が DataStore、DB、ファイルなど
 *   どの保存方式で管理されているかを知らない。
 *
 *   Repository 契約を挟むことで、保存方式の変更を
 *   UseCase や Presentation 層へ波及させない。
 *
 *   失敗は例外を直接投げず AppResult.Failure として返し、
 *   呼び出し側が成功・失敗を一貫して扱えるようにする。
 */
interface SampleSettingsRepository {

    /**
     * Sample Feature の設定値を購読する。
     *
     * 保存内容の変更に追従して画面更新したい場合に利用する。
     *
     * @return Sample設定値の購読結果。
     */
    fun observeSettings(): Flow<AppResult<SampleSettings>>

    /**
     * 通知設定を更新する。
     *
     * @param enabled true の場合、通知を有効にする。
     * @return 更新結果。
     */
    suspend fun setNotificationEnabled(enabled: Boolean): AppResult<Unit>

    /**
     * ログイン時確認設定を更新する。
     *
     * @param enabled true の場合、ログイン時確認を有効にする。
     * @return 更新結果。
     */
    suspend fun setConfirmOnLoginEnabled(enabled: Boolean): AppResult<Unit>

    /**
     * キャッシュ保持設定を更新する。
     *
     * @param enabled true の場合、キャッシュ保持を有効にする。
     * @return 更新結果。
     */
    suspend fun setKeepCacheEnabled(enabled: Boolean): AppResult<Unit>
}