package jp.co.nsco.basearchitecture.core.ui

import androidx.compose.ui.unit.dp
import jp.co.nsco.basearchitecture.core.theme.AppThemeId
import jp.co.nsco.basearchitecture.core.theme.AppThemeMode
import jp.co.nsco.basearchitecture.core.theme.ThemeSettings
import jp.co.nsco.basearchitecture.core.ui.dialog.DialogUiState
import jp.co.nsco.basearchitecture.core.ui.markdown.MarkdownRenderOptions
import jp.co.nsco.basearchitecture.core.ui.theme.BaseSpacing
import jp.co.nsco.basearchitecture.core.ui.theme.DashboardSpacing
import jp.co.nsco.basearchitecture.core.ui.theme.DefaultSpacing
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Core UI の値モデルと既定値を確認する。
 */
class UiModelTest {

    @Test
    fun themeModels_exposeStableDefaultsAndKeys() {
        assertEquals("default", AppThemeId.Default.value)
        assertEquals("dashboard", AppThemeId.Dashboard.value)
        assertEquals(AppThemeMode.System, ThemeSettings().themeMode)
        assertEquals(AppThemeId.Default, ThemeSettings().themeId)
    }

    @Test
    fun spacingAndMarkdownOptions_exposeExpectedDefaults() {
        assertEquals(BaseSpacing(), DefaultSpacing)
        assertEquals(4.dp, DashboardSpacing.xs)
        assertEquals(16.dp, DefaultSpacing.md)
        assertEquals(MarkdownRenderOptions(), MarkdownRenderOptions.Default)
        assertTrue(MarkdownRenderOptions.Default.enableLinks)
        assertTrue(MarkdownRenderOptions.Default.selectable)
    }

    @Test
    fun dialogUiState_keepsOptionalNegativeButton() {
        val state = DialogUiState(
            title = "Title",
            message = "Message",
            positiveButtonText = "OK"
        )

        assertEquals("Title", state.title)
        assertEquals("Message", state.message)
        assertEquals("OK", state.positiveButtonText)
        assertEquals(null, state.negativeButtonText)
    }
}
