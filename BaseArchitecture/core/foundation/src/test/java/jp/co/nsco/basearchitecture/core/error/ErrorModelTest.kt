package jp.co.nsco.basearchitecture.core.error

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Errorモデルの契約を確認する。
 */
class ErrorModelTest {

    @Test
    fun appError_variants_keepClassificationAndDetails() {
        val cause = IllegalStateException("cause")

        assertEquals("VALIDATION", AppError.Validation("VALIDATION", "name", "required").code)
        assertEquals("name", AppError.Validation("VALIDATION", "name", "required").field)
        assertEquals("business", AppError.Business("BUSINESS", "business").reason)
        assertEquals(cause, AppError.LocalStorage("LOCAL", cause).cause)
        assertEquals(cause, AppError.Network("NETWORK", cause).cause)
        assertEquals(503, AppError.Api("API", 503, "unavailable").statusCode)
        assertEquals("invalid", AppError.ApiInvalidResponse("INVALID", "invalid").reason)
        assertEquals("UNEXPECTED", AppError.Unexpected().code)
        assertNull(AppError.Unexpected().cause)
    }

    @Test
    fun uiError_keepsPresentationInformationAndDefaultsActions() {
        val error = UiError(
            code = "ERROR",
            title = "Title",
            message = "Message"
        )

        assertEquals("ERROR", error.code)
        assertEquals("Title", error.title)
        assertEquals("Message", error.message)
        assertNull(error.supportMessage)
        assertEquals(emptyList<UiErrorAction>(), error.actions)
        assertEquals(
            listOf(UiErrorAction.Close, UiErrorAction.Retry, UiErrorAction.Restart, UiErrorAction.Skip),
            UiErrorAction.entries
        )
    }
}
