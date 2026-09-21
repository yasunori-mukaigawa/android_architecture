package jp.co.nsco.basearchitecture.core.database

/**
 * Database クエリの並び順。
 *
 * 本 enum は、SQL の ASC / DESC に相当する並び順を表す。
 *
 * ■ 設計上の意図
 *   並び順を文字列で扱うと、SQL文字列組み立て時の typo や不正値混入が起きやすい。
 *
 *   DatabaseSortOrder として型付けすることで、
 *   呼び出し側が並び順を安全に指定できるようにする。
 */
enum class DatabaseSortOrder {

    /**
     * 昇順。
     */
    Ascending,

    /**
     * 降順。
     */
    Descending
}