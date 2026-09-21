package jp.co.nsco.basearchitecture.feature.versioninfo.domain

import jp.co.nsco.basearchitecture.core.result.AppResult

/**
 * 最新バージョン情報を取得する Repository 契約。
 *
 * LatestVersionRepository は、アプリの最新バージョン情報を取得するための
 * Domain 層の契約を表す。
 *
 * ■ 提供する責務
 *   最新バージョン情報取得の契約定義
 *   取得失敗時の AppResult 返却契約
 *
 * ■ 設計上の意図
 *   GetVersionInfoUseCase が、最新バージョン情報の取得元を直接知らないようにする。
 *
 *   最新バージョン情報は、API、ローカルJSON、Remote Config、固定値など、
 *   複数の取得方式が考えられる。
 *
 *   Repository 契約を Domain 層に置き、Data 層で実装することで、
 *   取得方式の変更を UseCase や Presentation 層へ波及させない。
 *
 * ■ 注意
 *   本 Repository は「現在のアプリ情報」ではなく、
 *   「配信側・管理側が持つ最新バージョン情報」を取得する責務を持つ。
 *
 *   現在インストールされているアプリ情報は AppInfoProvider から取得する。
 */
interface LatestVersionRepository {

    /**
     * 最新バージョン情報を取得する。
     *
     * @return 最新バージョン情報の取得結果。
     */
    suspend fun getLatestVersion(): AppResult<LatestVersionInfo>
}