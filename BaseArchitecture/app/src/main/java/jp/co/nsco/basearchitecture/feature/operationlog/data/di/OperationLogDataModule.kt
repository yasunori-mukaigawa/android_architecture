package jp.co.nsco.basearchitecture.feature.operationlog.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import jp.co.nsco.basearchitecture.feature.operationlog.data.local.OperationLogLocalDataSource
import jp.co.nsco.basearchitecture.feature.operationlog.data.local.RoomOperationLogLocalDataSource
import jp.co.nsco.basearchitecture.feature.operationlog.data.repository.OperationLogRepositoryImpl
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogRepository

/**
 * 操作ログ Feature の Data 層依存を登録する Hilt Module。
 *
 * 本 Module は、操作ログの Repository / LocalDataSource 契約に対して、
 * 実装クラスを紐付ける。
 *
 * ■ 提供する責務
 *   OperationLogRepository 実装の登録
 *   OperationLogLocalDataSource 実装の登録
 *
 * ■ 設計上の意図
 *   Domain 層は OperationLogRepository 契約に依存し、
 *   Room や Dao などの具体的な保存方式を知らない。
 *
 *   Repository 実装は OperationLogLocalDataSource 契約に依存し、
 *   実際に Room を使うかどうかは本 Module の DI 定義で決定する。
 *
 *   これにより、将来的に保存方式を Room 以外へ差し替える場合でも、
 *   Domain / Application 層への影響を抑えられる。
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class OperationLogDataModule {

    /**
     * OperationLogRepository の実装として OperationLogRepositoryImpl を登録する。
     *
     * @param impl 操作ログRepository実装。
     * @return OperationLogRepository契約。
     */
    @Binds
    @Singleton
    abstract fun bindOperationLogRepository(
        impl: OperationLogRepositoryImpl
    ): OperationLogRepository

    /**
     * OperationLogLocalDataSource の実装として RoomOperationLogLocalDataSource を登録する。
     *
     * @param impl Roomを利用する操作ログLocalDataSource実装。
     * @return OperationLogLocalDataSource契約。
     */
    @Binds
    @Singleton
    abstract fun bindOperationLogLocalDataSource(
        impl: RoomOperationLogLocalDataSource
    ): OperationLogLocalDataSource
}