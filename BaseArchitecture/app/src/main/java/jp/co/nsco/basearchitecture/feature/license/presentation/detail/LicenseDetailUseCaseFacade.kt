package jp.co.nsco.basearchitecture.feature.license.presentation.detail

import javax.inject.Inject
import jp.co.nsco.basearchitecture.feature.license.application.GetLicenseDetailUseCase

/**
 * ライセンス詳細画面で利用する UseCase をまとめる Facade。
 *
 * LicenseDetailUseCaseFacade は、License Detail の ViewModel が利用する
 * Application 層の UseCase を束ねる。
 *
 * ■ 提供する責務
 *   ライセンス詳細取得 UseCase の集約
 *
 * ■ 設計上の意図
 *   ViewModel のコンストラクタ引数が増えすぎると、
 *   依存関係の見通しが悪くなる。
 *
 *   画面で使用する UseCase を Facade としてまとめることで、
 *   ViewModel は必要なアプリケーション操作をまとまった単位で受け取れる。
 *
 * ■ 注意
 *   本 Facade は UseCase の集約のみを担当する。
 *   画面状態の更新、エラー文言変換、副作用発行は ViewModel 側の責務とする。
 *
 * @property getLicenseDetail ライセンス詳細を取得する UseCase。
 */
class LicenseDetailUseCaseFacade @Inject constructor(
    val getLicenseDetail: GetLicenseDetailUseCase
)