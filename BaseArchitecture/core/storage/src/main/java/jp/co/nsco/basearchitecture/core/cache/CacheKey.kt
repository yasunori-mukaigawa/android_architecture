package jp.co.nsco.basearchitecture.core.cache

/**
 * キャッシュデータを識別するキー。
 *
 * 本クラスは、キャッシュに保存する値の識別子を表す。
 * String をそのまま扱わず CacheKey として型付けすることで、
 * 他の文字列値との取り違えを防ぐ。
 *
 * ■ 設計上の意図
 *   キャッシュキーは単なる文字列ではなく、
 *   保存・取得対象を識別するための意味を持つ値として扱う。
 *
 * @property value キャッシュ対象を一意に識別する文字列。
 */
@JvmInline
value class CacheKey(
    val value: String
)