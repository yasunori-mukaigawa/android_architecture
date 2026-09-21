package jp.co.nsco.basearchitecture.feature.sample.application

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.theme.AppThemeId
import jp.co.nsco.basearchitecture.core.theme.ThemeRepository

/**
 * アプリのテーマIDを更新する UseCase。
 *
 * 本 UseCase は、現在適用するテーマプリセットIDを更新する
 * アプリケーション操作を表す。
 *
 * ■ 提供する責務
 *   テーマID更新処理の入口
 *   ThemeRepository への更新委譲
 *
 * ■ 設計上の意図
 *   Sample Feature では、Core のテーマ切り替え機能を画面から確認できるようにする。
 *
 *   ViewModel が ThemeRepository を直接扱うのではなく、
 *   UseCase を経由して「テーマIDを更新する」という操作として扱う。
 *
 * ■ 注意
 *   本 UseCase はテーマIDの妥当性判定や表示名変換は行わない。
 *   利用可能なテーマID一覧は GetAvailableThemePresetsUseCase から取得する。
 *
 * @param themeRepository アプリ全体のテーマ設定を管理する Repository。
 */
class UpdateThemeIdUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {

    /**
     * アプリのテーマIDを更新する。
     *
     * @param themeId 適用するテーマID。
     * @return テーマID更新結果。
     */
    suspend operator fun invoke(themeId: AppThemeId) = themeRepository.updateThemeId(themeId)
}