package jp.co.nsco.basearchitecture.core.cache

/**
 * キャッシュデータに付与するメタ情報。
 *
 * 本クラスは、キャッシュ値そのものではなく、
 * 保存時刻や有効期限などの管理情報を保持する。
 *
 * ■ 提供する責務
 *   保存時刻の保持
 *   有効期限の保持
 *   期限切れ判定
 *
 * ■ 設計上の意図
 *   キャッシュ値と有効期限情報を分離し、
 *   CacheStore 実装が共通の基準で Fresh / Stale / Miss を判定できるようにする。
 *
 * @property savedAtMillis キャッシュを保存した時刻。Unix epoch milliseconds。
 * @property expiresAtMillis キャッシュの有効期限。Unix epoch milliseconds。
 *                           null の場合は期限なしとして扱う。
 */
data class CacheMetadata(
    val savedAtMillis: Long,
    val expiresAtMillis: Long?
) {

    /**
     * キャッシュが期限切れかどうかを判定する。
     *
     * expiresAtMillis が null の場合は期限なしとして扱い、
     * 常に false を返す。
     *
     * @param nowMillis 判定基準となる現在時刻。Unix epoch milliseconds。
     * @return 期限切れの場合は true、期限内または期限なしの場合は false。
     */
    fun isExpired(nowMillis: Long): Boolean {
        val expiresAt = expiresAtMillis ?: return false
        return nowMillis >= expiresAt
    }
}