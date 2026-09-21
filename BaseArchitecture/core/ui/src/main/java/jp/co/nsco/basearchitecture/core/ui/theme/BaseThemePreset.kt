package jp.co.nsco.basearchitecture.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import jp.co.nsco.basearchitecture.core.theme.AppThemeId

/**
 * アプリテーマを構成する定義一式。
 *
 * BaseThemePreset は、1つのテーマ ID に対して、
 * Light / Dark の ColorScheme、Typography、Shapes、Spacing をまとめて保持する。
 *
 * ■ 提供する責務
 *   テーマ ID の保持
 *   Light ColorScheme の保持
 *   Dark ColorScheme の保持
 *   Typography の保持
 *   Shapes の保持
 *   Spacing の保持
 *
 * ■ 設計上の意図
 *   色、文字、形状、余白を別々に解決すると、
 *   テーマ切り替え時の組み合わせ不整合が起きやすくなる。
 *
 *   BaseThemePreset としてテーマ定義をまとめることで、
 *   ThemeRegistry がテーマ単位で一貫した見た目を返せるようにする。
 *
 * @property id テーマを識別する ID。
 * @property lightColorScheme Light 表示時に使用する ColorScheme。
 * @property darkColorScheme Dark 表示時に使用する ColorScheme。
 * @property typography テーマで使用する Typography。
 * @property shapes テーマで使用する Shapes。
 * @property spacing テーマで使用する余白定義。
 */
data class BaseThemePreset(
    val id: AppThemeId,
    val lightColorScheme: ColorScheme,
    val darkColorScheme: ColorScheme,
    val typography: Typography,
    val shapes: Shapes,
    val spacing: BaseSpacing
)

/**
 * Default テーマの ThemePreset を生成する。
 *
 * @return Default テーマに対応する BaseThemePreset。
 */
@Composable
fun defaultThemePreset(): BaseThemePreset = BaseThemePreset(
    id = AppThemeId.Default,
    lightColorScheme = defaultLightColorScheme(),
    darkColorScheme = defaultDarkColorScheme(),
    typography = DefaultTypography,
    shapes = DefaultShapes,
    spacing = DefaultSpacing
)

/**
 * Dashboard テーマの ThemePreset を生成する。
 *
 * @return Dashboard テーマに対応する BaseThemePreset。
 */
@Composable
fun dashboardThemePreset(): BaseThemePreset = BaseThemePreset(
    id = AppThemeId.Dashboard,
    lightColorScheme = dashboardLightColorScheme(),
    darkColorScheme = dashboardDarkColorScheme(),
    typography = DefaultTypography,
    shapes = DashboardShapes,
    spacing = DashboardSpacing
)