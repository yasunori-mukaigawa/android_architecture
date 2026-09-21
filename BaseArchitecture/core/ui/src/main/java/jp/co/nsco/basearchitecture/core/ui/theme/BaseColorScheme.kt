package jp.co.nsco.basearchitecture.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import jp.co.nsco.basearchitecture.core.ui.R

/**
 * Default テーマの Light 用 ColorScheme を生成する。
 *
 * 色定義は colors.xml の resource を参照し、
 * Compose の Material3 ColorScheme へ変換する。
 *
 * @return Default テーマの Light ColorScheme。
 */
@Composable
fun defaultLightColorScheme(): ColorScheme = lightColorScheme(
    primary = colorResource(R.color.base_default_light_primary),
    onPrimary = colorResource(R.color.base_default_light_on_primary),
    secondary = colorResource(R.color.base_default_light_secondary),
    onSecondary = colorResource(R.color.base_default_light_on_secondary),
    background = colorResource(R.color.base_default_light_background),
    onBackground = colorResource(R.color.base_default_light_on_background),
    surface = colorResource(R.color.base_default_light_surface),
    onSurface = colorResource(R.color.base_default_light_on_surface),
    surfaceVariant = colorResource(R.color.base_default_light_surface_variant),
    onSurfaceVariant = colorResource(R.color.base_default_light_on_surface_variant),
    error = colorResource(R.color.base_default_light_error),
    onError = colorResource(R.color.base_default_light_on_error),
    outline = colorResource(R.color.base_default_light_outline)
)

/**
 * Default テーマの Dark 用 ColorScheme を生成する。
 *
 * @return Default テーマの Dark ColorScheme。
 */
@Composable
fun defaultDarkColorScheme(): ColorScheme = darkColorScheme(
    primary = colorResource(R.color.base_default_dark_primary),
    onPrimary = colorResource(R.color.base_default_dark_on_primary),
    secondary = colorResource(R.color.base_default_dark_secondary),
    onSecondary = colorResource(R.color.base_default_dark_on_secondary),
    background = colorResource(R.color.base_default_dark_background),
    onBackground = colorResource(R.color.base_default_dark_on_background),
    surface = colorResource(R.color.base_default_dark_surface),
    onSurface = colorResource(R.color.base_default_dark_on_surface),
    surfaceVariant = colorResource(R.color.base_default_dark_surface_variant),
    onSurfaceVariant = colorResource(R.color.base_default_dark_on_surface_variant),
    error = colorResource(R.color.base_default_dark_error),
    onError = colorResource(R.color.base_default_dark_on_error),
    outline = colorResource(R.color.base_default_dark_outline)
)

/**
 * Dashboard テーマの Light 用 ColorScheme を生成する。
 *
 * Default テーマとは異なる画面用途や印象を表現したい場合に使用する。
 *
 * @return Dashboard テーマの Light ColorScheme。
 */
@Composable
fun dashboardLightColorScheme(): ColorScheme = lightColorScheme(
    primary = colorResource(R.color.base_dashboard_light_primary),
    onPrimary = colorResource(R.color.base_dashboard_light_on_primary),
    secondary = colorResource(R.color.base_dashboard_light_secondary),
    onSecondary = colorResource(R.color.base_dashboard_light_on_secondary),
    background = colorResource(R.color.base_dashboard_light_background),
    onBackground = colorResource(R.color.base_dashboard_light_on_background),
    surface = colorResource(R.color.base_dashboard_light_surface),
    onSurface = colorResource(R.color.base_dashboard_light_on_surface),
    surfaceVariant = colorResource(R.color.base_dashboard_light_surface_variant),
    onSurfaceVariant = colorResource(R.color.base_dashboard_light_on_surface_variant),
    error = colorResource(R.color.base_dashboard_light_error),
    onError = colorResource(R.color.base_dashboard_light_on_error),
    outline = colorResource(R.color.base_dashboard_light_outline)
)

/**
 * Dashboard テーマの Dark 用 ColorScheme を生成する。
 *
 * @return Dashboard テーマの Dark ColorScheme。
 */
@Composable
fun dashboardDarkColorScheme(): ColorScheme = darkColorScheme(
    primary = colorResource(R.color.base_dashboard_dark_primary),
    onPrimary = colorResource(R.color.base_dashboard_dark_on_primary),
    secondary = colorResource(R.color.base_dashboard_dark_secondary),
    onSecondary = colorResource(R.color.base_dashboard_dark_on_secondary),
    background = colorResource(R.color.base_dashboard_dark_background),
    onBackground = colorResource(R.color.base_dashboard_dark_on_background),
    surface = colorResource(R.color.base_dashboard_dark_surface),
    onSurface = colorResource(R.color.base_dashboard_dark_on_surface),
    surfaceVariant = colorResource(R.color.base_dashboard_dark_surface_variant),
    onSurfaceVariant = colorResource(R.color.base_dashboard_dark_on_surface_variant),
    error = colorResource(R.color.base_dashboard_dark_error),
    onError = colorResource(R.color.base_dashboard_dark_on_error),
    outline = colorResource(R.color.base_dashboard_dark_outline)
)
