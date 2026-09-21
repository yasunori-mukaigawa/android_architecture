package jp.co.nsco.basearchitecture.core.ui.theme

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Compose UI テーマ関連の依存を DI コンテナへ登録する Hilt Module。
 *
 * 本 Module は、テーマの保存処理には依存せず、
 * Compose が利用するテーマ定義 Registry のみを登録する。
 *
 * ■ 提供する責務
 *   BaseThemeRegistry 実装の登録
 *
 * ■ 設計上の意図
 *   UI モジュールが DataStore / Room などの保存実装へ依存しないようにする。
 *   テーマ設定の保存は ThemeRepository と storage 側の Module が担当し、
 *   Compose の見た目解決は本 Module が担当する。
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ThemeUiModule {

    /**
     * BaseThemeRegistry の実装として DefaultBaseThemeRegistry を登録する。
     *
     * @param impl アプリ標準のテーマ定義 Registry。
     * @return BaseThemeRegistry 契約として公開する実装。
     */
    @Binds
    @Singleton
    abstract fun bindBaseThemeRegistry(
        impl: DefaultBaseThemeRegistry
    ): BaseThemeRegistry
}
