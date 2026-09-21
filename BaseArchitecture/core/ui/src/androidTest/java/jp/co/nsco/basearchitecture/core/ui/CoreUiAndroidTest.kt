package jp.co.nsco.basearchitecture.core.ui

import androidx.compose.material3.Text
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNode
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.co.nsco.basearchitecture.core.theme.ThemeSettings
import jp.co.nsco.basearchitecture.core.ui.dialog.AppDialog
import jp.co.nsco.basearchitecture.core.ui.dialog.DialogUiState
import jp.co.nsco.basearchitecture.core.ui.loading.LoadingContent
import jp.co.nsco.basearchitecture.core.ui.theme.BaseAppTheme
import jp.co.nsco.basearchitecture.core.ui.theme.DefaultBaseThemeRegistry
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Templateとして提供するCore UI部品を確認する。 */
@RunWith(AndroidJUnit4::class)
class CoreUiAndroidTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loadingContent_showsAndHidesProgressIndicator() {
        composeRule.setContent {
            LoadingContent(isLoading = true) {
                Text("content")
            }
        }

        composeRule.onNodeWithText("content").assertExists()
        composeRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()

        composeRule.setContent {
            LoadingContent(isLoading = false) {
                Text("content")
            }
        }
        composeRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertDoesNotExist()
    }

    @Test
    fun appDialog_displaysButtonsAndEmitsClicks() {
        var positiveClicked = false
        var negativeClicked = false

        composeRule.setContent {
            AppDialog(
                dialogUiState = DialogUiState(
                    title = "Title",
                    message = "Message",
                    positiveButtonText = "OK",
                    negativeButtonText = "Cancel"
                ),
                onPositiveClick = { positiveClicked = true },
                onNegativeClick = { negativeClicked = true },
                onDismissRequest = {}
            )
        }

        composeRule.onNodeWithText("OK").performClick()
        composeRule.onNodeWithText("Cancel").performClick()
        composeRule.runOnIdle {
            assertTrue(positiveClicked)
            assertTrue(negativeClicked)
        }
    }

    @Test
    fun baseAppTheme_rendersContent() {
        composeRule.setContent {
            BaseAppTheme(
                themeSettings = ThemeSettings(),
                themeRegistry = DefaultBaseThemeRegistry()
            ) {
                Text("themed content")
            }
        }

        composeRule.onNodeWithText("themed content").assertExists()
        composeRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertDoesNotExist()
    }
}
