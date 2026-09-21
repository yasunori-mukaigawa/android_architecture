package jp.co.nsco.basearchitecture.feature.license.presentation.detail

import javax.inject.Inject
import jp.co.nsco.basearchitecture.feature.license.presentation.common.LicenseUiErrorMapper

/**
 * ライセンス詳細画面の Presentation 補助部品をまとめる Facade。
 *
 * LicenseDetailPresentationFacade は、License Detail の ViewModel が利用する
 * Presentation 層の補助クラスを束ねる。
 *
 * ■ 提供する責務
 *   エラー表示文言 Mapper の集約
 *
 * ■ 設計上の意図
 *   ViewModel のコンストラクタ引数が増えすぎると、
 *   依存関係の見通しが悪くなる。
 *
 *   Presentation 層の補助部品を Facade として束ねることで、
 *   ViewModel は画面制御に必要な依存をまとまった単位で受け取れる。
 *
 * ■ 注意
 *   本 Facade は Presentation 層の補助依存をまとめるだけであり、
 *   業務処理や State 更新処理は持たない。
 *
 * @property errorMapper AppError を License Feature 向け表示文言へ変換する Mapper。
 */
class LicenseDetailPresentationFacade @Inject constructor(
    val errorMapper: LicenseUiErrorMapper
)