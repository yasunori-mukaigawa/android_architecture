package jp.co.nsco.basearchitecture.feature.versioninfo.data

import org.junit.Assert.assertTrue
import org.junit.Test

class SampleAppExternalLinksProviderTest {
    @Test
    fun getLinks_returns_sample_store_and_google_search_urls() {
        val actual = SampleAppExternalLinksProvider().getLinks("jp.co.nsco.basearchitecture")

        assertTrue(actual.storeUri.primaryUri.startsWith("market://"))
        assertTrue(actual.storeUri.fallbackUri?.contains("play.google.com") == true)
        assertTrue(actual.appInfoPageUri.primaryUri.startsWith("package:"))
    }
}






