package jp.co.nsco.basearchitecture.feature.sample.application

import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.core.time.DateTimeProvider
import jp.co.nsco.basearchitecture.feature.sample.domain.SampleGreetingPolicy
import java.time.ZoneId
import java.time.ZonedDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * GetSampleHomeHeaderUseCaseTest。
 *
 * この状態または処理は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
class GetSampleHomeHeaderUseCaseTest {

    @Test
    fun invoke_returns_header_from_provider_datetime() {
        val useCase = GetSampleHomeHeaderUseCase(
            dateTimeProvider = FixedDateTimeProvider(dateTimeAt(hour = 10)),
            greetingPolicy = SampleGreetingPolicy()
        )

        val actual = useCase()

        assertTrue(actual is AppResult.Success)
        val header = (actual as AppResult.Success).value
        assertEquals(2026, header.date.year)
        assertEquals(6, header.date.monthValue)
        assertEquals(7, header.date.dayOfMonth)
    }

    private class FixedDateTimeProvider(
        private val dateTime: ZonedDateTime
    ) : DateTimeProvider {

        override fun now(): ZonedDateTime {
            return dateTime
        }
    }

    private fun dateTimeAt(hour: Int): ZonedDateTime {
        return ZonedDateTime.of(
            2026,
            6,
            7,
            hour,
            0,
            0,
            0,
            ZoneId.of("Asia/Tokyo")
        )
    }
}






