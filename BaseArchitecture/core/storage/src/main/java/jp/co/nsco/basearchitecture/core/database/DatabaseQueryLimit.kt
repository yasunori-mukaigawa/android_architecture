package jp.co.nsco.basearchitecture.core.database

/**
 * Database クエリで使用する取得件数制限。
 *
 * DatabaseQueryLimit は、SQL の LIMIT に相当する値を型付けする。
 *
 * ■ 設計上の意図
 *   limit を Int のまま扱うと、0 や負数など不正な値が入りやすい。
 *
 *   DatabaseQueryLimit として型付けし、生成時に値を検証することで、
 *   DAO 呼び出し前に不正な取得件数を排除する。
 *
 * @property value 取得件数。
 *
 * @throws IllegalArgumentException value が 0 以下の場合。
 */
@JvmInline
value class DatabaseQueryLimit(
    val value: Int
) {
    init {
        require(value > 0) {
            "Database query limit must be greater than 0."
        }
    }

    companion object {

        /**
         * 標準取得件数。
         */
        val Default = DatabaseQueryLimit(50)

        /**
         * 大きめの取得件数。
         */
        val Large = DatabaseQueryLimit(100)
    }
}