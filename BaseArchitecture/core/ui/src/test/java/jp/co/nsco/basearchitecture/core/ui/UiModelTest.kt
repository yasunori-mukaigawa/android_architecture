package jp.co.nsco.basearchitecture.core.ui

import androidx.compose.ui.unit.dp
import jp.co.nsco.basearchitecture.core.theme.AppThemeId
import jp.co.nsco.basearchitecture.core.theme.AppThemeMode
import jp.co.nsco.basearchitecture.core.theme.ThemeSettings
import jp.co.nsco.basearchitecture.core.ui.dialog.DialogUiState
import jp.co.nsco.basearchitecture.core.ui.theme.BaseSpacing
import jp.co.nsco.basearchitecture.core.ui.theme.DefaultSpacing
import org.junit.Assert.assertEquals
import org.junit.Test

/** Core UIの値モデルと既定値を確認する。 */
class UiModelTest {

    @Test
    fun themeModels_exposeStableDefaultsAndKeys() {
        assertEquals("default", AppThemeId.Default.value)
        assertEquals(AppThemeMode.System, ThemeSettings().themeMode)
        assertEquals(AppThemeId.Default, ThemeSettings().themeId)
    }

    @Test
    fun spacingAndDialogModels_exposeExpectedDefaults() {
        assertEquals(BaseSpacing(), DefaultSpacing)
        assertEquals(4.dp, DefaultSpacing.xs)
        assertEquals(16.dp, DefaultSpacing.md)

        val state = DialogUiState(
            title = "Title",
            message = "Message",
            positiveButtonText = "OK"
        )
        assertEquals(null, state.negativeButtonText)
    }
}
