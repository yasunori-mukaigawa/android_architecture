package jp.co.nsco.basearchitecture.feature.sample.presentation.settings

import jp.co.nsco.basearchitecture.core.theme.AppThemeId
import jp.co.nsco.basearchitecture.core.theme.AppThemeMode
import jp.co.nsco.basearchitecture.core.theme.ThemeSettings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SampleSettingsReducerThemeTest {
    private val reducer = SampleSettingsReducer()

    @Test
    fun reduce_updatesThemeSettingsAndOptionsWhenThemeSettingsLoaded() {
        val modeOptions = listOf(
            ThemeModeOptionUiState(AppThemeMode.Light, "Light", selected = true)
        )
        val presetOptions = listOf(
            ThemePresetOptionUiState(AppThemeId.Dashboard, "Dashboard", "Dashboard theme", selected = true)
        )

        val state = reducer.reduce(
            currentState = SampleSettingsUiState(screenErrorMessage = "error"),
            message = SampleSettingsUiMessage.ThemeSettingsLoaded(
                settings = ThemeSettings(
                    themeMode = AppThemeMode.Light,
                    themeId = AppThemeId.Dashboard
                ),
                modeOptions = modeOptions,
                presetOptions = presetOptions
            )
        )

        assertEquals(AppThemeMode.Light, state.themeMode)
        assertEquals(AppThemeId.Dashboard, state.themeId)
        assertEquals(modeOptions, state.themeModeOptions)
        assertEquals(presetOptions, state.themePresetOptions)
        assertNull(state.screenErrorMessage)
    }

    @Test
    fun reduce_updatesThemeModeOptimistically() {
        val modeOptions = AppThemeMode.entries.map { mode ->
            ThemeModeOptionUiState(mode, mode.name, selected = mode == AppThemeMode.Dark)
        }

        val state = reducer.reduce(
            currentState = SampleSettingsUiState(screenErrorMessage = "error"),
            message = SampleSettingsUiMessage.ThemeModeUpdated(
                themeMode = AppThemeMode.Dark,
                modeOptions = modeOptions
            )
        )

        assertEquals(AppThemeMode.Dark, state.themeMode)
        assertEquals(modeOptions, state.themeModeOptions)
        assertNull(state.screenErrorMessage)
    }

    @Test
    fun reduce_updatesThemePresetOptimistically() {
        val presetOptions = listOf(
            ThemePresetOptionUiState(AppThemeId.Default, "Default", "Default theme", selected = false),
            ThemePresetOptionUiState(AppThemeId.Dashboard, "Dashboard", "Dashboard theme", selected = true)
        )

        val state = reducer.reduce(
            currentState = SampleSettingsUiState(screenErrorMessage = "error"),
            message = SampleSettingsUiMessage.ThemePresetUpdated(
                themeId = AppThemeId.Dashboard,
                presetOptions = presetOptions
            )
        )

        assertEquals(AppThemeId.Dashboard, state.themeId)
        assertEquals(presetOptions, state.themePresetOptions)
        assertNull(state.screenErrorMessage)
    }

    @Test
    fun reduce_setsErrorWhenThemeUpdateFailed() {
        val state = reducer.reduce(
            currentState = SampleSettingsUiState(screenErrorMessage = null),
            message = SampleSettingsUiMessage.ThemeUpdateFailed("テーマ設定の保存に失敗しました。")
        )

        assertEquals("テーマ設定の保存に失敗しました。", state.screenErrorMessage)
    }
}





