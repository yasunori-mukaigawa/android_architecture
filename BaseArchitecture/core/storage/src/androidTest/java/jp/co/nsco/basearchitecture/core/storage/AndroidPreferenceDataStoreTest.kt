package jp.co.nsco.basearchitecture.core.storage

import android.content.Context
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.co.nsco.basearchitecture.core.datastore.AndroidPreferenceDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * AndroidX DataStore Preferences 実装の型別読み書きと削除を確認する。
 */
@RunWith(AndroidJUnit4::class)
class AndroidPreferenceDataStoreTest {

    @Test
    fun preferenceDataStore_readsWritesRemovesAndClearsAllSupportedTypes() = runBlocking {
        val context: Context = ApplicationProvider.getApplicationContext()
        val dataStore = PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("core-preferences-${System.nanoTime()}.preferences_pb")
        }
        val preferences = AndroidPreferenceDataStore(dataStore)

        assertEquals(false, preferences.observeBoolean("boolean", false).first())
        assertEquals("default", preferences.observeString("string", "default").first())
        assertEquals(1, preferences.observeInt("int", 1).first())
        assertEquals(2L, preferences.observeLong("long", 2L).first())
        assertEquals(3.0f, preferences.observeFloat("float", 3.0f).first())
        assertEquals(setOf("default"), preferences.observeStringSet("set", setOf("default")).first())

        preferences.putBoolean("boolean", true)
        preferences.putString("string", "value")
        preferences.putInt("int", 10)
        preferences.putLong("long", 20L)
        preferences.putFloat("float", 30.0f)
        preferences.putStringSet("set", setOf("a", "b"))

        assertEquals(true, preferences.observeBoolean("boolean", false).first())
        assertEquals("value", preferences.observeString("string", "default").first())
        assertEquals(10, preferences.observeInt("int", 1).first())
        assertEquals(20L, preferences.observeLong("long", 2L).first())
        assertEquals(30.0f, preferences.observeFloat("float", 3.0f).first())
        assertEquals(setOf("a", "b"), preferences.observeStringSet("set", emptySet()).first())

        preferences.remove("string")
        assertEquals("default", preferences.observeString("string", "default").first())

        preferences.clear()
        assertEquals(false, preferences.observeBoolean("boolean", false).first())
        assertEquals(1, preferences.observeInt("int", 1).first())
    }
}
