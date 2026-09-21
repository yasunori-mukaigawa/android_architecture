package jp.co.nsco.basearchitecture.feature.sample.presentation.settings

import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.resource.StringProvider
import jp.co.nsco.basearchitecture.core.theme.AppThemeId
import jp.co.nsco.basearchitecture.core.theme.AppThemeMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SampleThemeOptionMapperTest {
    private val mapper = SampleThemeOptionMapper(FakeStringProvider)

    @Test
    fun themeModeOptions_marksSelectedMode() {
        val options = mapper.themeModeOptions(AppThemeMode.Dark)

        assertEquals(3, options.size)
        assertFalse(options.first { it.mode == AppThemeMode.System }.selected)
        assertTrue(options.first { it.mode == AppThemeMode.Dark }.selected)
        assertEquals("ダーク", options.first { it.mode == AppThemeMode.Dark }.label)
    }

    @Test
    fun themePresetOptions_mapsKnownThemeIdsAndMarksSelectedPreset() {
        val options = mapper.themePresetOptions(
            availableThemeIds = listOf(AppThemeId.Default, AppThemeId.Dashboard),
            selected = AppThemeId.Dashboard
        )

        assertEquals("標準", options[0].label)
        assertFalse(options[0].selected)
        assertEquals("ダッシュボード", options[1].label)
        assertTrue(options[1].selected)
    }

    @Test
    fun themePresetOptions_usesThemeIdValueForUnknownThemeId() {
        val customId = AppThemeId("custom")

        val option = mapper.themePresetOptions(
            availableThemeIds = listOf(customId),
            selected = customId
        ).single()

        assertEquals("custom", option.label)
        assertEquals("カスタムテーマ", option.description)
        assertTrue(option.selected)
    }

    private object FakeStringProvider : StringProvider {
        override fun getString(resId: Int): String {
            return when (resId) {
                R.string.sample_theme_mode_system -> "システム設定に従う"
                R.string.sample_theme_mode_light -> "ライト"
                R.string.sample_theme_mode_dark -> "ダーク"
                R.string.sample_theme_preset_default -> "標準"
                R.string.sample_theme_preset_default_description -> "シンプルな標準テーマです。"
                R.string.sample_theme_preset_dashboard -> "ダッシュボード"
                R.string.sample_theme_preset_dashboard_description -> "カードと青系アクセントを使ったテーマです。"
                R.string.sample_theme_preset_custom_description -> "カスタムテーマ"
                else -> error("Unknown string resource: $resId")
            }
        }

        override fun getString(resId: Int, vararg args: Any): String {
            return getString(resId).format(*args)
        }
    }
}





