package jp.co.nsco.basearchitecture.core.database

/**
 * Database のページング取得条件。
 *
 * DatabasePageRequest は、LIMIT / OFFSET による
 * DB取得範囲を表す。
 *
 * ■ 提供する責務
 *   取得件数の保持
 *   取得開始位置の保持
 *   次ページ条件の生成
 *
 * ■ 設計上の意図
 *   limit / offset を個別の Int として渡すと、
 *   引数順の取り違えや不正値混入が起きやすい。
 *
 *   DatabasePageRequest としてまとめることで、
 *   DAO / LocalDataSource / Repository 間のページング条件を安全に受け渡す。
 *
 * @property limit 取得件数。
 * @property offset 取得開始位置。
 */
data class DatabasePageRequest(
    val limit: DatabaseQueryLimit = DatabaseQueryLimit.Default,
    val offset: DatabaseOffset = DatabaseOffset.First
) {

    /**
     * 次ページの取得条件を生成する。
     *
     * @return offset を limit 分だけ進めた DatabasePageRequest。
     */
    fun next(): DatabasePageRequest {
        return copy(
            offset = DatabaseOffset(offset.value + limit.value)
        )
    }

    companion object {

        /**
         * 先頭ページの取得条件。
         */
        val First = DatabasePageRequest()
    }
}