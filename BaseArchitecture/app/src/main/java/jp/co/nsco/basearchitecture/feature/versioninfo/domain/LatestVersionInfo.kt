package jp.co.nsco.basearchitecture.feature.versioninfo.domain

/**
 * 配信側・管理側が持つ最新バージョン情報。
 *
 * LatestVersionInfo は、現在インストールされているアプリではなく、
 * サーバーや設定ファイルなどから取得した最新バージョン情報を表す Domain Model。
 *
 * ■ 提供する責務
 *   最新バージョン名の保持
 *   最新バージョンコードの保持
 *
 * ■ 設計上の意図
 *   現在のアプリ情報と最新バージョン情報を分離して扱う。
 *
 *   現在のアプリ情報は AppInfoProvider から取得し、
 *   配信側・管理側が持つ最新情報は LatestVersionRepository から取得する。
 *
 * @property latestVersionName 最新バージョン名。
 * @property latestVersionCode 最新バージョンコード。
 */
data class LatestVersionInfo(
    val latestVersionName: String,
    val latestVersionCode: Long
)