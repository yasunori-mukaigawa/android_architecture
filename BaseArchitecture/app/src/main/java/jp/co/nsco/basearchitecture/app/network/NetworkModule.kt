package jp.co.nsco.basearchitecture.app.network

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.nsco.basearchitecture.BuildConfig
import jp.co.nsco.basearchitecture.core.network.NetworkConfig
import jp.co.nsco.basearchitecture.core.network.interceptor.AuthHeaderInterceptor
import jp.co.nsco.basearchitecture.core.network.interceptor.HeaderInterceptor
import jp.co.nsco.basearchitecture.core.network.interceptor.HttpLoggingInterceptorProvider
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * API 通信基盤の依存を提供する Hilt Module。
 *
 * 本 Module は、NetworkConfig / OkHttpClient / Moshi / Retrofit を生成し、
 * アプリ全体で利用できるよう DI コンテナへ登録する責務を持つ。
 *
 * ■ 提供する責務
 *   NetworkConfig の生成
 *   Moshi の生成
 *   OkHttpClient の生成
 *   Retrofit の生成
 *   Interceptor の組み立て
 *
 * ■ 設計上の意図
 *   baseUrl や BuildConfig.DEBUG などのアプリ構成値は app 側で決定する。
 *   core.network は通信処理の共通部品を提供し、
 *   app.network はその部品をアプリ設定に基づいて組み立てる。
 *
 *   これにより、Core が BuildConfig や環境別設定を直接知らない構造にする。
 *
 * ■ 注意
 *   Feature 固有の API Service は本 Module に直接追加しない。
 *   Feature ごとの Service 提供が必要な場合は、Feature 側 Module で
 *   Retrofit から create する。
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * API 通信設定を生成する。
     *
     * baseUrl や HTTP ログ有効/無効はアプリ構成値であるため、
     * BuildConfig から NetworkConfig へ変換する。
     *
     * @return API 通信基盤で使用する NetworkConfig。
     */
    @Provides
    @Singleton
    fun provideNetworkConfig(): NetworkConfig {
        return NetworkConfig(
            baseUrl = BuildConfig.API_BASE_URL,
            enableHttpLogging = BuildConfig.DEBUG
        )
    }

    /**
     * JSON 変換に使用する Moshi を生成する。
     *
     * @return アプリ全体で共有する Moshi。
     */
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    /**
     * API 通信用 OkHttpClient を生成する。
     *
     * 固定 Header、認証 Header、HTTP ログ Interceptor を組み込み、
     * NetworkConfig に定義された timeout を反映する。
     *
     * @param config API 通信設定。
     * @param headerInterceptor 固定共通 Header を付与する Interceptor。
     * @param authHeaderInterceptor 認証 Header を付与する Interceptor。
     * @param loggingInterceptorProvider HTTP 通信ログ Interceptor を生成する Provider。
     * @return API 通信で使用する OkHttpClient。
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        config: NetworkConfig,
        headerInterceptor: HeaderInterceptor,
        authHeaderInterceptor: AuthHeaderInterceptor,
        loggingInterceptorProvider: HttpLoggingInterceptorProvider
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(config.connectTimeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(config.readTimeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(config.writeTimeoutSeconds, TimeUnit.SECONDS)
            .addInterceptor(headerInterceptor)
            .addInterceptor(authHeaderInterceptor)
            .addInterceptor(loggingInterceptorProvider.create(enabled = config.enableHttpLogging))
            .build()
    }

    /**
     * Retrofit を生成する。
     *
     * NetworkConfig の baseUrl、共通 OkHttpClient、Moshi converter を使用して、
     * API Service 生成元となる Retrofit を構成する。
     *
     * @param config API 通信設定。
     * @param okHttpClient API 通信用 OkHttpClient。
     * @param moshi JSON 変換に使用する Moshi。
     * @return API Service 生成に使用する Retrofit。
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        config: NetworkConfig,
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}