package jp.co.nsco.basearchitecture.core.license

/**
 * OSS ライセンスの詳細情報。
 *
 * 本クラスは、ライセンス詳細画面などで使用する、
 * ライブラリ単位のライセンス全文を含む情報を保持する。
 *
 * ■ 提供する責務
 *   ライセンス ID の保持
 *   ライブラリ名の保持
 *   ライセンス種別名の保持
 *   著作権表記の保持
 *   ライセンス本文の保持
 *
 * ■ 設計上の意図
 *   一覧表示用の LicenseInfo と詳細表示用の LicenseDetail を分離する。
 *   一覧では不要な licenseText を詳細側だけに持たせることで、
 *   表示用途ごとのデータ量と責務を分ける。
 *
 * @property id ライセンスを識別する ID。
 *              詳細画面への navigation parameter などで使用する。
 * @property name ライブラリ名または依存関係名。
 * @property licenseName ライセンス種別名。
 *                       例: Apache License 2.0。
 * @property copyright 著作権表記。
 * @property licenseText ライセンス本文。
 */
data class LicenseDetail(
    val id: String,
    val name: String,
    val licenseName: String,
    val copyright: String,
    val licenseText: String
)