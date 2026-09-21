package jp.co.nsco.basearchitecture.feature.sample.application

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.theme.AppThemeMode
import jp.co.nsco.basearchitecture.core.theme.ThemeRepository

/**
 * アプリのテーマモードを更新する UseCase。
 *
 * 本 UseCase は、System / Light / Dark のテーマモードを更新する
 * アプリケーション操作を表す。
 *
 * ■ 提供する責務
 *   テーマモード更新処理の入口
 *   ThemeRepository への更新委譲
 *
 * ■ 設計上の意図
 *   Sample Feature では、Core のテーマモード切り替え機能を画面から確認できるようにする。
 *
 *   ViewModel が ThemeRepository を直接扱うのではなく、
 *   UseCase を経由して「テーマモードを更新する」という操作として扱う。
 *
 * ■ 注意
 *   本 UseCase はテーマの色やTypographyなどの解決は行わない。
 *   実際のテーマ適用は AppRoot / BaseAppTheme 側の責務とする。
 *
 * @param themeRepository アプリ全体のテーマ設定を管理する Repository。
 */
class UpdateThemeModeUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {

    /**
     * アプリのテーマモードを更新する。
     *
     * @param themeMode 適用するテーマモード。
     * @return テーマモード更新結果。
     */
    suspend operator fun invoke(themeMode: AppThemeMode) = themeRepository.updateThemeMode(themeMode)
}