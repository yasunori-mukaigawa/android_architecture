package jp.co.nsco.basearchitecture.feature.legal.data

import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocumentType
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * LegalDocumentRawResourceMapperTest。
 *
 * この状態または処理は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
class LegalDocumentRawResourceMapperTest {

    @Test
    fun toRawRes_privacyPolicy_returns_privacy_policy_resource() {
        val mapper = LegalDocumentRawResourceMapper()

        val actual = mapper.toRawRes(LegalDocumentType.PrivacyPolicy)

        assertEquals(R.raw.privacy_policy, actual)
    }

    @Test
    fun toRawRes_termsOfService_returns_terms_of_service_resource() {
        val mapper = LegalDocumentRawResourceMapper()

        val actual = mapper.toRawRes(LegalDocumentType.TermsOfService)

        assertEquals(R.raw.terms_of_service, actual)
    }
}






