package jp.co.nsco.basearchitecture.core.result

import jp.co.nsco.basearchitecture.core.error.AppError
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * AppResultTest。
 *
 * この状態または処理は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
class AppResultTest {

    @Test
    fun map_transforms_success_value() {
        val result = AppResult.Success(2)

        val actual = result.map { value ->
            value * 2
        }

        assertEquals(AppResult.Success(4), actual)
    }

    @Test
    fun map_keeps_failure_error() {
        val error = AppError.Business(
            code = "TEST-BUSINESS",
            reason = "business error"
        )
        val result = AppResult.Failure(error)

        val actual = result.map { value: Int ->
            value * 2
        }

        assertSame(error, (actual as AppResult.Failure).error)
    }

    @Test
    fun onSuccess_runs_only_for_success() {
        var calledValue = 0

        AppResult.Success(3).onSuccess { value ->
            calledValue = value
        }

        assertEquals(3, calledValue)
    }

    @Test
    fun onFailure_runs_only_for_failure() {
        val error = AppError.Unexpected()
        var called = false

        AppResult.Failure(error).onFailure { actual ->
            called = actual === error
        }

        assertTrue(called)
    }

    @Test
    fun getOrNull_and_errorOrNull_return_expected_values() {
        val error = AppError.Network(code = "TEST-NETWORK")
        val success = AppResult.Success("value")
        val failure = AppResult.Failure(error)

        assertEquals("value", success.getOrNull())
        assertNull(success.errorOrNull())
        assertNull(failure.getOrNull())
        assertSame(error, failure.errorOrNull())
    }
}






