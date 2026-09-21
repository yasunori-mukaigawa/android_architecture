package jp.co.nsco.basearchitecture.core.license

/**
 * OSS ライセンスの一覧表示用情報。
 *
 * 本クラスは、ライセンス一覧画面などで使用する、
 * ライセンス本文を含まない軽量な表示情報を保持する。
 *
 * ■ 提供する責務
 *   ライセンス ID の保持
 *   ライブラリ名の保持
 *   ライセンス種別名の保持
 *   著作権表記の保持
 *
 * ■ 設計上の意図
 *   一覧表示ではライセンス本文を必要としないため、
 *   LicenseDetail から licenseText を除いた軽量なモデルとして扱う。
 *
 *   詳細表示が必要な場合は、id を使用して LicenseProvider から
 *   LicenseDetail を取得する。
 *
 * @property id ライセンスを識別する ID。
 *              詳細画面への navigation parameter などで使用する。
 * @property name ライブラリ名または依存関係名。
 * @property licenseName ライセンス種別名。
 *                       例: Apache License 2.0。
 * @property copyright 著作権表記。
 */
data class LicenseInfo(
    val id: String,
    val name: String,
    val licenseName: String,
    val copyright: String
)