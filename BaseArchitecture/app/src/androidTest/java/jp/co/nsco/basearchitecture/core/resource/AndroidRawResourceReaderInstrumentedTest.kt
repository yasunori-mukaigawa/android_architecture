package jp.co.nsco.basearchitecture.core.resource

import androidx.test.platform.app.InstrumentationRegistry
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.result.AppResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * AndroidRawResourceReader Instrumented Test縲・ *
 * 譛ｬ繝・せ繝医・縲、ndroid縺ｮResources繧貞ｿ・ｦ√→縺吶ｋraw resource隱ｭ縺ｿ霎ｼ縺ｿ繧堤｢ｺ隱阪☆繧九・ * 騾壼ｸｸ縺ｮunit test縺ｧ縺ｯContext縺後↑縺・◆繧√（nstrumentation test縺ｨ縺励※驟咲ｽｮ縺吶ｋ縲・ */
class AndroidRawResourceReaderInstrumentedTest {

    @Test
    fun readText_existing_raw_resource_returns_success() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val reader = AndroidRawResourceReader(context)

        val actual = reader.readText(R.raw.privacy_policy)

        assertTrue(actual is AppResult.Success)
        assertTrue((actual as AppResult.Success).value.contains("# 繝励Λ繧､繝舌す繝ｼ繝昴Μ繧ｷ繝ｼ"))
    }

    @Test
    fun readText_invalid_raw_resource_returns_failure() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val reader = AndroidRawResourceReader(context)

        val actual = reader.readText(0)

        assertTrue(actual is AppResult.Failure)
    }
}


