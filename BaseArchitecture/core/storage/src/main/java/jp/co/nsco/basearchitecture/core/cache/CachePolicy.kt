package jp.co.nsco.basearchitecture.core.cache

/**
 * キャッシュと通信の利用方針を表すポリシー。
 *
 * 本 sealed interface は、データ取得時に
 * キャッシュを優先するか、通信を優先するか、片方のみを利用するかを表す。
 *
 * ■ 提供する責務
 *   キャッシュ利用方針の明示
 *   キャッシュ取得処理の分岐条件の型表現
 *   TTL 付きキャッシュ戦略の表現
 *
 * ■ 設計上の意図
 *   bool フラグや文字列でキャッシュ方針を表現せず、
 *   sealed interface として定義することで、
 *   呼び出し側と実装側の認識ずれを防ぐ。
 */
sealed interface CachePolicy {

    /**
     * キャッシュを優先して取得する方針。
     *
     * 有効なキャッシュが存在する場合はキャッシュを使用し、
     * キャッシュが存在しない場合は通信などの外部取得へフォールバックする想定。
     */
    data object CacheFirst : CachePolicy

    /**
     * 通信などの外部取得を優先する方針。
     *
     * 外部取得に成功した場合はその結果を使用し、
     * 必要に応じてキャッシュを更新する想定。
     *
     * 外部取得に失敗した場合にキャッシュへフォールバックするかどうかは、
     * 利用側または実装側の仕様で決定する。
     */
    data object NetworkFirst : CachePolicy

    /**
     * 通信などの外部取得のみを利用する方針。
     *
     * キャッシュを参照せず、常に最新取得を試みる場合に使用する。
     */
    data object NetworkOnly : CachePolicy

    /**
     * キャッシュのみを利用する方針。
     *
     * 通信などの外部取得を行わず、
     * 保存済みキャッシュだけを参照する場合に使用する。
     */
    data object CacheOnly : CachePolicy

    /**
     * TTL を考慮してキャッシュを優先する方針。
     *
     * 指定された TTL の範囲内であればキャッシュを使用し、
     * TTL を超過している場合は期限切れとして扱う想定。
     *
     * ttlMillis には 1 以上の値を指定する。
     * 0 以下の TTL はキャッシュ方針として不正であるため生成できない。
     *
     * @property ttlMillis キャッシュを有効とみなす期間。milliseconds。
     *
     * @throws IllegalArgumentException ttlMillis が 0 以下の場合。
     */
    data class CacheFirstWithTtl(
        val ttlMillis: Long
    ) : CachePolicy {
        init {
            require(ttlMillis > 0) {
                "ttlMillis must be greater than 0."
            }
        }
    }
}