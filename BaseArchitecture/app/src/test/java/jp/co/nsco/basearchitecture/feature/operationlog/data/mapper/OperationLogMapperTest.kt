package jp.co.nsco.basearchitecture.feature.operationlog.data.mapper

import jp.co.nsco.basearchitecture.feature.operationlog.data.local.OperationLogEntity
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLog
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogId
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogResult
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogType
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * OperationLogMapperTest。
 *
 * この状態または処理は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
class OperationLogMapperTest {

    @Test
    fun toDomain_converts_entity_to_domain() {
        val entity = OperationLogEntity(
            id = 10L,
            type = "action",
            result = "success",
            title = "save_sample",
            summary = "sample saved",
            detail = "button=save",
            occurredAtMillis = 1000L,
            correlationId = "cid-001"
        )

        val actual = entity.toDomain()

        assertEquals(OperationLogId(10L), actual.id)
        assertEquals(OperationLogType.Action, actual.type)
        assertEquals(OperationLogResult.Success, actual.result)
        assertEquals("save_sample", actual.title)
        assertEquals(1000L, actual.occurredAtMillis)
    }

    @Test
    fun toEntity_converts_domain_to_entity() {
        val domain = OperationLog(
            id = OperationLogId(20L),
            type = OperationLogType.Setting,
            result = OperationLogResult.Warning,
            title = "settings_changed",
            summary = null,
            occurredAtMillis = 2000L,
            detail = "notification=false",
            correlationId = null
        )

        val actual = domain.toEntity()

        assertEquals(20L, actual.id)
        assertEquals("setting", actual.type)
        assertEquals("warning", actual.result)
        assertEquals("settings_changed", actual.title)
        assertEquals(2000L, actual.occurredAtMillis)
    }

    @Test
    fun unknown_type_and_result_fallback_to_safe_values() {
        assertEquals(OperationLogType.System, OperationLogType.fromStorageValue("UnknownType"))
        assertEquals(OperationLogResult.Info, OperationLogResult.fromStorageValue("UnknownResult"))
    }
}






