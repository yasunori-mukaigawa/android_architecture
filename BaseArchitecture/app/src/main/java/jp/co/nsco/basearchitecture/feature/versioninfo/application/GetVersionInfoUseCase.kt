package jp.co.nsco.basearchitecture.feature.versioninfo.application

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.appinfo.AppInfoProvider
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.core.time.AppClock
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.AppExternalLinksProvider
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.LatestVersionRepository
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.VersionCheckStatus
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.VersionInfo

/**
 * バージョン情報を取得する UseCase。
 *
 * GetVersionInfoUseCase は、アプリ自身のバージョン情報、最新バージョン情報、
 * 外部リンク情報、最終確認時刻を組み合わせて VersionInfo を生成する。
 *
 * ■ 提供する責務
 *   アプリ情報の取得
 *   最新バージョン情報の取得
 *   バージョン確認状態の判定
 *   外部リンク情報の取得
 *   VersionInfo の生成
 *
 * ■ 設計上の意図
 *   Presentation 層が AppInfoProvider や LatestVersionRepository、
 *   外部リンク生成処理を個別に呼び出さないようにする。
 *
 *   画面に必要なバージョン情報を UseCase で組み立てることで、
 *   ViewModel は「バージョン情報を取得する」というアプリケーション操作だけを扱える。
 *
 *   アプリ情報の取得に失敗した場合は、バージョン情報画面の前提情報が不足するため、
 *   AppResult.Failure として呼び出し側へ返す。
 *
 *   一方、最新バージョン確認に失敗した場合は、
 *   現在のアプリ情報自体は表示可能なため VersionCheckStatus.CheckFailed として扱い、
 *   VersionInfo の生成は継続する。
 *
 * ■ 注意
 *   appName / description / currentVersionName / buildNumber は現状固定値で設定している。
 *   実案件では AppInfoProvider や BuildConfig、remote config などから取得する設計へ
 *   拡張することを想定する。
 *
 *   LatestVersionRepository / AppExternalLinksProvider は domain 層の契約に依存し、
 *   実装は data 層で提供する。
 *
 * @param appInfoProvider アプリ自身のバージョン・パッケージ情報を取得する Provider。
 * @param latestVersionRepository 最新バージョン情報を取得する Repository。
 * @param externalLinksProvider アプリ関連の外部リンクを生成する Provider。
 * @param appClock 現在時刻を取得する Clock。
 */
class GetVersionInfoUseCase @Inject constructor(
    private val appInfoProvider: AppInfoProvider,
    private val latestVersionRepository: LatestVersionRepository,
    private val externalLinksProvider: AppExternalLinksProvider,
    private val appClock: AppClock
) {

    /**
     * バージョン情報を取得する。
     *
     * @return 画面表示用に組み立てた VersionInfo。
     */
    suspend operator fun invoke(): AppResult<VersionInfo> {
        val appInfo = when (val result = appInfoProvider.getAppVersionInfo()) {
            is AppResult.Success -> result.value
            is AppResult.Failure -> return result
        }

        val latestResult = latestVersionRepository.getLatestVersion()
        val latest = (latestResult as? AppResult.Success)?.value
        val status = when {
            latestResult is AppResult.Failure -> {
                VersionCheckStatus.CheckFailed
            }

            latest != null && appInfo.versionCode < latest.latestVersionCode -> {
                VersionCheckStatus.UpdateAvailable
            }

            else -> {
                VersionCheckStatus.Latest
            }
        }

        return AppResult.Success(
            VersionInfo(
                appName = "Base Architecture Sample",
                description = "アプリ情報とバージョンを確認できます。",
                currentVersionName = "v1.2.3",
                currentVersionCode = appInfo.versionCode,
                latestVersionName = latest?.latestVersionName,
                latestVersionCode = latest?.latestVersionCode,
                buildNumber = "10203",
                channel = appInfo.channel,
                lastCheckedAtMillis = appClock.nowMillis(),
                status = status,
                externalLinks = externalLinksProvider.getLinks(appInfo.packageName)
            )
        )
    }
}