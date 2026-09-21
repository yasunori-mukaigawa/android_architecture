package jp.co.nsco.basearchitecture.feature.legal.presentation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LegalDocumentReducerTest {
    private val reducer = LegalDocumentReducer()

    @Test
    fun loadStarted_sets_loading_and_clears_error() {
        val current = LegalDocumentUiState(
            screenState = LegalDocumentScreenState.Error,
            title = "title",
            markdown = "markdown",
            errorMessage = "error"
        )

        val actual = reducer.reduce(current, LegalDocumentUiMessage.LoadStarted)

        assertEquals(LegalDocumentScreenState.Loading, actual.screenState)
        assertEquals("title", actual.title)
        assertEquals("markdown", actual.markdown)
        assertNull(actual.errorMessage)
    }

    @Test
    fun loadSucceeded_sets_loaded_title_and_markdown() {
        val actual = reducer.reduce(
            LegalDocumentUiState(),
            LegalDocumentUiMessage.LoadSucceeded(
                title = "利用規約",
                markdown = "# 利用規約"
            )
        )

        assertEquals(LegalDocumentScreenState.Loaded, actual.screenState)
        assertEquals("利用規約", actual.title)
        assertEquals("# 利用規約", actual.markdown)
        assertNull(actual.errorMessage)
    }

    @Test
    fun loadFailed_sets_error_and_keeps_document_content() {
        val current = LegalDocumentUiState(
            screenState = LegalDocumentScreenState.Loaded,
            title = "プライバシーポリシー",
            markdown = "# プライバシーポリシー"
        )

        val actual = reducer.reduce(
            current,
            LegalDocumentUiMessage.LoadFailed("読み込み失敗")
        )

        assertEquals(LegalDocumentScreenState.Error, actual.screenState)
        assertEquals("プライバシーポリシー", actual.title)
        assertEquals("# プライバシーポリシー", actual.markdown)
        assertEquals("読み込み失敗", actual.errorMessage)
    }
}





