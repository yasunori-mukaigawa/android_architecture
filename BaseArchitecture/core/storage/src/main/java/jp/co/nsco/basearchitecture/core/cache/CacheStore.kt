package jp.co.nsco.basearchitecture.core.cache

/**
 * キャッシュ保存先を表す契約。
 *
 * 本インターフェースは、メモリ、DataStore、Room、ファイルなど、
 * 具体的な保存方式を呼び出し側から隠蔽するための境界である。
 *
 * ■ 提供する責務
 *   キャッシュ値の取得
 *   キャッシュ値の保存
 *   指定キーのキャッシュ削除
 *   全キャッシュ削除
 *
 * ■ 設計上の意図
 *   呼び出し側はキャッシュの保存方式を知らず、
 *   CacheStore の契約だけを通じて値を扱う。
 *
 *   保存方式を差し替える場合も、利用側のコードではなく
 *   CacheStore 実装のみを変更すればよい構造にする。
 *
 * @param K キャッシュキーの型。
 * @param V キャッシュ値の型。
 */
interface CacheStore<K, V> {

    /**
     * 指定されたキーに対応するキャッシュ値を取得する。
     *
     * 取得結果は null ではなく CacheResult として返す。
     * これにより、キャッシュが有効か、古いか、存在しないかを
     * 呼び出し側が明確に判断できる。
     *
     * @param key 取得対象のキャッシュキー。
     * @return キャッシュ取得結果。
     */
    suspend fun get(key: K): CacheResult<V>

    /**
     * 指定されたキーにキャッシュ値を保存する。
     *
     * 値とあわせて CacheMetadata を保存し、
     * 取得時の期限切れ判定や管理情報として使用する。
     *
     * @param key 保存対象のキャッシュキー。
     * @param value 保存するキャッシュ値。
     * @param metadata 保存時刻や有効期限を含むメタ情報。
     */
    suspend fun put(
        key: K,
        value: V,
        metadata: CacheMetadata
    )

    /**
     * 指定されたキーに対応するキャッシュを削除する。
     *
     * @param key 削除対象のキャッシュキー。
     */
    suspend fun remove(key: K)

    /**
     * 保存されているキャッシュをすべて削除する。
     *
     * ログアウト、設定初期化、強制再取得など、
     * キャッシュ全体を破棄したい場合に使用する。
     */
    suspend fun clear()
}