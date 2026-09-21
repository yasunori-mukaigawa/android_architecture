package jp.co.nsco.basearchitecture.core.database

/**
 * Database 操作種別。
 *
 * DatabaseOperation は、DB処理の失敗を AppError へ変換するときに、
 * どの種類の操作で失敗したかを表す。
 *
 * ■ 提供する責務
 *   DB読み取り操作の表現
 *   DB書き込み操作の表現
 *   DB削除操作の表現
 *   DBトランザクション操作の表現
 *   DB初期化操作の表現
 *
 * ■ 設計上の意図
 *   DB例外をすべて同じエラーコードで扱うと、
 *   ログや表示文言、復旧方針の切り分けが難しくなる。
 *
 *   操作種別を明示することで、
 *   DatabaseErrorMapper が一貫したエラーコードへ変換できるようにする。
 */
enum class DatabaseOperation {

    /**
     * DB読み取り処理。
     */
    Read,

    /**
     * DB書き込み処理。
     */
    Write,

    /**
     * DB削除処理。
     */
    Delete,

    /**
     * 複数DB操作をまとめるトランザクション処理。
     */
    Transaction,

    /**
     * DB初期化、DBオープン、Migration などの構成処理。
     */
    Initialize
}