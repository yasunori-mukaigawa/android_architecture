package jp.co.nsco.basearchitecture.feature.operationlog.domain

/**
 * 操作ログ種別。
 *
 * OperationLogType は、アプリ内で記録する操作ログの分類を表す。
 *
 * ■ 提供する責務
 *   画面表示ログの分類
 *   ユーザー操作ログの分類
 *   設定変更ログの分類
 *   アップロード処理ログの分類
 *   ダウンロード処理ログの分類
 *   検索処理ログの分類
 *   エラーログの分類
 *   システム処理ログの分類
 *   DB保存値への変換
 *   DB保存値からの復元
 *
 * ■ 設計上の意図
 *   操作ログ種別を文字列だけで扱うと、typo や不正値混入が起きやすい。
 *
 *   enum class として型付けすることで、
 *   UseCase / Repository / Presentation 間で安全に操作ログ種別を受け渡せるようにする。
 *
 *   また、DB保存値に enum の name を直接使うと、
 *   enum 名の変更が既存DBとの互換性破壊につながる。
 *
 *   そのため、保存用の値は storageValue として明示的に定義し、
 *   enum 名と保存値の互換性を分離する。
 *
 * @property storageValue DB保存時に使用する安定した文字列。
 */
enum class OperationLogType(
    private val storageValue: String
) {

    /**
     * 画面表示や画面遷移に関するログ。
     */
    Screen("screen"),

    /**
     * ボタン押下などのユーザー操作に関するログ。
     */
    Action("action"),

    /**
     * 設定変更に関するログ。
     */
    Setting("setting"),

    /**
     * アップロード処理に関するログ。
     */
    Upload("upload"),

    /**
     * ダウンロード処理に関するログ。
     */
    Download("download"),

    /**
     * 検索処理に関するログ。
     */
    Search("search"),

    /**
     * エラー発生に関するログ。
     */
    Error("error"),

    /**
     * システム内部処理に関するログ。
     */
    System("system");

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
         * DB保存値から OperationLogType を復元する。
         *
         * 不明な値が渡された場合は System を返し、
         * 表示継続を優先する。
         *
         * @param value DBに保存されていた文字列。
         * @return 対応する OperationLogType。不明な場合は System。
         */
        fun fromStorageValue(value: String): OperationLogType {
            return entries.firstOrNull { type ->
                type.storageValue == value
            } ?: System
        }
    }
}