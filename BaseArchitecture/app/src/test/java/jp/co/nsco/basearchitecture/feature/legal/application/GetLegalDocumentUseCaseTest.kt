package jp.co.nsco.basearchitecture.feature.legal.application

import jp.co.nsco.basearchitecture.core.error.AppError
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocument
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocumentRepository
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocumentType
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

/**
 * GetLegalDocumentUseCaseTest。
 *
 * この状態または処理は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
class GetLegalDocumentUseCaseTest {

    @Test
    fun invoke_success_returns_document() = runTest {
        val document = LegalDocument(
            type = LegalDocumentType.PrivacyPolicy,
            title = "プライバシーポリシー",
            markdown = "# プライバシーポリシー"
        )
        val useCase = GetLegalDocumentUseCase(
            FakeLegalDocumentRepository(AppResult.Success(document))
        )

        val actual = useCase(LegalDocumentType.PrivacyPolicy)

        assertEquals(AppResult.Success(document), actual)
    }

    @Test
    fun invoke_failure_returns_failure() = runTest {
        val error = AppError.LocalStorage(code = "TEST_READ_FAILED")
        val useCase = GetLegalDocumentUseCase(
            FakeLegalDocumentRepository(AppResult.Failure(error))
        )

        val actual = useCase(LegalDocumentType.TermsOfService)

        assertSame(error, (actual as AppResult.Failure).error)
    }

    private class FakeLegalDocumentRepository(
        private val result: AppResult<LegalDocument>
    ) : LegalDocumentRepository {

        override suspend fun getDocument(
            type: LegalDocumentType
        ): AppResult<LegalDocument> {
            return result
        }
    }
}







