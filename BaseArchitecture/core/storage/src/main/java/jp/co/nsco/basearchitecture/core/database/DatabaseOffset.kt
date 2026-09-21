package jp.co.nsco.basearchitecture.core.database

/**
 * Database クエリで使用する取得開始位置。
 *
 * DatabaseOffset は、SQL の OFFSET に相当する値を型付けする。
 *
 * ■ 設計上の意図
 *   offset を Int のまま扱うと、負数など不正な値が入りやすい。
 *
 *   DatabaseOffset として型付けし、生成時に値を検証することで、
 *   DAO 呼び出し前に不正な取得開始位置を排除する。
 *
 * @property value 取得開始位置。
 *
 * @throws IllegalArgumentException value が負数の場合。
 */
@JvmInline
value class DatabaseOffset(
    val value: Int
) {
    init {
        require(value >= 0) {
            "Database offset must be greater than or equal to 0."
        }
    }

    companion object {

        /**
         * 先頭から取得する場合の offset。
         */
        val First = DatabaseOffset(0)
    }
}