package jp.co.nsco.basearchitecture.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import jp.co.nsco.basearchitecture.core.appinfo.AndroidAppInfoProvider
import jp.co.nsco.basearchitecture.core.appinfo.AppInfoProvider
import jp.co.nsco.basearchitecture.core.coroutine.AppCoroutineScopeProvider
import jp.co.nsco.basearchitecture.core.coroutine.AppDispatcherProvider
import jp.co.nsco.basearchitecture.core.coroutine.CoroutineScopeProvider
import jp.co.nsco.basearchitecture.core.coroutine.DispatcherProvider
import jp.co.nsco.basearchitecture.core.external.AndroidExternalUriOpener
import jp.co.nsco.basearchitecture.core.external.ExternalUriOpener
import jp.co.nsco.basearchitecture.core.logging.AndroidAppLogger
import jp.co.nsco.basearchitecture.core.logging.AppLogger
import jp.co.nsco.basearchitecture.core.logging.AppLoggingConfiguration
import jp.co.nsco.basearchitecture.core.logging.AppLoggingInitializer
import jp.co.nsco.basearchitecture.core.logging.DefaultAppLoggingConfiguration
import jp.co.nsco.basearchitecture.core.logging.TimberAppLoggingInitializer
import jp.co.nsco.basearchitecture.core.resource.AndroidRawResourceReader
import jp.co.nsco.basearchitecture.core.resource.AndroidStringProvider
import jp.co.nsco.basearchitecture.core.resource.RawResourceReader
import jp.co.nsco.basearchitecture.core.resource.StringProvider
import jp.co.nsco.basearchitecture.core.time.AppClock
import jp.co.nsco.basearchitecture.core.time.AppDateTimeProvider
import jp.co.nsco.basearchitecture.core.time.DateTimeProvider
import jp.co.nsco.basearchitecture.core.time.SystemAppClock

