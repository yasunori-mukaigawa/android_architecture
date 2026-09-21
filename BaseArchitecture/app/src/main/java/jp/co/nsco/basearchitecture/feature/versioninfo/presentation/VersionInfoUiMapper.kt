package jp.co.nsco.basearchitecture.feature.versioninfo.presentation

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.resource.StringProvider
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.VersionCheckStatus
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.VersionInfo

/**
 * VersionInfo を画面表示用 State へ変換する Mapper。
 *
 * VersionInfoUiMapper は、Domain Model である VersionInfo を、
 * VersionInfo 画面でそのまま描画できる VersionInfoUiState へ変換する。
 *
 * ■ 提供する責務
 *   Domain Model から UiState への変換
 *   バージョン確認状態に応じた表示文言の解決
 *   最終確認時刻の表示形式変換
 *   ボタン表示条件の判定
 *
 * ■ 設計上の意図
 *   Domain 層は、表示文言や日時フォーマット、ボタン表示条件を知らない。
 *
 *   Presentation 層の Mapper に変換処理を集約することで、
 *   ViewModel や Composable が string resource、DateTimeFormatter、
 *   表示条件分岐を直接持たないようにする。
 *
 * @param stringProvider 文字列リソース取得を行う Provider。
 */
class VersionInfoUiMapper @Inject constructor(
    private val stringProvider: StringProvider
) {

    /**
     * 最終確認時刻の表示フォーマット。
     *
     * 端末の timezone に合わせて表示する。
     */
    private val formatter = DateTimeFormatter.ofPattern(LastCheckedAtPattern)
        .withZone(ZoneId.systemDefault())

    /**
     * VersionInfo を VersionInfoUiState へ変換する。
     *
     * @param info バージョン情報 Domain Model。
     * @return 画面表示用 UI State。
     */
    fun map(info: VersionInfo): VersionInfoUiState {
        val statusTitle = when (info.status) {
            VersionCheckStatus.Latest -> {
                stringProvider.getString(R.string.version_info_status_latest_title)
            }

            VersionCheckStatus.UpdateAvailable -> {
                stringProvider.getString(R.string.version_info_status_update_available_title)
            }

            VersionCheckStatus.CheckFailed -> {
                stringProvider.getString(R.string.version_info_status_check_failed_title)
            }
        }

        val statusMessage = when (info.status) {
            VersionCheckStatus.Latest -> {
                stringProvider.getString(R.string.version_info_status_latest_message)
            }

            VersionCheckStatus.UpdateAvailable -> {
                stringProvider.getString(R.string.version_info_status_update_available_message)
            }

            VersionCheckStatus.CheckFailed -> {
                stringProvider.getString(R.string.version_info_status_check_failed_message)
            }
        }

        return VersionInfoUiState(
            appName = info.appName,
            description = info.description,
            channel = info.channel,
            currentVersion = info.currentVersionName,
            latestVersion = info.latestVersionName,
            buildNumber = info.buildNumber,
            lastCheckedAt = formatter.format(Instant.ofEpochMilli(info.lastCheckedAtMillis)),
            status = info.status,
            statusTitle = statusTitle,
            statusMessage = statusMessage,
            showOpenStoreButton = info.status == VersionCheckStatus.UpdateAvailable,
            showRetryButton = info.status == VersionCheckStatus.CheckFailed,
            showOpenAppInfoButton = true
        )
    }

    private companion object {

        /**
         * 最終確認時刻の表示フォーマット。
         */
        private const val LastCheckedAtPattern = "yyyy/MM/dd HH:mm"
    }
}