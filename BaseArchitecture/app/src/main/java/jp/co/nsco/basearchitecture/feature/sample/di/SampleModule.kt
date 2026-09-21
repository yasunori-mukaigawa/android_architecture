package jp.co.nsco.basearchitecture.feature.sample.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import jp.co.nsco.basearchitecture.feature.sample.data.SampleSettingsRepositoryImpl
import jp.co.nsco.basearchitecture.feature.sample.domain.SampleSettingsRepository

/**
 * Sample Feature の依存関係を登録する Hilt Module。
 *
 * 本 Module は、Sample Feature で使用する Repository 契約に対して、
 * 実装クラスを紐付ける。
 *
 * ■ 提供する責務
 *   SampleSettingsRepository 実装の登録
 *
 * ■ 設計上の意図
 *   Application 層は SampleSettingsRepository 契約に依存し、
 *   DataStore などの具体的な保存方式を知らない。
 *
 *   実際にどの Repository 実装を利用するかは DI Module で決定する。
 *
 *   これにより、保存方式の変更やテスト用実装への差し替えを
 *   UseCase / ViewModel へ波及させずに行える。
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class SampleModule {

    /**
     * SampleSettingsRepository の実装として SampleSettingsRepositoryImpl を登録する。
     *
     * @param impl Sample設定Repository実装。
     * @return SampleSettingsRepository契約。
     */
    @Binds
    @Singleton
    abstract fun bindSampleSettingsRepository(
        impl: SampleSettingsRepositoryImpl
    ): SampleSettingsRepository
}