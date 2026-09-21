package jp.co.nsco.basearchitecture.core.ui.theme

import androidx.compose.runtime.Composable
import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.theme.AppThemeId

/**
 * テーマIDからThemePresetを解決するための契約。
 *
 * 案件固有のテーマ追加や差し替えを、BaseAppThemeから分離する。
 */
interface BaseThemeRegistry {

    /** アプリで利用可能なテーマID一覧を取得する。 */
    fun availableThemeIds(): List<AppThemeId>

    /** 指定されたテーマIDに対応するThemePresetを取得する。 */
    @Composable
    fun resolveThemePreset(themeId: AppThemeId): BaseThemePreset
}

/**
 * Template標準のBaseThemeRegistry実装。
 *
 * TemplateではDefaultテーマのみを提供し、未知のIDはDefaultへフォールバックする。
 */
class DefaultBaseThemeRegistry @Inject constructor() : BaseThemeRegistry {

    /** Defaultテーマのみを返す。 */
    override fun availableThemeIds(): List<AppThemeId> {
        return listOf(AppThemeId.Default)
    }

    /** 未知のテーマIDを含め、Defaultテーマへ解決する。 */
    @Composable
    override fun resolveThemePreset(themeId: AppThemeId): BaseThemePreset {
        return defaultThemePreset()
    }
}
