package jp.co.nsco.basearchitecture.feature.versioninfo.domain

/**
 * アプリ関連の外部リンクを提供する Provider 契約。
 *
 * AppExternalLinksProvider は、アプリのパッケージ名などをもとに、
 * ストアページやアプリ情報ページの外部リンクを取得する契約を表す。
 *
 * ■ 提供する責務
 *   外部リンク取得の契約定義
 *   パッケージ名から外部リンク情報を生成するための境界定義
 *
 * ■ 設計上の意図
 *   外部リンクの生成方法を UseCase から分離する。
 *
 *   ストアURIやWeb fallback の組み立て方は、OS、配布方式、実案件の運用によって変わる。
 *   Provider 契約を挟むことで、UseCase は外部リンク生成の実装詳細を知らずに済む。
 *
 * @see AppExternalLinks
 */
interface AppExternalLinksProvider {

    /**
     * アプリ関連の外部リンクを取得する。
     *
     * @param packageName アプリのパッケージ名。
     * @return アプリ関連の外部リンク。
     */
    fun getLinks(packageName: String): AppExternalLinks
}