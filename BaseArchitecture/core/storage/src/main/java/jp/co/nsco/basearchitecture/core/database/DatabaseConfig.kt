package jp.co.nsco.basearchitecture.core.database

/**
 * Room Database を構成するための共通設定。
 *
 * DatabaseConfig は、DB名や破壊的マイグレーション許可など、
 * アプリ側で決定するデータベース設定を保持する。
 *
 * ■ 提供する責務
 *   Databaseファイル名の保持
 *   破壊的マイグレーション許可設定の保持
 *   Schema export設定の判断補助
 *
 * ■ 設計上の意図
 *   Room.databaseBuilder に直接設定値を埋め込まず、
 *   app.database 側で DatabaseConfig として定義する。
 *
 *   core.database は設定値の型だけを提供し、
 *   実際の DB 名や Migration 方針は app 側で決定する。
 *
 * @property name Databaseファイル名。
 * @property fallbackToDestructiveMigration true の場合、破壊的マイグレーションを許可する。
 *
 * @throws IllegalArgumentException name が空の場合。
 */
data class DatabaseConfig(
    val name: String,
    val fallbackToDestructiveMigration: Boolean = false
) {
    init {
        require(name.isNotBlank()) {
            "Database name must not be blank."
        }
    }
}