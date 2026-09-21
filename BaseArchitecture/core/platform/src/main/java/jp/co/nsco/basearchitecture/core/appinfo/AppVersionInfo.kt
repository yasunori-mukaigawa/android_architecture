package jp.co.nsco.basearchitecture.core.appinfo

/**
 * アプリのバージョン表示に必要な情報。
 *
 * 本クラスは、バージョン情報画面や問い合わせ情報表示などで利用する、
 * アプリ自身の識別情報を保持する。
 *
 * ■ 提供する責務
 *   アプリ名の保持
 *   パッケージ名の保持
 *   バージョン名の保持
 *   バージョンコードの保持
 *   配布チャネルの保持
 *
 * ■ 設計上の意図
 *   Android の PackageInfo をそのまま UI 側へ渡さず、
 *   アプリ内で扱いやすい表示用データとして切り出す。
 *
 * @property appName 利用者向けに表示するアプリ名。
 * @property packageName アプリを識別するパッケージ名。
 * @property versionName 利用者向けに表示するバージョン名。
 * @property versionCode 内部管理・問い合わせ時に使用するバージョンコード。
 * @property channel 配布チャネル。Production / Staging などの環境識別に使用する。
 */
data class AppVersionInfo(
    val appName: String,
    val packageName: String,
    val versionName: String,
    val versionCode: Long,
    val channel: String
)