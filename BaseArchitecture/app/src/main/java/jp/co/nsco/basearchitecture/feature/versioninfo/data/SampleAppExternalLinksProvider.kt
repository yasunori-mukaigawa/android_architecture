package jp.co.nsco.basearchitecture.feature.versioninfo.data

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.external.ExternalUri
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.AppExternalLinks
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.AppExternalLinksProvider

/**
 * サンプルアプリ向けの外部リンク Provider 実装。
 *
 * SampleAppExternalLinksProvider は、アプリのパッケージ名をもとに
 * ストアページやアプリ情報ページを開くための URI を生成する。
 *
 * ■ 提供する責務
 *   Play Store 向け URI の生成
 *   Web fallback URI の生成
 *   Android アプリ情報画面向け URI の生成
 *
 * ■ 設計上の意図
 *   外部リンクの組み立てを UseCase から分離する。
 *
 *   Store URI や fallback URL の形式は OS や配布方式によって変わり得るため、
 *   Provider 実装へ閉じ込めることで UseCase はリンク生成の詳細を知らずに済む。
 */
class SampleAppExternalLinksProvider @Inject constructor() : AppExternalLinksProvider {

    /**
     * アプリ関連の外部リンクを取得する。
     *
     * @param packageName アプリのパッケージ名。
     * @return アプリ関連の外部リンク。
     */
    override fun getLinks(packageName: String): AppExternalLinks {
        return AppExternalLinks(
            storeUri = ExternalUri(
                primaryUri = "market://details?id=$packageName",
                fallbackUri = "https://play.google.com/store/apps/details?id=$packageName"
            ),
            appInfoPageUri = ExternalUri(
                primaryUri = "package:$packageName"
            )
        )
    }
}
