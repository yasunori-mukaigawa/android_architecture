package jp.co.nsco.basearchitecture.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.co.nsco.basearchitecture.core.theme.AppThemeId
import jp.co.nsco.basearchitecture.core.theme.ThemeSettings
import jp.co.nsco.basearchitecture.core.ui.dialog.AppDialog
import jp.co.nsco.basearchitecture.core.ui.dialog.DialogUiState
import jp.co.nsco.basearchitecture.core.ui.loading.LoadingContent
import jp.co.nsco.basearchitecture.core.ui.markdown.MarkdownDocumentView
import jp.co.nsco.basearchitecture.core.ui.theme.BaseAppTheme
import jp.co.nsco.basearchitecture.core.ui.theme.DefaultBaseThemeRegistry
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Composeで提供するCore UI部品とテーマ解決を確認する。
 */
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
        composeRule.onNodeWithText("content").assertExists()
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

        composeRule.onNodeWithText("Title").assertExists()
        composeRule.onNodeWithText("Message").assertExists()
        composeRule.onNodeWithText("OK").performClick()
        composeRule.onNodeWithText("Cancel").performClick()
        composeRule.runOnIdle {
            assertTrue(positiveClicked)
            assertTrue(negativeClicked)
        }
    }

    @Test
    fun themeRegistry_resolvesDashboardAndFallsBackToDefault() {
        val registry = DefaultBaseThemeRegistry()

        composeRule.setContent {
            Text(registry.resolveThemePreset(AppThemeId.Dashboard).id.value)
        }
        composeRule.onNodeWithText("dashboard").assertExists()

        composeRule.setContent {
            Text(registry.resolveThemePreset(AppThemeId("unknown")).id.value)
        }
        composeRule.onNodeWithText("default").assertExists()
    }

    @Test
    fun baseAppTheme_appliesThemeAndRendersContent() {
        composeRule.setContent {
            BaseAppTheme(
                themeSettings = ThemeSettings(themeId = AppThemeId.Dashboard),
                themeRegistry = DefaultBaseThemeRegistry()
            ) {
                Text("themed content")
            }
        }

        composeRule.onNodeWithText("themed content").assertExists()
    }

    @Test
    fun markdownDocumentView_rendersInsideMaterialTheme() {
        composeRule.setContent {
            MaterialTheme {
                MarkdownDocumentView(
                    markdown = "**Markdown**",
                    options = jp.co.nsco.basearchitecture.core.ui.markdown.MarkdownRenderOptions(
                        enableLinks = false,
                        selectable = false
                    )
                )
            }
        }

        composeRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertDoesNotExist()
    }
}