/**
 * Core 層の共通依存を DI コンテナへ登録する Hilt Module。
 *
 * 本 Module は、Core 層で定義している抽象契約に対して、
 * Android 環境で利用する標準実装を紐付ける責務を持つ。
 *
 * ■ 提供する責務
 *   文字列リソース取得契約の登録
 *   Raw リソース読取契約の登録
 *   Coroutine Dispatcher 契約の登録
 *   アプリケーション単位 CoroutineScope 契約の登録
 *   Logging 契約の登録
 *   Logging 共通設定の登録
 *   Logging 初期化契約の登録
 *   日時取得契約の登録
 *   Clock 契約の登録
 *   ライセンス情報取得契約の登録
 *   アプリ情報取得契約の登録
 *   外部 URI 起動契約の登録
 *
 * ■ 設計上の意図
 *   利用側は AndroidStringProvider や AndroidAppLogger などの具象クラスに依存せず、
 *   StringProvider / AppLogger などの契約に依存する。
 *
 *   これにより、実装差し替え、Unit テスト、環境別実装の切り替えを行いやすくする。
 *
 * ■ 注意
 *   本 Module には、Core の標準実装として完結する依存登録のみを置く。
 *
 *   DataStore のように、保存ファイル名や生成スコープなど
 *   アプリ構成に依存する依存登録は app 側 Module に配置する。
 *
 *   Feature 固有の Repository / DAO / Gateway などをここに追加しない。
 *   CoreModule が Feature を知り始めると、Core が共通基盤ではなく
 *   アプリ固有の便利置き場になってしまうため。
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    /**
     * 文字列リソース取得契約の実装を登録する。
     *
     * Android の Resources 取得処理を AndroidStringProvider に閉じ込め、
     * 利用側は StringProvider の契約のみを参照する。
     *
     * @param impl Android Resources を利用する StringProvider 実装。
     * @return DI コンテナへ登録する StringProvider 実装。
     */
    @Binds
    @Singleton
    abstract fun bindStringProvider(
        impl: AndroidStringProvider
    ): StringProvider

    /**
     * Raw リソース読取契約の実装を登録する。
     *
     * Android の raw resource 読取処理を AndroidRawResourceReader に閉じ込め、
     * 利用側は RawResourceReader の契約のみを参照する。
     *
     * @param impl Android raw resource を読み取る RawResourceReader 実装。
     * @return DI コンテナへ登録する RawResourceReader 実装。
     */
    @Binds
    @Singleton
    abstract fun bindRawResourceReader(
        impl: AndroidRawResourceReader
    ): RawResourceReader

    /**
     * CoroutineDispatcher 提供契約の実装を登録する。
     *
     * Dispatchers.Main / IO / Default を利用側が直接参照しないようにし、
     * テスト時に Dispatcher を差し替えられる構造にする。
     *
     * @param impl アプリ標準の CoroutineDispatcher を提供する実装。
     * @return DI コンテナへ登録する DispatcherProvider 実装。
     */
    @Binds
    @Singleton
    abstract fun bindDispatcherProvider(
        impl: AppDispatcherProvider
    ): DispatcherProvider

    /**
     * アプリケーション単位 CoroutineScope 契約の実装を登録する。
     *
     * 長寿命のバックグラウンド処理が個別に Scope を生成しないようにし、
     * Core の標準 Scope を Singleton として共有する。
     *
     * @param impl アプリケーション単位の CoroutineScope を提供する実装。
     * @return DI コンテナへ登録する CoroutineScopeProvider 実装。
     */
    @Binds
    @Singleton
    abstract fun bindCoroutineScopeProvider(
        impl: AppCoroutineScopeProvider
    ): CoroutineScopeProvider

    /**
     * アプリログ出力契約の実装を登録する。
     *
     * Android の Log API など、実行環境に依存するログ出力処理を隠蔽する。
     *
     * @param impl Android 環境向けの AppLogger 実装。
     * @return DI コンテナへ登録する AppLogger 実装。
     */
    @Binds
    @Singleton
    abstract fun bindAppLogger(
        impl: AndroidAppLogger
    ): AppLogger

    /**
     * ログ共通設定契約の実装を登録する。
     *
     * Initializer で設定された既定TAGを、AppLoggerのログ出力へ共有する。
     *
     * @param impl アプリログ共通設定の標準実装。
     * @return DI コンテナへ登録する AppLoggingConfiguration 実装。
     */
    @Binds
    @Singleton
    abstract fun bindAppLoggingConfiguration(
        impl: DefaultAppLoggingConfiguration
    ): AppLoggingConfiguration

    /**
     * ログ出力基盤初期化契約の実装を登録する。
     *
     * Application 起動時の Timber Tree 登録を初期化実装へ委譲し、
     * Application が Timber APIへ直接依存しない構造にする。
     *
     * @param impl Timberを利用したログ初期化実装。
     * @return DI コンテナへ登録する AppLoggingInitializer 実装。
     */
    @Binds
    @Singleton
    abstract fun bindAppLoggingInitializer(
        impl: TimberAppLoggingInitializer
    ): AppLoggingInitializer

    /**
     * 日時取得契約の実装を登録する。
     *
     * 現在日時の取得を DateTimeProvider 経由にすることで、
     * 利用側がシステム時刻へ直接依存しないようにする。
     *
     * @param impl アプリ標準の日時取得実装。
     * @return DI コンテナへ登録する DateTimeProvider 実装。
     */
    @Binds
    @Singleton
    abstract fun bindDateTimeProvider(
        impl: AppDateTimeProvider
    ): DateTimeProvider

    /**
     * 現在時刻取得契約の実装を登録する。
     *
     * System.currentTimeMillis などの直接参照を避け、
     * テスト時に時刻を固定できる構造にする。
     *
     * @param impl システム時刻を返す AppClock 実装。
     * @return DI コンテナへ登録する AppClock 実装。
     */
    @Binds
    @Singleton
    abstract fun bindAppClock(
        impl: SystemAppClock
    ): AppClock

    /**
     * アプリ情報取得契約の実装を登録する。
     *
     * Context / PackageManager / SDK バージョン差分を AndroidAppInfoProvider に閉じ込め、
     * 利用側は AppInfoProvider の契約のみを参照する。
     *
     * @param impl Android 標準 API を利用する AppInfoProvider 実装。
     * @return DI コンテナへ登録する AppInfoProvider 実装。
     */
    @Binds
    @Singleton
    abstract fun bindAppInfoProvider(
        impl: AndroidAppInfoProvider
    ): AppInfoProvider

    /**
     * 外部 URI 起動契約の実装を登録する。
     *
     * Intent によるブラウザ起動や外部アプリ起動処理を実装側に閉じ込め、
     * 利用側は ExternalUriOpener の契約のみを参照する。
     *
     * @param impl Android Intent を利用する ExternalUriOpener 実装。
     * @return DI コンテナへ登録する ExternalUriOpener 実装。
     */
    @Binds
    @Singleton
    abstract fun bindExternalUriOpener(
        impl: AndroidExternalUriOpener
    ): ExternalUriOpener
}
