package jp.co.nsco.basearchitecture.feature.versioninfo.domain

/**
 * VersionInfo 画面で表示するアプリバージョン情報。
 *
 * VersionInfo は、現在のアプリ情報、最新バージョン情報、
 * バージョン確認結果、外部リンク情報をまとめた Domain Model。
 *
 * ■ 提供する責務
 *   アプリ名の保持
 *   アプリ説明文の保持
 *   現在バージョン情報の保持
 *   最新バージョン情報の保持
 *   ビルド番号の保持
 *   配信チャネルの保持
 *   最終確認時刻の保持
 *   バージョン確認状態の保持
 *   外部リンク情報の保持
 *
 * ■ 設計上の意図
 *   VersionInfo 画面に必要な情報を UseCase で組み立て、
 *   Presentation 層へまとめて渡せるようにする。
 *
 *   最新バージョン確認に失敗した場合でも、
 *   現在のアプリ情報は表示できるようにするため、
 *   latestVersionName / latestVersionCode は nullable とする。
 *
 * @property appName アプリ名。
 * @property description アプリ説明文。
 * @property currentVersionName 現在インストールされているアプリのバージョン名。
 * @property currentVersionCode 現在インストールされているアプリのバージョンコード。
 * @property latestVersionName 最新バージョン名。取得できない場合は null。
 * @property latestVersionCode 最新バージョンコード。取得できない場合は null。
 * @property buildNumber ビルド番号。
 * @property channel 配信チャネル。
 * @property lastCheckedAtMillis バージョン確認を行った時刻。
 * @property status バージョン確認状態。
 * @property externalLinks アプリ関連の外部リンク情報。
 */
data class VersionInfo(
    val appName: String,
    val description: String,
    val currentVersionName: String,
    val currentVersionCode: Long,
    val latestVersionName: String?,
    val latestVersionCode: Long?,
    val buildNumber: String,
    val channel: String,
    val lastCheckedAtMillis: Long,
    val status: VersionCheckStatus,
    val externalLinks: AppExternalLinks
)