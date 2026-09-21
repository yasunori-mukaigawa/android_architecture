package jp.co.nsco.basearchitecture.feature.operationlog.domain

/**
 * 操作ログの結果種別。
 *
 * OperationLogResult は、記録対象の操作や処理が
 * どのような結果で終わったかを表す。
 *
 * ■ 提供する責務
 *   成功結果の表現
 *   失敗結果の表現
 *   警告結果の表現
 *   情報通知結果の表現
 *   DB保存値への変換
 *   DB保存値からの復元
 *
 * ■ 設計上の意図
 *   操作結果を文字列だけで扱うと、typo や不正値混入が起きやすい。
 *
 *   enum class として型付けすることで、
 *   UseCase / Repository / Presentation 間で安全に操作結果を受け渡せるようにする。
 *
 *   また、DB保存値に enum の name を直接使うと、
 *   enum 名の変更が既存DBとの互換性破壊につながる。
 *
 *   そのため、保存用の値は storageValue として明示的に定義し、
 *   enum 名と保存値の互換性を分離する。
 *
 * @property storageValue DB保存時に使用する安定した文字列。
 */
enum class OperationLogResult(
    private val storageValue: String
) {

    /**
     * 正常に完了した操作。
     */
    Success("success"),

    /**
     * 失敗した操作。
     */
    Failure("failure"),

    /**
     * 警告を伴って完了した操作。
     */
    Warning("warning"),

    /**
     * 情報通知として記録する操作。
     */
    Info("info");

    /**
     * DB保存用の文字列へ変換する。
     *
     * @return DB保存用の安定値。
     */
    fun toStorageValue(): String {
        return storageValue
    }

    companion object {

        /**
         * DB保存値から OperationLogResult を復元する。
         *
         * 不明な値が渡された場合は Info を返し、
         * 表示継続を優先する。
         *
         * @param value DBに保存されていた文字列。
         * @return 対応する OperationLogResult。不明な場合は Info。
         */
        fun fromStorageValue(value: String): OperationLogResult {
            return entries.firstOrNull { result ->
                result.storageValue == value
            } ?: Info
        }
    }
}