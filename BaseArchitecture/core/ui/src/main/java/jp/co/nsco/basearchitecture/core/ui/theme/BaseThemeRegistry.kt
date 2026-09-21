package jp.co.nsco.basearchitecture.core.ui.theme

import androidx.compose.runtime.Composable
import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.theme.AppThemeId

/**
 * テーマ ID から ThemePreset を解決するための契約。
 *
 * BaseThemeRegistry は、アプリで利用可能なテーマ一覧と、
 * 指定されたテーマ ID に対応する ThemePreset を提供する。
 *
 * ■ 提供する責務
 *   利用可能テーマ ID の提供
 *   テーマ ID から ThemePreset への解決
 *
 * ■ 設計上の意図
 *   BaseAppTheme が個別のテーマ定義を直接知らないようにする。
 *
 *   テーマ追加やテーマ差し替えが発生しても、
 *   BaseAppTheme 側ではなく Registry 実装側の変更に閉じ込める。
 */
interface BaseThemeRegistry {

    /**
     * アプリで利用可能なテーマ ID 一覧を取得する。
     *
     * 設定画面でのテーマ選択肢表示などに使用する。
     *
     * @return 利用可能なテーマ ID 一覧。
     */
    fun availableThemeIds(): List<AppThemeId>

    /**
     * 指定されたテーマ ID に対応する ThemePreset を取得する。
     *
     * 未知のテーマ ID が指定された場合の扱いは実装側で決定する。
     *
     * @param themeId 解決対象のテーマ ID。
     * @return テーマ ID に対応する BaseThemePreset。
     */
    @Composable
    fun resolveThemePreset(themeId: AppThemeId): BaseThemePreset
}

/**
 * アプリ標準の BaseThemeRegistry 実装。
 *
 * 本実装は、Default テーマと Dashboard テーマを提供する。
 *
 * ■ 設計上の意図
 *   ベースアーキテクチャとして利用可能な標準テーマを定義する。
 *   未知の ThemeId が指定された場合は Default テーマへフォールバックする。
 */
class DefaultBaseThemeRegistry @Inject constructor() : BaseThemeRegistry {

    /**
     * アプリで利用可能なテーマ ID 一覧を取得する。
     *
     * @return Default / Dashboard のテーマ ID 一覧。
     */
    override fun availableThemeIds(): List<AppThemeId> {
        return listOf(AppThemeId.Default, AppThemeId.Dashboard)
    }

    /**
     * 指定されたテーマ ID に対応する ThemePreset を取得する。
     *
     * Dashboard が指定された場合は Dashboard テーマを返し、
     * それ以外は Default テーマへフォールバックする。
     *
     * @param themeId 解決対象のテーマ ID。
     * @return 解決された BaseThemePreset。
     */
    @Composable
    override fun resolveThemePreset(themeId: AppThemeId): BaseThemePreset {
        return when (themeId) {
            AppThemeId.Dashboard -> dashboardThemePreset()
            else -> defaultThemePreset()
        }
    }
}