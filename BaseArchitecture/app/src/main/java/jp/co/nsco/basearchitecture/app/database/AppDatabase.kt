package jp.co.nsco.basearchitecture.app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.co.nsco.basearchitecture.feature.operationlog.data.local.OperationLogDao
import jp.co.nsco.basearchitecture.feature.operationlog.data.local.OperationLogEntity

/**
 * アプリ全体で利用する Room Database。
 *
 * 本クラスは、アプリに含まれる Room Entity と Dao を集約する。
 *
 * ■ 提供する責務
 *   RoomDatabase定義
 *   Entity一覧の管理
 *   Dao提供
 *
 * ■ 設計上の意図
 *   Room の @Database は Entity 一覧をコンパイル時に定義する必要があるため、
 *   アプリ全体を組み立てる app.database に配置する。
 *
 *   core.database は Feature Entity を知らず、
 *   AppDatabase が Feature 固有の Entity / Dao を集約する。
 */
@Database(
    entities = [
        OperationLogEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * 操作ログ Dao を取得する。
     *
     * @return 操作ログテーブルへアクセスする Dao。
     */
    abstract fun operationLogDao(): OperationLogDao
}