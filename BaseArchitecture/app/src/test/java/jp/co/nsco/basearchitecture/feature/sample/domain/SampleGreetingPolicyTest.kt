package jp.co.nsco.basearchitecture.feature.sample.domain

import java.time.ZoneId
import java.time.ZonedDateTime
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * SampleGreetingPolicyTest。
 *
 * この状態または処理は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
class SampleGreetingPolicyTest {

    private lateinit var policy: SampleGreetingPolicy

    @Before
    fun setUp() {
        policy = SampleGreetingPolicy()
    }

    @Test
    fun resolveGreetingType_returns_morning_from_5_to_before_12() {
        assertEquals(SampleGreetingType.Morning, policy.resolveGreetingType(dateTimeAt(hour = 5)))
        assertEquals(SampleGreetingType.Morning, policy.resolveGreetingType(dateTimeAt(hour = 11)))
    }

    @Test
    fun resolveGreetingType_returns_afternoon_from_12_to_before_18() {
        assertEquals(SampleGreetingType.Afternoon, policy.resolveGreetingType(dateTimeAt(hour = 12)))
        assertEquals(SampleGreetingType.Afternoon, policy.resolveGreetingType(dateTimeAt(hour = 17)))
    }

    @Test
    fun resolveGreetingType_returns_evening_from_18_to_before_5() {
        assertEquals(SampleGreetingType.Evening, policy.resolveGreetingType(dateTimeAt(hour = 18)))
        assertEquals(SampleGreetingType.Evening, policy.resolveGreetingType(dateTimeAt(hour = 4)))
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






