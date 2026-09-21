package jp.co.nsco.basearchitecture.feature.operationlog.domain

import javax.inject.Inject

/**
 * 操作ログ保持期間に関する Policy。
 *
 * OperationLogRetentionPolicy は、操作ログの保持日数から
 * 削除対象となる境界時刻を算出する責務を持つ。
 *
 * ■ 提供する責務
 *   保持日数の正規化
 *   削除境界時刻の算出
 *
 * ■ 設計上の意図
 *   UseCase が保持日数の下限値や既定値、日数からミリ秒への変換ルールを
 *   直接持たないようにする。
 *
 *   操作ログの保持期間に関するルールを Policy に集約することで、
 *   保持期間ルールの変更を局所化できる。
 *
 * ■ 注意
 *   本 Policy は epoch millis を前提に計算する。
 *   日付単位での厳密な境界、タイムゾーン、営業日単位の保持などが必要な場合は、
 *   ZonedDateTime ベースの Policy へ拡張する。
 */
class OperationLogRetentionPolicy @Inject constructor() {

    /**
     * 操作ログ削除対象の境界時刻を算出する。
     *
     * retentionDays が最小値未満の場合は既定値に補正し、
     * 現在時刻から保持日数分を差し引いた時刻を返す。
     *
     * @param nowMillis 現在時刻。epoch millis。
     * @param retentionDays 操作ログを保持する日数。
     * @return 削除対象となる境界時刻。epoch millis。
     */
    fun calculateThresholdMillis(
        nowMillis: Long,
        retentionDays: Int
    ): Long {
        val normalizedDays = if (retentionDays < MIN_RETENTION_DAYS) {
            DEFAULT_RETENTION_DAYS
        } else {
            retentionDays
        }

        return nowMillis - normalizedDays * MILLIS_PER_DAY
    }

    private companion object {

        /**
         * 操作ログ保持日数として許可する最小値。
         */
        private const val MIN_RETENTION_DAYS = 1

        /**
         * 不正な保持日数が指定された場合の既定保持日数。
         */
        private const val DEFAULT_RETENTION_DAYS = 90

        /**
         * 1日を表すミリ秒。
         */
        private const val MILLIS_PER_DAY = 24L * 60L * 60L * 1000L
    }
}