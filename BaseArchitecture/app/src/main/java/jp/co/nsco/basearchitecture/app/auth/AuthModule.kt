package jp.co.nsco.basearchitecture.app.auth

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import jp.co.nsco.basearchitecture.core.auth.AuthTokenProvider
import jp.co.nsco.basearchitecture.core.auth.AuthTokenStore
import jp.co.nsco.basearchitecture.core.auth.DataStoreAuthTokenStore

/**
 * 認証トークン関連の依存を提供する Hilt Module。
 *
 * 本 Module は、認証トークンの保存・取得契約と、
 * API Header 付与用の AuthTokenProvider を DI コンテナへ登録する。
 *
 * ■ 提供する責務
 *   AuthTokenStore 実装の登録
 *   AuthTokenProvider 実装の登録
 *
 * ■ 設計上の意図
 *   認証トークンの保存方式はアプリ構成に依存しやすいため、
 *   app 側 Module で実装を選択する。
 *
 *   現在は DataStoreAuthTokenStore を標準実装として使用し、
 *   AuthTokenStore と AuthTokenProvider の両方の契約に紐付ける。
 *
 * ■ 注意
 *   本 Module はトークンの保存先を登録するだけであり、
 *   ログイン処理、トークン更新、401 応答時の再認証は扱わない。
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    /**
     * AuthTokenStore の実装として DataStoreAuthTokenStore を登録する。
     *
     * @param impl PreferenceDataStore を利用する認証トークン保存実装。
     * @return AuthTokenStore 契約として公開する実装。
     */
    @Binds
    @Singleton
    abstract fun bindAuthTokenStore(
        impl: DataStoreAuthTokenStore
    ): AuthTokenStore

    /**
     * AuthTokenProvider の実装として DataStoreAuthTokenStore を登録する。
     *
     * AuthHeaderInterceptor は本契約を通じて認証トークンを取得する。
     *
     * @param impl PreferenceDataStore を利用する認証トークン保存実装。
     * @return AuthTokenProvider 契約として公開する実装。
     */
    @Binds
    @Singleton
    abstract fun bindAuthTokenProvider(
        impl: DataStoreAuthTokenStore
    ): AuthTokenProvider
}
