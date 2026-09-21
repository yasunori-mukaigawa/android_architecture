package jp.co.nsco.basearchitecture.core.database

import jp.co.nsco.basearchitecture.core.result.AppResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * DefaultDatabaseExecutor の成功・失敗・Flow・Transaction変換を確認する。
 */
class DatabaseExecutorTest {

    @Test
    fun execute_returnsSuccessAndMapsFailure() = runTest {
        val executor = createExecutor()

        assertEquals(AppResult.Success("value"), executor.execute(DatabaseOperation.Read) { "value" })

        val failure = executor.execute(DatabaseOperation.Write) {
            error("write failed")
        } as AppResult.Failure
        assertEquals(DatabaseErrorCode.WriteFailed, failure.error.code)
    }

    @Test
    fun execute_withCustomCode_keepsCustomCode() = runTest {
        val failure = createExecutor().execute("CUSTOM_ERROR") {
            error("failed")
        } as AppResult.Failure

        assertEquals("CUSTOM_ERROR", failure.error.code)
    }

    @Test
    fun execute_rethrowsCancellationException() = runTest {
        val cancellation = CancellationException("cancelled")

        try {
            createExecutor().execute(DatabaseOperation.Read) {
                throw cancellation
            }
            fail("CancellationException should be rethrown")
        } catch (actual: CancellationException) {
            assertEquals(cancellation, actual)
        }
    }

    @Test
    fun observe_emitsSuccessValuesThenFailure() = runTest {
        val values = createExecutor().observe(DatabaseOperation.Read) {
            flow {
                emit("first")
                throw IllegalStateException("read failed")
            }
        }.toList()

        assertEquals(AppResult.Success("first"), values.first())
        assertEquals(DatabaseErrorCode.ReadFailed, (values.last() as AppResult.Failure).error.code)
    }

    @Test
    fun observe_withCustomCode_mapsFlowFailure() = runTest {
        val values = createExecutor().observe("CUSTOM_READ_ERROR") {
            flow<String> { throw IllegalStateException("read failed") }
        }.toList()

        assertEquals("CUSTOM_READ_ERROR", (values.single() as AppResult.Failure).error.code)
    }

    @Test
    fun observe_rethrowsCancellationException() = runTest {
        val cancellation = CancellationException("cancelled")

        try {
            createExecutor().observe(DatabaseOperation.Read) {
                flow<String> { throw cancellation }
            }.toList()
            fail("CancellationException should be rethrown")
        } catch (actual: CancellationException) {
            assertEquals(cancellation, actual)
        }
    }

    @Test
    fun executeInTransaction_usesRunnerAndMapsFailures() = runTest {
        val runner = FakeTransactionRunner()
        val executor = DefaultDatabaseExecutor(DatabaseErrorMapper(), runner)

        assertEquals(AppResult.Success(10), executor.executeInTransaction { 10 })
        assertTrue(runner.called)

        runner.throwOnRun = true
        val failure = executor.executeInTransaction { 10 } as AppResult.Failure
        assertEquals(DatabaseErrorCode.TransactionFailed, failure.error.code)
    }

    @Test
    fun executeInTransaction_withCustomCode_keepsCustomCode() = runTest {
        val runner = FakeTransactionRunner(throwOnRun = true)
        val executor = DefaultDatabaseExecutor(DatabaseErrorMapper(), runner)

        val failure = executor.executeInTransaction("CUSTOM_TRANSACTION") { 10 } as AppResult.Failure

        assertEquals("CUSTOM_TRANSACTION", failure.error.code)
    }

    private fun createExecutor(): DefaultDatabaseExecutor {
        return DefaultDatabaseExecutor(DatabaseErrorMapper(), FakeTransactionRunner())
    }

    private class FakeTransactionRunner(
        var throwOnRun: Boolean = false
    ) : DatabaseTransactionRunner {
        var called: Boolean = false

        override suspend fun <T> runInTransaction(block: suspend () -> T): T {
            called = true
            if (throwOnRun) {
                error("transaction failed")
            }
            return block()
        }
    }
}
