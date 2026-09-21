package jp.co.nsco.basearchitecture.core.license

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * License の一覧用モデルと詳細用モデルを確認する。
 */
class LicenseModelTest {

    @Test
    fun licenseModels_keepListAndDetailInformationSeparate() {
        val info = LicenseInfo("id", "Library", "Apache License 2.0", "Copyright")
        val detail = LicenseDetail(
            id = "id",
            name = "Library",
            licenseName = "Apache License 2.0",
            copyright = "Copyright",
            licenseText = "License text"
        )

        assertEquals("id", info.id)
        assertEquals("Library", info.name)
        assertEquals(info.id, detail.id)
        assertEquals("License text", detail.licenseText)
    }
}
