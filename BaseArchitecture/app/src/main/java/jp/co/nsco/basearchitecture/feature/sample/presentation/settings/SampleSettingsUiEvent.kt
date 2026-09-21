package jp.co.nsco.basearchitecture.feature.sample.presentation.settings

import jp.co.nsco.basearchitecture.core.architecture.UiEvent
import jp.co.nsco.basearchitecture.core.theme.AppThemeId
import jp.co.nsco.basearchitecture.core.theme.AppThemeMode

/**
 * Sample Settings 画面で発生するユーザー操作・画面イベント。
 *
 * SampleSettingsUiEvent は、View から ViewModel へ通知する
 * 「画面で何が起きたか」を表す。
 *
 * ■ 提供する責務
 *   初期表示イベントの表現
 *   通知設定変更イベントの表現
 *   ログイン時確認設定変更イベントの表現
 *   キャッシュ保持設定変更イベントの表現
 *   テーマモード変更イベントの表現
 *   テーマプリセット変更イベントの表現
 *   Home 画面遷移操作の表現
 *
 * ■ 設計上の意図
 *   View は UseCase や State 更新処理を直接呼び出さず、
 *   Event として ViewModel へ通知する。
 *
 *   ViewModel は受け取った Event を解釈し、
 *   UseCase 呼び出し、Message dispatch、Effect 発行を行う。
 */
sealed interface SampleSettingsUiEvent : UiEvent {

    /**
     * 画面初期化イベント。
     *
     * Sample 設定値とテーマ設定の購読開始を ViewModel へ依頼する。
     */
    data object Initialize : SampleSettingsUiEvent

    /**
     * 通知設定変更イベント。
     *
     * @property enabled 変更後の通知設定。
     */
    data class NotificationChanged(
        val enabled: Boolean
    ) : SampleSettingsUiEvent

    /**
     * ログイン時確認設定変更イベント。
     *
     * @property enabled 変更後のログイン時確認設定。
     */
    data class ConfirmOnLoginChanged(
        val enabled: Boolean
    ) : SampleSettingsUiEvent

    /**
     * キャッシュ保持設定変更イベント。
     *
     * @property enabled 変更後のキャッシュ保持設定。
     */
    data class KeepCacheChanged(
        val enabled: Boolean
    ) : SampleSettingsUiEvent

    /**
     * テーマモード変更イベント。
     *
     * @property themeMode 変更後のテーマモード。
     */
    data class ThemeModeChanged(
        val themeMode: AppThemeMode
    ) : SampleSettingsUiEvent

    /**
     * テーマプリセット変更イベント。
     *
     * @property themeId 変更後のテーマID。
     */
    data class ThemePresetChanged(
        val themeId: AppThemeId
    ) : SampleSettingsUiEvent

    /**
     * Home 画面への遷移操作イベント。
     */
    data object HomeClicked : SampleSettingsUiEvent
}