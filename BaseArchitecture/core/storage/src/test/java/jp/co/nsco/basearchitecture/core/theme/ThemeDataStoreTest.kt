package jp.co.nsco.basearchitecture.core.theme

import jp.co.nsco.basearchitecture.core.datastore.PreferenceDataStore
import jp.co.nsco.basearchitecture.core.result.AppResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ThemeDataStoreTest {

    @Test
    fun themeSettings_returnsDefaultValuesWhenPreferencesAreEmpty() = runTest {
        val dataStore = ThemeDataStore(FakePreferenceDataStore())

        val settings = dataStore.themeSettings.first()

        assertEquals(AppThemeMode.System, settings.themeMode)
        assertEquals(AppThemeId.Default, settings.themeId)
    }

    @Test
    fun updateThemeMode_savesSelectedMode() = runTest {
        val preferences = FakePreferenceDataStore()
        val dataStore = ThemeDataStore(preferences)

        val result = dataStore.updateThemeMode(AppThemeMode.Dark)
        val settings = dataStore.themeSettings.first()

        assertTrue(result is AppResult.Success)
        assertEquals(AppThemeMode.Dark, settings.themeMode)
    }

    @Test
    fun updateThemeId_savesSelectedThemeId() = runTest {
        val preferences = FakePreferenceDataStore()
        val dataStore = ThemeDataStore(preferences)

        val result = dataStore.updateThemeId(AppThemeId.Dashboard)
        val settings = dataStore.themeSettings.first()

        assertTrue(result is AppResult.Success)
        assertEquals(AppThemeId.Dashboard, settings.themeId)
    }

    @Test
    fun themeSettings_fallsBackWhenStoredValuesAreInvalid() = runTest {
        val preferences = FakePreferenceDataStore(
            strings = mapOf(
                ThemePreferenceKeys.ThemeMode to "Unknown",
                ThemePreferenceKeys.ThemeId to ""
            )
        )
        val dataStore = ThemeDataStore(preferences)

        val settings = dataStore.themeSettings.first()

        assertEquals(AppThemeMode.System, settings.themeMode)
        assertEquals(AppThemeId.Default, settings.themeId)
    }

    @Test
    fun updateThemeMode_returnsFailureWhenPreferenceWriteFails() = runTest {
        val dataStore = ThemeDataStore(
            FakePreferenceDataStore(throwOnWrite = true)
        )

        val result = dataStore.updateThemeMode(AppThemeMode.Light)

        assertTrue(result is AppResult.Failure)
    }

    private class FakePreferenceDataStore(
        strings: Map<String, String> = emptyMap(),
        private val throwOnWrite: Boolean = false
    ) : PreferenceDataStore {
        private val booleanValues = MutableStateFlow<Map<String, Boolean>>(emptyMap())
        private val stringValues = MutableStateFlow(strings)

        override fun observeBoolean(key: String, defaultValue: Boolean): Flow<Boolean> {
            return booleanValues.map { values -> values[key] ?: defaultValue }
        }

        override fun observeString(key: String, defaultValue: String): Flow<String> {
            return stringValues.map { values -> values[key] ?: defaultValue }
        }

        override fun observeInt(key: String, defaultValue: Int): Flow<Int> {
            return MutableStateFlow(defaultValue)
        }

        override fun observeLong(key: String, defaultValue: Long): Flow<Long> {
            return MutableStateFlow(defaultValue)
        }

        override fun observeFloat(key: String, defaultValue: Float): Flow<Float> {
            return MutableStateFlow(defaultValue)
        }

        override fun observeStringSet(
            key: String,
            defaultValue: Set<String>
        ): Flow<Set<String>> {
            return MutableStateFlow(defaultValue)
        }

        override suspend fun putBoolean(key: String, value: Boolean) {
            if (throwOnWrite) {
                error("write failed")
            }
            booleanValues.value = booleanValues.value + (key to value)
        }

        override suspend fun putString(key: String, value: String) {
            if (throwOnWrite) {
                error("write failed")
            }
            stringValues.value = stringValues.value + (key to value)
        }

        override suspend fun putInt(key: String, value: Int) = checkWritable()

        override suspend fun putLong(key: String, value: Long) = checkWritable()

        override suspend fun putFloat(key: String, value: Float) = checkWritable()

        override suspend fun putStringSet(key: String, value: Set<String>) = checkWritable()

        override suspend fun remove(key: String) = checkWritable()

        override suspend fun clear() = checkWritable()

        private fun checkWritable() {
            if (throwOnWrite) {
                error("write failed")
            }
        }
    }
}






