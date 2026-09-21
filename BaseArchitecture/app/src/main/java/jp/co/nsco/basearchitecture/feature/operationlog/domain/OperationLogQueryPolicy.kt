package jp.co.nsco.basearchitecture.feature.operationlog.domain

import javax.inject.Inject

/**
 * 操作ログ取得条件に関する Policy。
 *
 * OperationLogQueryPolicy は、操作ログ取得時に使用する
 * limit などのクエリ条件を補正する責務を持つ。
 *
 * ■ 提供する責務
 *   取得上限件数の正規化
 *   不正な取得件数の補正
 *
 * ■ 設計上の意図
 *   UseCase や ViewModel が、取得件数の下限値や既定値を
 *   個別に判断しないようにする。
 *
 *   クエリ条件の補正ルールを Policy に集約することで、
 *   操作ログ取得に関するルールを Domain 層に閉じ込める。
 */
class OperationLogQueryPolicy @Inject constructor() {

    /**
     * 取得上限件数を正規化する。
     *
     * limit が最小値未満の場合は、既定値に補正する。
     *
     * @param limit 呼び出し側から指定された取得上限件数。
     * @return 正規化後の取得上限件数。
     */
    fun normalizeLimit(limit: Int): Int {
        return if (limit < MIN_LIMIT) {
            DEFAULT_LIMIT
        } else {
            limit
        }
    }

    private companion object {

        /**
         * 取得上限件数として許可する最小値。
         */
        private const val MIN_LIMIT = 1

        /**
         * 不正な取得上限件数が指定された場合の既定値。
         */
        private const val DEFAULT_LIMIT = 50
    }
}