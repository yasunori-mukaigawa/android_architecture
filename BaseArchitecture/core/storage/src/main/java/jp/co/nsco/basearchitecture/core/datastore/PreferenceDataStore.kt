package jp.co.nsco.basearchitecture.core.datastore

import kotlinx.coroutines.flow.Flow

/**
 * 軽量な設定値を永続化するための契約。
 *
 * 本インターフェースは、テーマ設定、表示設定、ユーザー選択状態など、
 * アプリ内で保持したい小さな Key-Value 形式の設定値を扱う。
 *
 * ■ 提供する責務
 *   Boolean / String / Int / Long / Float / StringSet の購読
 *   Boolean / String / Int / Long / Float / StringSet の保存
 *   指定キーの削除
 *   全設定値の削除
 *   保存方式の隠蔽
 *   AndroidX DataStore 依存の隠蔽
 *
 * ■ 設計上の意図
 *   呼び出し側は DataStore / SharedPreferences / ファイルなどの
 *   具体的な保存方式を知らず、PreferenceDataStore の契約だけを利用する。
 *
 *   値の取得は suspend の単発取得ではなく Flow として公開する。
 *   これにより、設定値の変更を画面状態へ自然に反映できる。
 *
 * ■ 注意
 *   本契約は軽量な設定値を対象とする。
 *   一覧データ、履歴、大きな構造化データなどは Room や専用 Store を使用する。
 */
interface PreferenceDataStore {

    /**
     * Boolean 設定値を購読する。
     *
     * @param key 設定値を識別するキー。
     * @param defaultValue 値が未保存の場合に使用する既定値。
     * @return Boolean 設定値を流す Flow。
     */
    fun observeBoolean(
        key: String,
        defaultValue: Boolean
    ): Flow<Boolean>

    /**
     * String 設定値を購読する。
     *
     * @param key 設定値を識別するキー。
     * @param defaultValue 値が未保存の場合に使用する既定値。
     * @return String 設定値を流す Flow。
     */
    fun observeString(
        key: String,
        defaultValue: String
    ): Flow<String>

    /**
     * Int 設定値を購読する。
     *
     * @param key 設定値を識別するキー。
     * @param defaultValue 値が未保存の場合に使用する既定値。
     * @return Int 設定値を流す Flow。
     */
    fun observeInt(
        key: String,
        defaultValue: Int
    ): Flow<Int>

    /**
     * Long 設定値を購読する。
     *
     * @param key 設定値を識別するキー。
     * @param defaultValue 値が未保存の場合に使用する既定値。
     * @return Long 設定値を流す Flow。
     */
    fun observeLong(
        key: String,
        defaultValue: Long
    ): Flow<Long>

    /**
     * Float 設定値を購読する。
     *
     * @param key 設定値を識別するキー。
     * @param defaultValue 値が未保存の場合に使用する既定値。
     * @return Float 設定値を流す Flow。
     */
    fun observeFloat(
        key: String,
        defaultValue: Float
    ): Flow<Float>

    /**
     * String Set 設定値を購読する。
     *
     * @param key 設定値を識別するキー。
     * @param defaultValue 値が未保存の場合に使用する既定値。
     * @return String Set 設定値を流す Flow。
     */
    fun observeStringSet(
        key: String,
        defaultValue: Set<String>
    ): Flow<Set<String>>

    /**
     * Boolean 設定値を保存する。
     *
     * @param key 設定値を識別するキー。
     * @param value 保存する Boolean 値。
     */
    suspend fun putBoolean(
        key: String,
        value: Boolean
    )

    /**
     * String 設定値を保存する。
     *
     * @param key 設定値を識別するキー。
     * @param value 保存する String 値。
     */
    suspend fun putString(
        key: String,
        value: String
    )

    /**
     * Int 設定値を保存する。
     *
     * @param key 設定値を識別するキー。
     * @param value 保存する Int 値。
     */
    suspend fun putInt(
        key: String,
        value: Int
    )

    /**
     * Long 設定値を保存する。
     *
     * @param key 設定値を識別するキー。
     * @param value 保存する Long 値。
     */
    suspend fun putLong(
        key: String,
        value: Long
    )

    /**
     * Float 設定値を保存する。
     *
     * @param key 設定値を識別するキー。
     * @param value 保存する Float 値。
     */
    suspend fun putFloat(
        key: String,
        value: Float
    )

    /**
     * String Set 設定値を保存する。
     *
     * @param key 設定値を識別するキー。
     * @param value 保存する String Set 値。
     */
    suspend fun putStringSet(
        key: String,
        value: Set<String>
    )

    /**
     * 指定されたキーの設定値を削除する。
     *
     * @param key 削除対象の設定キー。
     */
    suspend fun remove(key: String)

    /**
     * 保存されている設定値をすべて削除する。
     *
     * ログアウト、初期化、テストデータ削除など、
     * 設定値全体を破棄したい場合に使用する。
     */
    suspend fun clear()
}