package jp.co.nsco.basearchitecture.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import jp.co.nsco.basearchitecture.core.ui.R

/** DefaultテーマのLight用ColorSchemeを生成する。 */
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

/** DefaultテーマのDark用ColorSchemeを生成する。 */
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
