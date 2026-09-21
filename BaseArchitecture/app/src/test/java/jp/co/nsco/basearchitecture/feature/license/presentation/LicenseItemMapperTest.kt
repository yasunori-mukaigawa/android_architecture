package jp.co.nsco.basearchitecture.feature.license.presentation.list

import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.license.LicenseInfo
import jp.co.nsco.basearchitecture.core.resource.StringProvider
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * LicenseItemMapperTest。
 *
 * この状態または処理は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
class LicenseItemMapperTest {

    @Test
    fun map_converts_license_info_to_ui_state() {
        val mapper = LicenseItemMapper(FakeStringProvider())
        val info = LicenseInfo(
            id = "androidx-core",
            name = "AndroidX Core",
            licenseName = "Apache License 2.0",
            copyright = "copyright"
        )

        val actual = mapper.map(info)

        assertEquals("androidx-core", actual.id)
        assertEquals("AndroidX Core", actual.name)
        assertEquals("Apache License 2.0", actual.licenseName)
        assertEquals("copyright", actual.copyright)
    }

    @Test
    fun map_replaces_blank_values_with_unknown() {
        val mapper = LicenseItemMapper(FakeStringProvider())

        val actual = mapper.map(
            LicenseInfo(
                id = "blank",
                name = "",
                licenseName = "",
                copyright = ""
            )
        )

        assertEquals("不明", actual.name)
        assertEquals("不明", actual.licenseName)
        assertEquals("不明", actual.copyright)
    }

    private class FakeStringProvider : StringProvider {
        override fun getString(resId: Int): String {
            return when (resId) {
                R.string.license_unknown -> "不明"
                else -> error("Unexpected resId: $resId")
            }
        }

        override fun getString(resId: Int, vararg args: Any): String {
            return getString(resId)
        }
    }
}








