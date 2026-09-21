package jp.co.nsco.basearchitecture.app.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import jp.co.nsco.basearchitecture.core.database.DatabaseConfig
import jp.co.nsco.basearchitecture.feature.operationlog.data.local.OperationLogDao

/**
 * Database関連の依存を生成する Hilt Module。
 *
 * 本 Module は、Room Database の生成、RoomDatabase抽象型の提供、
 * Feature Dao の提供を行う。
 *
 * ■ 提供する責務
 *   DatabaseConfigの提供
 *   AppDatabaseの生成
 *   RoomDatabase抽象型の提供
 *   Feature Dao の提供
 *
 * ■ 設計上の意図
 *   DB名、Entity一覧、Dao提供、Migrationなどはアプリ構成に依存するため、
 *   core.database ではなく app.database 側で管理する。
 *
 *   core.database は共通契約と補助実装のみを提供し、
 *   app.database がそれらを具体的な AppDatabase と接続する。
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseProvideModule {

    /**
     * Database設定を提供する。
     *
     * @return アプリで利用するDatabase設定。
     */
    @Provides
    @Singleton
    fun provideDatabaseConfig(): DatabaseConfig {
        return DatabaseConfig(
            name = "base_architecture.db",
            fallbackToDestructiveMigration = false
        )
    }

    /**
     * AppDatabaseを生成する。
     *
     * @param context アプリケーションContext。
     * @param config Database設定。
     * @return アプリ全体で共有するAppDatabase。
     */
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        config: DatabaseConfig
    ): AppDatabase {
        val builder = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            config.name
        )

        if (config.fallbackToDestructiveMigration) {
            builder.fallbackToDestructiveMigration()
        }

        return builder.build()
    }

    /**
     * AppDatabaseをRoomDatabaseとして提供する。
     *
     * RoomDatabaseTransactionRunner は特定の AppDatabase ではなく、
     * RoomDatabase 抽象型に依存する。
     *
     * @param appDatabase アプリのRoomDatabase実装。
     * @return RoomDatabase抽象型としてのAppDatabase。
     */
    @Provides
    @Singleton
    fun provideRoomDatabase(
        appDatabase: AppDatabase
    ): RoomDatabase {
        return appDatabase
    }

    /**
     * 操作ログDaoを提供する。
     *
     * @param database アプリDatabase。
     * @return 操作ログDao。
     */
    @Provides
    @Singleton
    fun provideOperationLogDao(
        database: AppDatabase
    ): OperationLogDao {
        return database.operationLogDao()
    }
}