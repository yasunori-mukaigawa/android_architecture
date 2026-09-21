package jp.co.nsco.basearchitecture.feature.operationlog.data.mapper

import jp.co.nsco.basearchitecture.feature.operationlog.data.local.OperationLogEntity
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLog
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogId
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogResult
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogType

/**
 * OperationLogEntity を OperationLog へ変換する。
 *
 * 本関数は、Room Entity として保存されている操作ログを、
 * Domain Model である OperationLog に変換する。
 *
 * ■ 提供する責務
 *   DB保存形式から Domain Model への変換
 *   DB上のIDから OperationLogId への変換
 *   保存文字列から OperationLogType への復元
 *   保存文字列から OperationLogResult への復元
 *
 * ■ 設計上の意図
 *   Room 固有の Entity を Domain 層へ漏らさないようにする。
 *
 *   Data 層では OperationLogEntity として扱い、
 *   Repository より上位では OperationLog として扱うことで、
 *   DB保存形式の変更が Application / Presentation 層へ波及しにくくなる。
 *
 *   type / result は DB 上では文字列として保存するが、
 *   Domain 層では OperationLogType / OperationLogResult として型付けして扱う。
 *
 * ■ 注意
 *   type / result は保存用の storageValue から復元する。
 *
 *   不明な保存値がDBに存在した場合でも、
 *   OperationLogType.fromStorageValue / OperationLogResult.fromStorageValue 側で
 *   安全な既定値へフォールバックし、表示継続を優先する。
 *
 * @return Domain Model に変換された操作ログ。
 */
fun OperationLogEntity.toDomain(): OperationLog {
    return OperationLog(
        id = OperationLogId(id),
        type = OperationLogType.fromStorageValue(type),
        result = OperationLogResult.fromStorageValue(result),
        title = title,
        summary = summary,
        occurredAtMillis = occurredAtMillis,
        detail = detail,
        correlationId = correlationId
    )
}

/**
 * OperationLog を OperationLogEntity へ変換する。
 *
 * 本関数は、Domain Model である OperationLog を、
 * Room Entity として保存できる形式へ変換する。
 *
 * ■ 提供する責務
 *   Domain Model から DB保存形式への変換
 *   OperationLogId から DB上のIDへの変換
 *   OperationLogType から保存文字列への変換
 *   OperationLogResult から保存文字列への変換
 *
 * ■ 設計上の意図
 *   Domain Model を Room Entity に直接依存させず、
 *   Data 層の Mapper で保存形式への変換を行う。
 *
 *   これにより、DBカラム構成や保存値の形式を変更する場合でも、
 *   修正箇所を Data 層に閉じ込められる。
 *
 *   enum の name を DB保存値として使用すると、
 *   enum 名変更時に既存DBとの互換性が壊れる。
 *
 *   そのため、DB保存値には OperationLogType / OperationLogResult が持つ
 *   storageValue を使用する。
 *
 * ■ 注意
 *   id が 0 以下の場合は新規追加扱いとして 0L を設定する。
 *   Room の autoGenerate により、insert 時にDB側でIDが採番される。
 *
 * @return Room に保存するための Entity。
 */
fun OperationLog.toEntity(): OperationLogEntity {
    return OperationLogEntity(
        id = if (id.value > 0L) id.value else 0L,
        type = type.toStorageValue(),
        result = result.toStorageValue(),
        title = title,
        summary = summary,
        detail = detail,
        occurredAtMillis = occurredAtMillis,
        correlationId = correlationId
    )
}