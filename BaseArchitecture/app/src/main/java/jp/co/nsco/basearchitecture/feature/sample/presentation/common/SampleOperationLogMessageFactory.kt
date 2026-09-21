package jp.co.nsco.basearchitecture.feature.sample.presentation.common

import javax.inject.Inject
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.resource.StringProvider

/**
 * Sample Feature の操作ログ文言を生成する Factory。
 *
 * SampleOperationLogMessageFactory は、Sample Feature で操作ログを保存する際に使用する
 * タイトルや概要文言を生成する。
 *
 * ■ 提供する責務
 *   Sample Home 表示ログ文言の生成
 *   Sample Settings 変更ログ文言の生成
 *   設定値に応じたログ概要文言の生成
 *   エラーログ文言の生成
 *
 * ■ 設計上の意図
 *   ViewModel や UseCase が string resource ID や Context を直接扱わないようにする。
 *
 *   操作ログに保存する文言は Presentation 上の表示文言に近いため、
 *   Presentation common の Factory に集約する。
 *
 *   これにより、文言変更や多言語対応の影響を本クラスに閉じ込められる。
 *
 * @param stringProvider 文字列リソース取得を行う Provider。
 */
class SampleOperationLogMessageFactory @Inject constructor(
    private val stringProvider: StringProvider
) {

    /**
     * Sample Home 表示ログのタイトルを取得する。
     *
     * @return 操作ログタイトル。
     */
    fun homeDisplayedTitle(): String {
        return stringProvider.getString(R.string.sample_log_home_displayed_title)
    }

    /**
     * Sample Home 表示ログの概要を取得する。
     *
     * @return 操作ログ概要。
     */
    fun homeDisplayedSummary(): String {
        return stringProvider.getString(R.string.sample_log_home_displayed_summary)
    }

    /**
     * 設定変更ログのタイトルを取得する。
     *
     * @return 操作ログタイトル。
     */
    fun settingChangedTitle(): String {
        return stringProvider.getString(R.string.sample_log_setting_changed_title)
    }

    /**
     * 通知設定変更ログの概要を取得する。
     *
     * @param enabled true の場合は有効化、false の場合は無効化の文言を返す。
     * @return 操作ログ概要。
     */
    fun notificationSummary(enabled: Boolean): String {
        return stringProvider.getString(
            if (enabled) {
                R.string.sample_log_notification_enabled
            } else {
                R.string.sample_log_notification_disabled
            }
        )
    }

    /**
     * ログイン時確認設定変更ログの概要を取得する。
     *
     * @param enabled true の場合は有効化、false の場合は無効化の文言を返す。
     * @return 操作ログ概要。
     */
    fun confirmOnLoginSummary(enabled: Boolean): String {
        return stringProvider.getString(
            if (enabled) {
                R.string.sample_log_confirm_login_enabled
            } else {
                R.string.sample_log_confirm_login_disabled
            }
        )
    }

    /**
     * キャッシュ保持設定変更ログの概要を取得する。
     *
     * @param enabled true の場合は有効化、false の場合は無効化の文言を返す。
     * @return 操作ログ概要。
     */
    fun keepCacheSummary(enabled: Boolean): String {
        return stringProvider.getString(
            if (enabled) {
                R.string.sample_log_keep_cache_enabled
            } else {
                R.string.sample_log_keep_cache_disabled
            }
        )
    }

    /**
     * エラーログのタイトルを取得する。
     *
     * @return 操作ログタイトル。
     */
    fun errorTitle(): String {
        return stringProvider.getString(R.string.sample_log_error_title)
    }

    /**
     * 保存失敗時のエラーログ概要を取得する。
     *
     * @return 操作ログ概要。
     */
    fun saveErrorSummary(): String {
        return stringProvider.getString(R.string.sample_log_save_error_summary)
    }

    /**
     * 設定取得失敗時のエラーログ概要を取得する。
     *
     * @return 操作ログ概要。
     */
    fun settingsErrorSummary(): String {
        return stringProvider.getString(R.string.sample_log_settings_error_summary)
    }
}