package jp.co.nsco.basearchitecture.core.theme

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * テーマ関連の依存を DI コンテナへ登録する Hilt Module。
 *
 * 本 Module は、テーマ設定の保存・取得契約と、
 * ThemeDataStore の標準実装を登録する責務を持つ。
 *
 * ■ 提供する責務
 *   ThemeRepository 実装の登録
 *
 * ■ 設計上の意図
 *   AppRoot や設定画面は ThemeDataStore へ直接依存せず、
 *   ThemeRepository の契約に依存する。
 *
 *   これにより、テーマ保存方式を差し替えやすくする。
 *
 * ■ 注意
 *   ThemeRepository は PreferenceDataStore に依存するため、
 *   PreferenceDataStore の DI 登録が先に成立している必要がある。
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ThemeModule {

    /**
     * ThemeRepository の実装として ThemeDataStore を登録する。
     *
     * @param impl PreferenceDataStore を利用するテーマ設定保存実装。
     * @return ThemeRepository 契約として公開する実装。
     */
    @Binds
    @Singleton
    abstract fun bindThemeRepository(
        impl: ThemeDataStore
    ): ThemeRepository

}
