package jp.co.nsco.basearchitecture.feature.operationlog.domain

/**
 * 操作ログを表す Domain Model。
 *
 * OperationLog は、アプリ内で発生した画面表示、ユーザー操作、設定変更、
 * エラー、システム処理などの履歴情報を表す。
 *
 * ■ 提供する責務
 *   操作ログIDの保持
 *   操作ログ種別の保持
 *   操作結果の保持
 *   表示タイトルの保持
 *   概要・詳細情報の保持
 *   発生時刻の保持
 *   関連処理IDの保持
 *
 * ■ 設計上の意図
 *   操作ログを単なる文字列やDBレコードとして扱わず、
 *   Domain Model として表現する。
 *
 *   これにより、Application 層では OperationLog を中心に
 *   保存・取得・削除の操作を組み立てられる。
 *
 *   DB保存形式である OperationLogEntity とは分離し、
 *   Room やカラム定義の都合を Domain 層へ持ち込まない。
 *
 * @property id 操作ログID。
 * @property type 操作ログ種別。
 * @property result 操作結果。
 * @property title 操作ログタイトル。
 * @property summary 操作ログ概要。
 * @property occurredAtMillis 操作が発生した時刻。epoch millis。
 * @property detail 操作ログ詳細。
 * @property correlationId 関連する処理や一連の操作を追跡するためのID。
 */
data class OperationLog(
    val id: OperationLogId,
    val type: OperationLogType,
    val result: OperationLogResult,
    val title: String,
    val summary: String?,
    val occurredAtMillis: Long,
    val detail: String?,
    val correlationId: String?
)