package jp.co.nsco.basearchitecture.feature.sample.presentation.settings

import javax.inject.Inject
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.resource.StringProvider
import jp.co.nsco.basearchitecture.core.theme.AppThemeId
import jp.co.nsco.basearchitecture.core.theme.AppThemeMode

/**
 * テーマ設定を画面表示用の選択肢へ変換する Mapper。
 *
 * SampleThemeOptionMapper は、Core 層の AppThemeMode / AppThemeId を、
 * Sample Settings 画面で表示する ThemeModeOptionUiState /
 * ThemePresetOptionUiState に変換する。
 *
 * ■ 提供する責務
 *   テーマモード選択肢の生成
 *   テーマプリセット選択肢の生成
 *   選択状態の反映
 *   テーマ表示ラベルの解決
 *   テーマ説明文の解決
 *
 * ■ 設計上の意図
 *   Core 層の ThemeSettings は、テーマモードやテーマIDという意味だけを持つ。
 *
 *   画面に表示するラベル、説明文、選択状態は Presentation 層の関心であるため、
 *   Mapper で UiState に変換する。
 *
 *   ViewModel や Composable が string resource の分岐を直接持たないようにする。
 *
 * @param stringProvider 文字列リソース取得を行う Provider。
 */
class SampleThemeOptionMapper @Inject constructor(
    private val stringProvider: StringProvider
) {

    /**
     * テーマモード選択肢一覧を生成する。
     *
     * @param selected 現在選択中のテーマモード。
     * @return 画面表示用テーマモード選択肢一覧。
     */
    fun themeModeOptions(selected: AppThemeMode): List<ThemeModeOptionUiState> {
        return AppThemeMode.entries.map { mode ->
            ThemeModeOptionUiState(
                mode = mode,
                label = when (mode) {
                    AppThemeMode.System -> stringProvider.getString(R.string.sample_theme_mode_system)
                    AppThemeMode.Light -> stringProvider.getString(R.string.sample_theme_mode_light)
                    AppThemeMode.Dark -> stringProvider.getString(R.string.sample_theme_mode_dark)
                },
                selected = mode == selected
            )
        }
    }

    /**
     * テーマプリセット選択肢一覧を生成する。
     *
     * @param availableThemeIds 利用可能なテーマID一覧。
     * @param selected 現在選択中のテーマID。
     * @return 画面表示用テーマプリセット選択肢一覧。
     */
    fun themePresetOptions(
        availableThemeIds: List<AppThemeId>,
        selected: AppThemeId
    ): List<ThemePresetOptionUiState> {
        return availableThemeIds.map { id ->
            ThemePresetOptionUiState(
                themeId = id,
                label = when (id) {
                    AppThemeId.Default -> stringProvider.getString(R.string.sample_theme_preset_default)
                    AppThemeId.Dashboard -> stringProvider.getString(R.string.sample_theme_preset_dashboard)
                    else -> id.value
                },
                description = when (id) {
                    AppThemeId.Default -> {
                        stringProvider.getString(R.string.sample_theme_preset_default_description)
                    }

                    AppThemeId.Dashboard -> {
                        stringProvider.getString(R.string.sample_theme_preset_dashboard_description)
                    }

                    else -> {
                        stringProvider.getString(R.string.sample_theme_preset_custom_description)
                    }
                },
                selected = id == selected
            )
        }
    }
}