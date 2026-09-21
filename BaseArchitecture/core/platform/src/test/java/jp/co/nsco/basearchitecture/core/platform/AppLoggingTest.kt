package jp.co.nsco.basearchitecture.core.platform

import jp.co.nsco.basearchitecture.core.logging.AndroidAppLogger
import jp.co.nsco.basearchitecture.core.logging.DefaultAppLoggingConfiguration
import jp.co.nsco.basearchitecture.core.logging.TimberAppLoggingInitializer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import timber.log.Timber

/**
 * Timber を利用したログ初期化処理を確認する。
 */
class AppLoggingTest {

    @After
    fun tearDown() {
        Timber.uprootAll()
    }

    @Test
    fun initialize_debug_registersOneTree() {
        val configuration = DefaultAppLoggingConfiguration()
        val initializer = TimberAppLoggingInitializer(configuration)

        initializer.initialize(isDebug = true, tag = "TestApp")
        initializer.initialize(isDebug = true, tag = "IgnoredAfterRegistration")

        assertEquals(1, Timber.treeCount)
        assertEquals("IgnoredAfterRegistration", configuration.defaultTag)
    }

    @Test
    fun initialize_release_doesNotRegisterTree() {
        val initializer = TimberAppLoggingInitializer(DefaultAppLoggingConfiguration())

        initializer.initialize(isDebug = false)

        assertEquals(0, Timber.treeCount)
    }

    @Test
    fun androidAppLogger_usesDefaultTagAndPerCallTag() {
        val tree = RecordingTree()
        val configuration = DefaultAppLoggingConfiguration()
        val logger = AndroidAppLogger(configuration)
        Timber.plant(tree)

        logger.info("default")
        assertEquals("BaseArchitecture", tree.lastTag)

        logger.info("feature", tag = "SampleFeature")
        assertEquals("SampleFeature", tree.lastTag)
    }

    private class RecordingTree : Timber.Tree() {

        var lastTag: String? = null

        override fun log(
            priority: Int,
            tag: String?,
            message: String,
            t: Throwable?
        ) {
            lastTag = tag
        }
    }
}
