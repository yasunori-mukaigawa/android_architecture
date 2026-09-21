package jp.co.nsco.basearchitecture.core.database

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

/**
 * Database の設定値・ページング値・エラーコード契約を確認する。
 */
class DatabaseModelTest {

    @Test
    fun databaseConfig_keepsValuesAndRejectsBlankName() {
        val config = DatabaseConfig("sample.db", fallbackToDestructiveMigration = true)

        assertEquals("sample.db", config.name)
        assertEquals(true, config.fallbackToDestructiveMigration)
        assertIllegalArgument { DatabaseConfig(" ") }
    }

    @Test
    fun databasePaging_valuesValidateAndAdvance() {
        assertEquals(0, DatabaseOffset.First.value)
        assertEquals(50, DatabaseQueryLimit.Default.value)
        assertEquals(100, DatabaseQueryLimit.Large.value)
        assertIllegalArgument { DatabaseOffset(-1) }
        assertIllegalArgument { DatabaseQueryLimit(0) }

        val first = DatabasePageRequest(
            limit = DatabaseQueryLimit(20),
            offset = DatabaseOffset(40)
        )
        assertEquals(DatabaseOffset(60), first.next().offset)
        assertEquals(DatabaseSortOrder.Ascending, DatabaseSortOrder.Ascending)
        assertEquals(DatabaseSortOrder.Descending, DatabaseSortOrder.Descending)
    }

    @Test
    fun databaseErrorCode_mapsEveryOperation() {
        assertEquals(DatabaseErrorCode.ReadFailed, DatabaseErrorCode.fromOperation(DatabaseOperation.Read))
        assertEquals(DatabaseErrorCode.WriteFailed, DatabaseErrorCode.fromOperation(DatabaseOperation.Write))
        assertEquals(DatabaseErrorCode.DeleteFailed, DatabaseErrorCode.fromOperation(DatabaseOperation.Delete))
        assertEquals(
            DatabaseErrorCode.TransactionFailed,
            DatabaseErrorCode.fromOperation(DatabaseOperation.Transaction)
        )
        assertEquals(
            DatabaseErrorCode.InitializeFailed,
            DatabaseErrorCode.fromOperation(DatabaseOperation.Initialize)
        )
    }

    @Test
    fun databaseErrorMapper_createsLocalStorageErrors() {
        val mapper = DatabaseErrorMapper()
        val cause = IllegalStateException("db")

        val operationError = mapper.fromThrowable(cause, DatabaseOperation.Read)
        val customError = mapper.fromThrowable(cause, "CUSTOM_DATABASE_ERROR")

        assertEquals(DatabaseErrorCode.ReadFailed, operationError.code)
        assertEquals(cause, operationError.cause)
        assertEquals("CUSTOM_DATABASE_ERROR", customError.code)
    }

    private fun assertIllegalArgument(block: () -> Unit) {
        try {
            block()
            fail("Expected IllegalArgumentException")
        } catch (_: IllegalArgumentException) {
            // Expected validation failure.
        }
    }
}
