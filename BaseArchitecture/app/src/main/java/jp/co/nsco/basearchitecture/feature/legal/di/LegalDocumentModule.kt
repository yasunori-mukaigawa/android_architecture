package jp.co.nsco.basearchitecture.feature.legal.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import jp.co.nsco.basearchitecture.feature.legal.data.RawLegalDocumentRepository
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocumentRepository

/**
 * Legal Feature の依存関係を登録する Hilt Module。
 *
 * 本 Module は、法務文書取得に関する Repository 契約と
 * その実装を DI コンテナへ登録する責務を持つ。
 *
 * ■ 提供する責務
 *   LegalDocumentRepository 実装の登録
 *   Domain 層の Repository 契約と Data 層実装の紐付け
 *
 * ■ 設計上の意図
 *   UseCase / ViewModel は LegalDocumentRepository 契約に依存し、
 *   具体的な取得方法である RawLegalDocumentRepository には直接依存しない。
 *
 *   本 Module で契約と実装を紐付けることで、
 *   法務文書の取得元を raw resource から asset / DB / API などへ変更する場合でも、
 *   呼び出し側への影響を抑えられる。
 *
 * ■ 注意
 *   本 Module は Legal Feature の DI 境界である。
 *   Feature 外の共通依存、たとえば RawResourceReader や StringProvider の登録は
 *   Core 側の DI Module で行う。
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class LegalDocumentModule {

    /**
     * LegalDocumentRepository の実装として RawLegalDocumentRepository を登録する。
     *
     * RawLegalDocumentRepository は raw resource から Markdown 文書を読み取り、
     * LegalDocumentRepository 契約を満たす。
     *
     * @param impl raw resource を利用する LegalDocumentRepository 実装。
     * @return LegalDocumentRepository 契約。
     */
    @Binds
    @Singleton
    abstract fun bindLegalDocumentRepository(
        impl: RawLegalDocumentRepository
    ): LegalDocumentRepository
}