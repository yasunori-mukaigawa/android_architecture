package jp.co.nsco.basearchitecture.core.license

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * OSS ライセンス情報取得の標準実装を DI コンテナへ登録する Hilt Module。
 *
 * License 機能を利用するアプリだけが本 Module を含む license module を
 * 依存することで、OSS License Plugin を使わないアプリへ不要な契約を強制しない。
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class LicenseModule {

    /**
     * Android のリソースからライセンス情報を取得する実装を登録する。
     */
    @Binds
    @Singleton
    abstract fun bindLicenseProvider(
        impl: AndroidLicenseProvider
    ): LicenseProvider
}
