package jp.co.nsco.basearchitecture.core.cache

/**
 * キャッシュ取得結果を表す型。
 *
 * 本 sealed interface は、キャッシュから値を取得できたか、
 * 取得できた値が有効か、取得できなかった理由は何かを表す。
 *
 * ■ 提供する責務
 *   有効なキャッシュ値の表現
 *   期限切れ等により古いキャッシュ値の表現
 *   キャッシュ未取得理由の表現
 *
 * ■ 設計上の意図
 *   null や例外でキャッシュ状態を表現せず、
 *   Fresh / Stale / Miss として構造化して扱う。
 *
 *   これにより、呼び出し側はキャッシュ状態に応じて
 *   表示・再取得・フォールバックなどの処理を明確に分岐できる。
 *
 * @param T キャッシュに保存される値の型。
 */
sealed interface CacheResult<out T> {

    /**
     * 有効なキャッシュ値。
     *
     * キャッシュが存在し、期限切れではない場合に返す。
     *
     * @property value 取得したキャッシュ値。
     */
    data class Fresh<T>(
        val value: T
    ) : CacheResult<T>

    /**
     * 期限切れまたは古い可能性があるキャッシュ値。
     *
     * キャッシュ値は存在するが、
     * 有効期限切れなどにより最新とは扱えない場合に返す。
     *
     * @property value 取得した古いキャッシュ値。
     */
    data class Stale<T>(
        val value: T
    ) : CacheResult<T>

    /**
     * キャッシュ値を取得できなかった状態。
     *
     * 値が存在しない、期限切れにより利用できない、
     * 明示的に無効化されたなどの理由を保持する。
     *
     * @property reason キャッシュを利用できなかった理由。
     */
    data class Miss(
        val reason: CacheMissReason
    ) : CacheResult<Nothing>
}

/**
 * キャッシュを利用できなかった理由。
 *
 * 本 enum は、CacheResult.Miss の理由を表す。
 * 呼び出し側は理由に応じて、通信取得・再読込・エラー表示などを判断する。
 */
enum class CacheMissReason {

    /**
     * 指定されたキーに対応するキャッシュが存在しない。
     */
    NotFound,

    /**
     * キャッシュは存在するが、有効期限切れにより利用できない。
     */
    Expired,

    /**
     * キャッシュが明示的に無効化されている。
     */
    Invalidated
}