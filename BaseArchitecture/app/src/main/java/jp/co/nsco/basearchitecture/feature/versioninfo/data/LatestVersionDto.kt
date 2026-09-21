package jp.co.nsco.basearchitecture.feature.versioninfo.data

/**
 * 最新バージョン情報 API の response DTO。
 *
 * LatestVersionDto は、サーバーから返却される
 * 最新バージョン情報の JSON 構造に対応する Data 層のモデル。
 *
 * ■ 提供する責務
 *   API response の受け取り
 *   JSON フィールドと Kotlin オブジェクトの対応付け
 *
 * ■ 設計上の意図
 *   DTO は API のデータ構造を表すため、Domain Model と分離する。
 *
 *   API のフィールド名や response 構造が変わった場合でも、
 *   Mapper で Domain Model へ変換することで、
 *   Domain / Application / Presentation 層への影響を抑えられる。
 *
 * @property latestVersionName 最新バージョン名。
 * @property latestVersionCode 最新バージョンコード。
 */
data class LatestVersionDto(
    val latestVersionName: String,
    val latestVersionCode: Long
)