package jp.co.nsco.basearchitecture.core.database

/**
 * Database トランザクションを実行するための契約。
 *
 * 本インターフェースは、RoomDatabase.withTransaction の利用を抽象化し、
 * 呼び出し側が RoomDatabase 実装へ直接依存しないようにする。
 *
 * ■ 提供する責務
 *   DBトランザクション実行の抽象化
 *   RoomDatabase依存の隠蔽
 *   複数DAO更新の一貫性確保
 *
 * ■ 設計上の意図
 *   複数テーブルをまとめて更新する処理では、トランザクション境界が重要になる。
 *
 *   UseCase / Repository / LocalDataSource が RoomDatabase を直接参照すると、
 *   DB実装への依存が広がるため、DatabaseTransactionRunner 経由で実行する。
 */
interface DatabaseTransactionRunner {

    /**
     * Database トランザクション内で処理を実行する。
     *
     * block 内で例外が発生した場合、Room 側のトランザクションはロールバックされる。
     *
     * @param block トランザクション内で実行する処理。
     * @return block の実行結果。
     */
    suspend fun <T> runInTransaction(block: suspend () -> T): T
}