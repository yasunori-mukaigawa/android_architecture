package jp.co.nsco.basearchitecture.feature.sample.application

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.theme.ThemeRepository

/**
 * アプリ全体のテーマ設定を購読する UseCase。
 *
 * 本 UseCase は、現在のテーマモードやテーマIDを Flow として購読する
 * アプリケーション操作を表す。
 *
 * ■ 提供する責務
 *   テーマ設定購読処理の入口
 *   ThemeRepository への購読委譲
 *
 * ■ 設計上の意図
 *   Sample Feature では、Core のテーマ機能を画面から確認できるようにする。
 *
 *   ViewModel が ThemeRepository を直接扱うのではなく、
 *   UseCase を経由して「テーマ設定を購読する」という操作として扱う。
 *
 * ■ 注意
 *   本 UseCase はテーマ設定の保存や変更は行わない。
 *   テーマIDの変更は UpdateThemeIdUseCase、
 *   テーマモードの変更は UpdateThemeModeUseCase を使用する。
 *
 * @param themeRepository アプリ全体のテーマ設定を管理する Repository。
 */
class ObserveThemeSettingsUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {

    /**
     * アプリ全体のテーマ設定を購読する。
     *
     * @return テーマ設定の Flow。
     */
    operator fun invoke() = themeRepository.themeSettings
}