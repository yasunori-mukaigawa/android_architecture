package jp.co.nsco.basearchitecture.feature.license.application

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.license.LicenseInfo
import jp.co.nsco.basearchitecture.core.license.LicenseProvider
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.core.result.map

/**
 * ライセンス一覧を取得する UseCase。
 *
 * 本 UseCase は、アプリ内で表示するOSSライセンス一覧を取得する
 * アプリケーション操作を表す。
 *
 * ■ 提供する責務
 *   ライセンス一覧取得処理の入口
 *   LicenseProvider への一覧取得委譲
 *   表示しやすい順序への並び替え
 *   取得結果の AppResult 返却
 *
 * ■ 設計上の意図
 *   ViewModel が LicenseProvider を直接呼び出さず、
 *   「ライセンス一覧を取得する」という UseCase として扱えるようにする。
 *
 *   一覧表示ではライセンス名順に並んでいる方が読みやすいため、
 *   取得後に name 昇順で整列する。
 *
 *   LicenseProvider はライセンス情報の取得元を隠蔽し、
 *   本 UseCase は一覧取得というアプリケーション操作と
 *   表示前提の最小限の整形を担当する。
 *
 * ■ 注意
 *   本 UseCase は一覧項目の表示文言変換や選択時の遷移処理は行わない。
 *   それらは Presentation 層の責務とする。
 *
 * @param licenseProvider ライセンス情報を提供する Provider。
 */
class GetLicensesUseCase @Inject constructor(
    private val licenseProvider: LicenseProvider
) {

    /**
     * ライセンス一覧を取得する。
     *
     * 取得に成功した場合は、ライセンス名の昇順に並び替えて返す。
     *
     * @return ライセンス一覧の取得結果。
     */
    operator fun invoke(): AppResult<List<LicenseInfo>> {
        return licenseProvider.getLicenses()
            .map { licenses ->
                licenses.sortedBy { license -> license.name }
            }
    }
}