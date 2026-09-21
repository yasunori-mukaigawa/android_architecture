package jp.co.nsco.basearchitecture.feature.operationlog.domain

/**
 * 操作ログID。
 *
 * OperationLogId は、操作ログを識別するIDを表す Value Object。
 *
 * ■ 提供する責務
 *   操作ログIDの型付け
 *   Long値の取り違え防止
 *
 * ■ 設計上の意図
 *   操作ログIDを単なる Long として扱うと、
 *   他のIDや数値項目と取り違える可能性がある。
 *
 *   OperationLogId として型付けすることで、
 *   Domain / Application 層でIDの意味を明確にする。
 *
 * ■ 注意
 *   0L は保存前の仮IDとして利用する。
 *   Room の autoGenerate により採番された値が、保存後の正式なIDとなる。
 *
 * @property value 操作ログIDの実値。
 */
@JvmInline
value class OperationLogId(
    val value: Long
)