package jp.co.nsco.basearchitecture.core.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Validation 関連モデルの振る舞いを確認する。
 */
class ValidationModelTest {

    @Test
    fun fieldValidationResult_exposesValidAndInvalidStates() {
        val error = FieldValidationError(
            field = "name",
            code = "REQUIRED",
            reason = "name is required"
        )

        assertEquals(FieldValidationResult.Valid, FieldValidationResult.Valid)
        assertEquals(
            error,
            (FieldValidationResult.Invalid(error) as FieldValidationResult.Invalid).error
        )
    }

    @Test
    fun formValidationResult_supportsFilteringAndImmutableAdd() {
        val first = FieldValidationError("name", "REQUIRED", "required")
        val second = FieldValidationError("name", "TOO_SHORT", "too short")
        val other = FieldValidationError("email", "INVALID", "invalid")
        val result = FormValidationResult(listOf(first, second))

        assertFalse(result.isValid)
        assertTrue(result.isInvalid)
        assertEquals(listOf(first, second), result.errorsOf("name"))
        assertEquals(first, result.firstErrorOf("name"))
        assertTrue(result.hasError("name"))
        assertFalse(result.hasError("email"))
        assertNull(result.firstErrorOf("email"))

        val added = result.add(other)
        assertEquals(listOf(first, second), result.errors)
        assertEquals(listOf(first, second, other), added.errors)
        assertEquals(added.errors, result.addAll(listOf(other)).errors)
    }

    @Test
    fun formValidationResult_companionFactories_createExpectedResults() {
        val error = FieldValidationError("name", "REQUIRED", "required")

        assertTrue(FormValidationResult.Valid.isValid)
        assertFalse(FormValidationResult.Valid.isInvalid)
        assertEquals(listOf(error), FormValidationResult.invalid(error).errors)
        assertEquals(
            FormValidationResult.invalid(listOf(error)).errors,
            FormValidationResult.invalid(listOf(error)).errors
        )
    }
}
