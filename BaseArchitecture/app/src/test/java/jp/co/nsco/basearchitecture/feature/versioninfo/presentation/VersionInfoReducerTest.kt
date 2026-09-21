package jp.co.nsco.basearchitecture.feature.versioninfo.presentation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class VersionInfoReducerTest {
    private val reducer = VersionInfoReducer()

    @Test
    fun loadStarted_sets_loading_and_keeps_content() {
        val current = VersionInfoUiState(appName = "App", errorMessage = "error")

        val actual = reducer.reduce(current, VersionInfoUiMessage.LoadStarted)

        assertEquals(VersionInfoScreenState.Loading, actual.screenState)
        assertEquals("App", actual.appName)
        assertNull(actual.errorMessage)
    }

    @Test
    fun loadSucceeded_sets_loaded() {
        val actual = reducer.reduce(
            VersionInfoUiState(),
            VersionInfoUiMessage.LoadSucceeded(VersionInfoUiState(appName = "App"))
        )

        assertEquals(VersionInfoScreenState.Loaded, actual.screenState)
        assertEquals("App", actual.appName)
    }

    @Test
    fun loadFailed_sets_error() {
        val actual = reducer.reduce(VersionInfoUiState(appName = "App"), VersionInfoUiMessage.LoadFailed("failed"))

        assertEquals(VersionInfoScreenState.Error, actual.screenState)
        assertEquals("App", actual.appName)
        assertEquals("failed", actual.errorMessage)
        assertTrue(actual.showRetryButton)
    }
}






