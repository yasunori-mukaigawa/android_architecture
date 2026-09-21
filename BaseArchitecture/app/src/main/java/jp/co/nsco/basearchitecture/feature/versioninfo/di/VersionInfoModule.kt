package jp.co.nsco.basearchitecture.feature.versioninfo.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import jp.co.nsco.basearchitecture.feature.versioninfo.data.FakeLatestVersionRepository
import jp.co.nsco.basearchitecture.feature.versioninfo.data.LatestVersionApi
import jp.co.nsco.basearchitecture.feature.versioninfo.data.SampleAppExternalLinksProvider
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.AppExternalLinksProvider
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.LatestVersionRepository
import retrofit2.Retrofit

/**
 * VersionInfo Feature の DI 定義。
 *
 * VersionInfoModule は、VersionInfo Feature で利用する
 * Repository / Provider / API の依存関係を Hilt に登録する。
 *
 * ■ 提供する責務
 *   LatestVersionRepository 実装の binding
 *   AppExternalLinksProvider 実装の binding
 *   LatestVersionApi の生成
 *
 * ■ 設計上の意図
 *   Application 層や Presentation 層は、Repository / Provider の契約に依存し、
 *   具体的な実装選択は DI Module に閉じ込める。
 *
 *   これにより、Fake 実装、Retrofit 実装、別API実装などを差し替える場合でも、
 *   UseCase や ViewModel へ影響を出さずに構成を変更できる。
 *
 * ■ 注意
 *   現在は LatestVersionRepository として FakeLatestVersionRepository を bind している。
 *   そのため、provideLatestVersionApi で生成している LatestVersionApi は、
 *   RetrofitLatestVersionRepository を bind する構成に切り替えた場合に利用される。
 *
 *   サンプルでは API 未接続でも画面確認できるよう Fake 実装を利用し、
 *   Retrofit 実装へ差し替え可能な構成だけを示している。
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class VersionInfoModule {

    /**
     * 最新バージョン情報 Repository の実装を登録する。
     *
     * 現在はサンプル表示用として FakeLatestVersionRepository を利用する。
     *
     * @param impl 固定値で最新バージョン情報を返す Repository 実装。
     * @return LatestVersionRepository 契約。
     */
    @Binds
    @Singleton
    abstract fun bindLatestVersionRepository(
        impl: FakeLatestVersionRepository
    ): LatestVersionRepository

    /**
     * 外部リンク Provider の実装を登録する。
     *
     * @param impl サンプルアプリ向けの外部リンク Provider 実装。
     * @return AppExternalLinksProvider 契約。
     */
    @Binds
    @Singleton
    abstract fun bindAppExternalLinksProvider(
        impl: SampleAppExternalLinksProvider
    ): AppExternalLinksProvider

    companion object {

        /**
         * 最新バージョン情報取得 API を生成する。
         *
         * Retrofit から LatestVersionApi の実装を生成する。
         *
         * @param retrofit アプリ共通の Retrofit インスタンス。
         * @return 最新バージョン情報取得 API。
         */
        @Provides
        @Singleton
        fun provideLatestVersionApi(retrofit: Retrofit): LatestVersionApi {
            return retrofit.create(LatestVersionApi::class.java)
        }
    }
}