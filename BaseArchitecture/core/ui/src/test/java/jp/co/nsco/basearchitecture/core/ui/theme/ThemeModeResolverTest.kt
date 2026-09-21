package jp.co.nsco.basearchitecture.core.ui.theme

import jp.co.nsco.basearchitecture.core.theme.AppThemeMode
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ThemeModeResolverTest {

    @Test
    fun useDarkThemeForMode_returnsSystemValueWhenModeIsSystem() {
        assertTrue(useDarkThemeForMode(AppThemeMode.System, systemInDarkTheme = true))
        assertFalse(useDarkThemeForMode(AppThemeMode.System, systemInDarkTheme = false))
    }

    @Test
    fun useDarkThemeForMode_returnsFalseWhenModeIsLight() {
        assertFalse(useDarkThemeForMode(AppThemeMode.Light, systemInDarkTheme = true))
    }

    @Test
    fun useDarkThemeForMode_returnsTrueWhenModeIsDark() {
        assertTrue(useDarkThemeForMode(AppThemeMode.Dark, systemInDarkTheme = false))
    }
}






