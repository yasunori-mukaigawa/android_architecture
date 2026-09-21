package jp.co.nsco.basearchitecture.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import jp.co.nsco.basearchitecture.core.theme.AppThemeId

/**
 * アプリテーマを構成する定義一式。
 *
 * BaseThemePresetは、1つのテーマIDに対して、
 * Light / DarkのColorScheme、Typography、Shapes、Spacingをまとめて保持する。
 *
 * @property id テーマを識別するID。
 * @property lightColorScheme Light表示時に使用するColorScheme。
 * @property darkColorScheme Dark表示時に使用するColorScheme。
 * @property typography テーマで使用するTypography。
 * @property shapes テーマで使用するShapes。
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

/** DefaultテーマのThemePresetを生成する。 */
@Composable
fun defaultThemePreset(): BaseThemePreset = BaseThemePreset(
    id = AppThemeId.Default,
    lightColorScheme = defaultLightColorScheme(),
    darkColorScheme = defaultDarkColorScheme(),
    typography = DefaultTypography,
    shapes = DefaultShapes,
    spacing = DefaultSpacing
)
