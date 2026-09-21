package jp.co.nsco.basearchitecture.feature.operationlog.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 操作ログテーブルの Room Entity。
 *
 * OperationLogEntity は、operation_logs テーブルの1レコードを表す。
 *
 * ■ 提供する責務
 *   操作ログのDB保存形式の定義
 *   Roomテーブル名・カラム名の定義
 *   検索に利用するIndexの定義
 *
 * ■ 設計上の意図
 *   Domain Model である OperationLog と、
 *   DB保存形式である OperationLogEntity を分離する。
 *
 *   DBカラム名、Index、autoGenerate などの Room 固有の都合は
 *   Entity に閉じ込め、Domain 層へ漏らさない。
 *
 *   Entity / Domain の変換は mapper で行い、
 *   Repository や UseCase が Room の保存形式を意識しないようにする。
 *
 * ■ Index 方針
 *   occurred_at_millis は直近ログ取得・古いログ削除で利用する。
 *   type は種別指定検索で利用する。
 *   result は結果別検索や分析用途への拡張を想定して定義する。
 *
 * @property id DB上の操作ログID。Roomにより自動採番される。
 * @property type 操作ログ種別。
 * @property result 操作結果。
 * @property title 操作ログタイトル。
 * @property summary 操作ログ概要。
 * @property detail 操作ログ詳細。
 * @property occurredAtMillis 操作が発生した時刻。epoch millis。
 * @property correlationId 関連処理を追跡するためのID。
 */
@Entity(
    tableName = "operation_logs",
    indices = [
        Index(value = ["occurred_at_millis"]),
        Index(value = ["type"]),
        Index(value = ["result"])
    ]
)
data class OperationLogEntity(
    /**
     * DB上の操作ログID。
     *
     * 新規追加時は 0L を渡し、Room の autoGenerate により採番する。
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    /**
     * 操作ログ種別。
     *
     * OperationLogType の保存値を保持する。
     */
    @ColumnInfo(name = "type")
    val type: String,

    /**
     * 操作結果。
     *
     * OperationLogResult の保存値を保持する。
     */
    @ColumnInfo(name = "result")
    val result: String,

    /**
     * 操作ログタイトル。
     *
     * 一覧表示や概要把握に使用する短い説明を保持する。
     */
    @ColumnInfo(name = "title")
    val title: String,

    /**
     * 操作ログ概要。
     *
     * 補足的な説明が必要な場合に使用する。
     */
    @ColumnInfo(name = "summary")
    val summary: String?,

    /**
     * 操作ログ詳細。
     *
     * エラー詳細や処理内容など、長めの補足情報を保持する。
     */
    @ColumnInfo(name = "detail")
    val detail: String?,

    /**
     * 操作が発生した時刻。
     *
     * epoch millis として保存し、並び替えや保持期間削除に利用する。
     */
    @ColumnInfo(name = "occurred_at_millis")
    val occurredAtMillis: Long,

    /**
     * 関連処理を追跡するためのID。
     *
     * 複数ログを同一処理単位で紐付けたい場合に利用する。
     */
    @ColumnInfo(name = "correlation_id")
    val correlationId: String?
)