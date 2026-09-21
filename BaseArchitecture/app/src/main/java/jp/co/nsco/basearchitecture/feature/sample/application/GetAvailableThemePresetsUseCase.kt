package jp.co.nsco.basearchitecture.feature.sample.application

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.ui.theme.BaseThemeRegistry

/**
 * 利用可能なテーマID一覧を取得する UseCase。
 *
 * 本 UseCase は、アプリに登録されているテーマプリセットのID一覧を取得する
 * アプリケーション操作を表す。
 *
 * ■ 提供する責務
 *   テーマプリセットID一覧取得の入口
 *   BaseThemeRegistry への取得委譲
 *
 * ■ 設計上の意図
 *   ViewModel が BaseThemeRegistry を直接参照せず、
 *   「選択可能なテーマ一覧を取得する」という UseCase として扱えるようにする。
 *
 *   Sample Feature では、Core のテーマ切り替え機能を画面から確認するために、
 *   登録済みテーマIDを取得して UI に表示する。
 *
 * ■ 注意
 *   本 UseCase はテーマの表示名や見た目の解決は行わない。
 *   テーマIDをどのように表示するかは Presentation 層の責務とする。
 *
 * @param themeRegistry アプリで利用可能なテーマプリセットを管理する Registry。
 */
class GetAvailableThemePresetsUseCase @Inject constructor(
    private val themeRegistry: BaseThemeRegistry
) {

    /**
     * 利用可能なテーマID一覧を取得する。
     *
     * @return 登録済みテーマID一覧。
     */
    operator fun invoke() = themeRegistry.availableThemeIds()
}