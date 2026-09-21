package jp.co.nsco.basearchitecture.core.database

import androidx.room.RoomDatabase
import androidx.room.withTransaction
import javax.inject.Inject

/**
 * RoomDatabase を利用する DatabaseTransactionRunner 実装。
 *
 * 本クラスは、RoomDatabase.withTransaction を使用して、
 * DBトランザクションを実行する責務を持つ。
 *
 * ■ 提供する責務
 *   Roomトランザクションの実行
 *   RoomDatabase依存の局所化
 *   DatabaseTransactionRunner契約の本番実装
 *
 * ■ 設計上の意図
 *   Repository / LocalDataSource が RoomDatabase.withTransaction を直接呼び出さないようにする。
 *
 *   RoomDatabase への依存を本クラスに閉じ込めることで、
 *   トランザクション実行方法の変更を局所化する。
 *
 * @param database アプリで利用する RoomDatabase。
 */
class RoomDatabaseTransactionRunner @Inject constructor(
    private val database: RoomDatabase
) : DatabaseTransactionRunner {

    /**
     * Room のトランザクション内で処理を実行する。
     *
     * @param block トランザクション内で実行する処理。
     * @return block の実行結果。
     */
    override suspend fun <T> runInTransaction(block: suspend () -> T): T {
        return database.withTransaction {
            block()
        }
    }
}