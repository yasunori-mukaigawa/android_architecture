package jp.co.nsco.basearchitecture.core.theme

import jp.co.nsco.basearchitecture.core.result.AppResult
import kotlinx.coroutines.flow.Flow

/**
 * アプリテーマ設定を取得・更新するための契約。
 *
 * 本インターフェースは、テーマモードやテーマ ID の保存方式を隠蔽し、
 * アプリ全体で利用するテーマ設定への入口を定義する。
 *
 * ■ 提供する責務
 *   テーマ設定の購読
 *   テーマモードの更新
 *   テーマ ID の更新
 *   保存方式の隠蔽
 *
 * ■ 設計上の意図
 *   AppRoot / Theme 適用処理 / 設定画面は、
 *   PreferenceDataStore などの保存方式を直接知らず、
 *   ThemeRepository の契約だけを利用する。
 *
 *   themeSettings は Flow として公開し、
 *   テーマ変更が UI に自然に反映されるようにする。
 */
interface ThemeRepository {

    /**
     * 現在のテーマ設定。
     *
     * テーマモードやテーマ ID が変更されるたびに新しい ThemeSettings を流す。
     */
    val themeSettings: Flow<ThemeSettings>

    /**
     * テーマモードを更新する。
     *
     * @param themeMode 保存するテーマモード。
     * @return 更新結果。成功時は Unit、失敗時は AppError を返す。
     */
    suspend fun updateThemeMode(themeMode: AppThemeMode): AppResult<Unit>

    /**
     * テーマ ID を更新する。
     *
     * @param themeId 保存するテーマ ID。
     * @return 更新結果。成功時は Unit、失敗時は AppError を返す。
     */
    suspend fun updateThemeId(themeId: AppThemeId): AppResult<Unit>
}