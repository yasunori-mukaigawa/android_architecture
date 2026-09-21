package jp.co.nsco.basearchitecture.app.database

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import jp.co.nsco.basearchitecture.core.database.DatabaseExecutor
import jp.co.nsco.basearchitecture.core.database.DatabaseTransactionRunner
import jp.co.nsco.basearchitecture.core.database.DefaultDatabaseExecutor
import jp.co.nsco.basearchitecture.core.database.RoomDatabaseTransactionRunner

/**
 * Database関連の契約と実装を紐付ける Hilt Module。
 *
 * 本 Module は、core.database が提供する契約に対して、
 * Room を利用する標準実装を登録する。
 *
 * ■ 提供する責務
 *   DatabaseTransactionRunner 実装の登録
 *   DatabaseExecutor 実装の登録
 *
 * ■ 設計上の意図
 *   core.database は契約と標準実装を提供する。
 *   app.database は、アプリでどの実装を利用するかを DI で決定する。
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseBindModule {

    /**
     * DatabaseTransactionRunner の実装として RoomDatabaseTransactionRunner を登録する。
     *
     * @param impl RoomDatabase を利用するトランザクション実行実装。
     * @return DatabaseTransactionRunner契約。
     */
    @Binds
    @Singleton
    abstract fun bindDatabaseTransactionRunner(
        impl: RoomDatabaseTransactionRunner
    ): DatabaseTransactionRunner

    /**
     * DatabaseExecutor の実装として DefaultDatabaseExecutor を登録する。
     *
     * @param impl DB処理を AppResult として安全実行する標準実装。
     * @return DatabaseExecutor契約。
     */
    @Binds
    @Singleton
    abstract fun bindDatabaseExecutor(
        impl: DefaultDatabaseExecutor
    ): DatabaseExecutor
}