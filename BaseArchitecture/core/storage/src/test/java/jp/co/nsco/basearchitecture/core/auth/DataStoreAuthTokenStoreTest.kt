package jp.co.nsco.basearchitecture.core.auth

import jp.co.nsco.basearchitecture.core.datastore.PreferenceDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * DataStoreAuthTokenStore の保存・購読・削除契約を確認する。
 */
class DataStoreAuthTokenStoreTest {

    @Test
    fun tokenStore_savesObservesAndClearsToken() = runTest {
        val preferences = FakePreferenceDataStore()
        val store = DataStoreAuthTokenStore(preferences)

        assertNull(store.getToken())
        store.saveToken("token")
        assertEquals("token", store.getToken())
        assertEquals("token", store.observeToken().first())

        store.clearToken()

        assertNull(store.getToken())
        assertNull(store.observeToken().first())
    }

    @Test
    fun tokenStore_treatsBlankTokenAsNull() = runTest {
        val preferences = FakePreferenceDataStore()
        val store = DataStoreAuthTokenStore(preferences)

        store.saveToken("  ")

        assertNull(store.getToken())
        assertNull(store.observeToken().first())
    }

    private class FakePreferenceDataStore : PreferenceDataStore {
        private val strings = MutableStateFlow<Map<String, String>>(emptyMap())

        override fun observeBoolean(key: String, defaultValue: Boolean): Flow<Boolean> =
            MutableStateFlow(defaultValue)

        override fun observeString(key: String, defaultValue: String): Flow<String> =
            strings.map { values -> values[key] ?: defaultValue }

        override fun observeInt(key: String, defaultValue: Int): Flow<Int> =
            MutableStateFlow(defaultValue)

        override fun observeLong(key: String, defaultValue: Long): Flow<Long> =
            MutableStateFlow(defaultValue)

        override fun observeFloat(key: String, defaultValue: Float): Flow<Float> =
            MutableStateFlow(defaultValue)

        override fun observeStringSet(key: String, defaultValue: Set<String>): Flow<Set<String>> =
            MutableStateFlow(defaultValue)

        override suspend fun putBoolean(key: String, value: Boolean) = Unit

        override suspend fun putString(key: String, value: String) {
            strings.value = strings.value + (key to value)
        }

        override suspend fun putInt(key: String, value: Int) = Unit

        override suspend fun putLong(key: String, value: Long) = Unit

        override suspend fun putFloat(key: String, value: Float) = Unit

        override suspend fun putStringSet(key: String, value: Set<String>) = Unit

        override suspend fun remove(key: String) {
            strings.value = strings.value - key
        }

        override suspend fun clear() {
            strings.value = emptyMap()
        }
    }
}
