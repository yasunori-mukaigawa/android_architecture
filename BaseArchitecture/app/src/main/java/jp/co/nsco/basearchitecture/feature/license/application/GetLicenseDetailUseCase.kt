package jp.co.nsco.basearchitecture.feature.license.application

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.error.AppError
import jp.co.nsco.basearchitecture.core.license.LicenseDetail
import jp.co.nsco.basearchitecture.core.license.LicenseProvider
import jp.co.nsco.basearchitecture.core.result.AppResult

private const val LicenseIdRequired = "LICENSE_ID_REQUIRED"
private const val LicenseIdField = "licenseId"

/**
 * ライセンス詳細を取得する UseCase。
 *
 * 本 UseCase は、指定されたライセンスIDに対応するライセンス詳細情報を取得する
 * アプリケーション操作を表す。
 *
 * ■ 提供する責務
 *   ライセンス詳細取得処理の入口
 *   ライセンスIDの必須チェック
 *   LicenseProvider への詳細取得委譲
 *   取得結果の AppResult 返却
 *
 * ■ 設計上の意図
 *   ViewModel が LicenseProvider を直接呼び出さず、
 *   「ライセンス詳細を取得する」というアプリケーション操作として扱えるようにする。
 *
 *   また、空のライセンスIDは取得対象を特定できないため、
 *   LicenseProvider へ渡す前に UseCase 側で ValidationError として扱う。
 *
 *   これにより、呼び出し側は正常系・検証エラー・取得失敗を
 *   AppResult として一貫して処理できる。
 *
 * ■ 注意
 *   本 UseCase はライセンス本文の整形や表示文言の解決は行わない。
 *   表示用メッセージへの変換は Presentation 層の責務とする。
 *
 * @param licenseProvider ライセンス情報を提供する Provider。
 */
class GetLicenseDetailUseCase @Inject constructor(
    private val licenseProvider: LicenseProvider
) {

    /**
     * 指定されたライセンスIDに対応するライセンス詳細を取得する。
     *
     * ライセンスIDが空白の場合は、取得処理を行わず
     * AppError.Validation を返す。
     *
     * @param id 取得対象のライセンスID。
     * @return ライセンス詳細の取得結果。
     */
    operator fun invoke(id: String): AppResult<LicenseDetail> {
        if (id.isBlank()) {
            return AppResult.Failure(
                AppError.Validation(
                    code = LicenseIdRequired,
                    field = LicenseIdField,
                    reason = "License id is required."
                )
            )
        }

        return licenseProvider.getLicenseDetail(id)
    }
}