package jp.co.nsco.basearchitecture.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * AndroidX DataStore Preferences を利用した PreferenceDataStore 実装。
 *
 * 本クラスは、AndroidX DataStore Preferences API を使用して、
 * アプリ内の軽量な設定値を永続化する責務を持つ。
 *
 * ■ 提供する責務
 *   DataStore Preferences への値保存
 *   DataStore Preferences からの値購読
 *   Preferences.Key 生成処理の隠蔽
 *   未保存時の既定値返却
 *   指定キー削除
 *   全設定値削除
 *
 * ■ 設計上の意図
 *   呼び出し側が AndroidX DataStore の Preferences / Preferences.Key を
 *   直接扱わないようにする。
 *
 *   Repository や UseCase は PreferenceDataStore の契約のみを参照し、
 *   保存方式や Preferences Key の生成方法を知らない状態にする。
 *
 *   DataStore の生成責務は本クラスでは持たない。
 *   ファイル名や生成スコープは App 側の DI Module で決定し、
 *   本クラスは注入された DataStore を利用するだけにする。
 *
 * @param dataStore アプリ側で生成された DataStore Preferences。
 */
@Singleton
class AndroidPreferenceDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferenceDataStore {

    /**
     * Boolean 設定値を購読する。
     *
     * 指定された key に対応する値が存在しない場合は defaultValue を流す。
     * DataStore の値が更新されると、返却した Flow に新しい値が流れる。
     *
     * @param key 設定値を識別するキー。
     * @param defaultValue 値が未保存の場合に使用する既定値。
     * @return Boolean 設定値を流す Flow。
     */
    override fun observeBoolean(
        key: String,
        defaultValue: Boolean
    ): Flow<Boolean> {
        val preferenceKey = booleanPreferencesKey(key)

        return dataStore.data.map { preferences ->
            preferences[preferenceKey] ?: defaultValue
        }
    }

    /**
     * String 設定値を購読する。
     *
     * @param key 設定値を識別するキー。
     * @param defaultValue 値が未保存の場合に使用する既定値。
     * @return String 設定値を流す Flow。
     */
    override fun observeString(
        key: String,
        defaultValue: String
    ): Flow<String> {
        val preferenceKey = stringPreferencesKey(key)

        return dataStore.data.map { preferences ->
            preferences[preferenceKey] ?: defaultValue
        }
    }

    /**
     * Int 設定値を購読する。
     *
     * @param key 設定値を識別するキー。
     * @param defaultValue 値が未保存の場合に使用する既定値。
     * @return Int 設定値を流す Flow。
     */
    override fun observeInt(
        key: String,
        defaultValue: Int
    ): Flow<Int> {
        val preferenceKey = intPreferencesKey(key)

        return dataStore.data.map { preferences ->
            preferences[preferenceKey] ?: defaultValue
        }
    }

    /**
     * Long 設定値を購読する。
     *
     * @param key 設定値を識別するキー。
     * @param defaultValue 値が未保存の場合に使用する既定値。
     * @return Long 設定値を流す Flow。
     */
    override fun observeLong(
        key: String,
        defaultValue: Long
    ): Flow<Long> {
        val preferenceKey = longPreferencesKey(key)

        return dataStore.data.map { preferences ->
            preferences[preferenceKey] ?: defaultValue
        }
    }

    /**
     * Float 設定値を購読する。
     *
     * @param key 設定値を識別するキー。
     * @param defaultValue 値が未保存の場合に使用する既定値。
     * @return Float 設定値を流す Flow。
     */
    override fun observeFloat(
        key: String,
        defaultValue: Float
    ): Flow<Float> {
        val preferenceKey = floatPreferencesKey(key)

        return dataStore.data.map { preferences ->
            preferences[preferenceKey] ?: defaultValue
        }
    }

    /**
     * String Set 設定値を購読する。
     *
     * @param key 設定値を識別するキー。
     * @param defaultValue 値が未保存の場合に使用する既定値。
     * @return String Set 設定値を流す Flow。
     */
    override fun observeStringSet(
        key: String,
        defaultValue: Set<String>
    ): Flow<Set<String>> {
        val preferenceKey = stringSetPreferencesKey(key)

        return dataStore.data.map { preferences ->
            preferences[preferenceKey] ?: defaultValue
        }
    }

    /**
     * Boolean 設定値を保存する。
     *
     * @param key 設定値を識別するキー。
     * @param value 保存する Boolean 値。
     */
    override suspend fun putBoolean(
        key: String,
        value: Boolean
    ) {
        val preferenceKey = booleanPreferencesKey(key)

        dataStore.edit { preferences ->
            preferences[preferenceKey] = value
        }
    }

    /**
     * String 設定値を保存する。
     *
     * @param key 設定値を識別するキー。
     * @param value 保存する String 値。
     */
    override suspend fun putString(
        key: String,
        value: String
    ) {
        val preferenceKey = stringPreferencesKey(key)

        dataStore.edit { preferences ->
            preferences[preferenceKey] = value
        }
    }

    /**
     * Int 設定値を保存する。
     *
     * @param key 設定値を識別するキー。
     * @param value 保存する Int 値。
     */
    override suspend fun putInt(
        key: String,
        value: Int
    ) {
        val preferenceKey = intPreferencesKey(key)

        dataStore.edit { preferences ->
            preferences[preferenceKey] = value
        }
    }

    /**
     * Long 設定値を保存する。
     *
     * @param key 設定値を識別するキー。
     * @param value 保存する Long 値。
     */
    override suspend fun putLong(
        key: String,
        value: Long
    ) {
        val preferenceKey = longPreferencesKey(key)

        dataStore.edit { preferences ->
            preferences[preferenceKey] = value
        }
    }

    /**
     * Float 設定値を保存する。
     *
     * @param key 設定値を識別するキー。
     * @param value 保存する Float 値。
     */
    override suspend fun putFloat(
        key: String,
        value: Float
    ) {
        val preferenceKey = floatPreferencesKey(key)

        dataStore.edit { preferences ->
            preferences[preferenceKey] = value
        }
    }

    /**
     * String Set 設定値を保存する。
     *
     * DataStore Preferences の StringSet は Set として保存されるため、
     * 呼び出し側で順序に意味を持たせないこと。
     *
     * @param key 設定値を識別するキー。
     * @param value 保存する String Set 値。
     */
    override suspend fun putStringSet(
        key: String,
        value: Set<String>
    ) {
        val preferenceKey = stringSetPreferencesKey(key)

        dataStore.edit { preferences ->
            preferences[preferenceKey] = value
        }
    }

    /**
     * 指定されたキーの設定値を削除する。
     *
     * DataStore Preferences のキーは型付きであるため、
     * 削除時は現在保存されているキー一覧から同名キーを探して削除する。
     *
     * @param key 削除対象の設定キー。
     */
    override suspend fun remove(key: String) {
        dataStore.edit { preferences ->
            preferences.removeByName(key)
        }
    }

    /**
     * 保存されている設定値をすべて削除する。
     */
    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    /**
     * 名前が一致する Preference Key を削除する。
     *
     * Preferences.Key は型情報を持つため、呼び出し側から String のみを受け取る場合、
     * booleanPreferencesKey / stringPreferencesKey などの型付きキーを一意に決められない。
     *
     * そのため、現在保存されているキー一覧から name が一致するキーを探して削除する。
     *
     * @param key 削除対象の設定キー名。
     */
    private fun MutablePreferences.removeByName(key: String) {
        val targetKey = asMap().keys.firstOrNull { it.name == key } ?: return
        remove(targetKey)
    }
}