package jp.co.nsco.basearchitecture.feature.operationlog.application

import jp.co.nsco.basearchitecture.core.error.AppError
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.core.time.AppClock
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLog
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogId
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogQueryPolicy
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogRepository
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogRetentionPolicy
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogResult
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * OperationLogUseCaseTest。
 *
 * この状態または処理は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
class OperationLogUseCaseTest {

    private lateinit var repository: FakeOperationLogRepository
    private lateinit var appClock: FixedAppClock
    private lateinit var queryPolicy: OperationLogQueryPolicy
    private lateinit var retentionPolicy: OperationLogRetentionPolicy

    @Before
    fun setUp() {
        repository = FakeOperationLogRepository()
        appClock = FixedAppClock(nowMillis = 1_000_000L)
        queryPolicy = OperationLogQueryPolicy()
        retentionPolicy = OperationLogRetentionPolicy()
    }

    @Test
    fun saveOperationLogUseCase_valid_input_saves_log() = runTest {
        val useCase = SaveOperationLogUseCase(
            repository = repository,
            appClock = appClock
        )

        val actual = useCase(
            type = OperationLogType.Action,
            result = OperationLogResult.Success,
            title = "save_sample",
            summary = "sample saved",
            detail = null,
            correlationId = "cid-001"
        )

        assertTrue(actual is AppResult.Success)
        assertEquals(OperationLogId(1L), (actual as AppResult.Success).value)
        assertEquals("save_sample", repository.savedLog?.title)
        assertEquals(1_000_000L, repository.savedLog?.occurredAtMillis)
    }

    @Test
    fun saveOperationLogUseCase_blank_title_returns_failure() = runTest {
        val useCase = SaveOperationLogUseCase(
            repository = repository,
            appClock = appClock
        )

        val actual = useCase(
            type = OperationLogType.Action,
            result = OperationLogResult.Failure,
            title = " "
        )

        assertTrue(actual is AppResult.Failure)
        assertTrue((actual as AppResult.Failure).error is AppError.Validation)
        assertEquals(null, repository.savedLog)
    }

    @Test
    fun getOperationLogsUseCase_invalid_limit_uses_default_limit() = runTest {
        val useCase = GetOperationLogsUseCase(
            repository = repository,
            queryPolicy = queryPolicy
        )

        useCase(limit = 0)

        assertEquals(50, repository.lastGetRecentLimit)
    }

    @Test
    fun deleteOldOperationLogsUseCase_invalid_retention_uses_default_days() = runTest {
        val useCase = DeleteOldOperationLogsUseCase(
            repository = repository,
            appClock = appClock,
            retentionPolicy = retentionPolicy
        )

        useCase(retentionDays = 0)

        val expectedThreshold = 1_000_000L - 90L * 24L * 60L * 60L * 1000L
        assertEquals(expectedThreshold, repository.lastDeleteThresholdMillis)
    }

    private class FixedAppClock(
        private val nowMillis: Long
    ) : AppClock {
        override fun nowMillis(): Long {
            return nowMillis
        }
    }

    private class FakeOperationLogRepository : OperationLogRepository {
        var savedLog: OperationLog? = null
        var lastGetRecentLimit: Int? = null
        var lastDeleteThresholdMillis: Long? = null

        override fun observeRecent(limit: Int): Flow<AppResult<List<OperationLog>>> {
            return flowOf(AppResult.Success(emptyList()))
        }

        override suspend fun getRecent(limit: Int): AppResult<List<OperationLog>> {
            lastGetRecentLimit = limit
            return AppResult.Success(emptyList())
        }

        override suspend fun getByType(
            type: OperationLogType,
            limit: Int
        ): AppResult<List<OperationLog>> {
            return AppResult.Success(emptyList())
        }

        override suspend fun save(log: OperationLog): AppResult<OperationLogId> {
            savedLog = log
            return AppResult.Success(OperationLogId(1L))
        }

        override suspend fun deleteOlderThan(thresholdMillis: Long): AppResult<Int> {
            lastDeleteThresholdMillis = thresholdMillis
            return AppResult.Success(1)
        }

        override suspend fun clear(): AppResult<Int> {
            return AppResult.Success(0)
        }
    }
}






