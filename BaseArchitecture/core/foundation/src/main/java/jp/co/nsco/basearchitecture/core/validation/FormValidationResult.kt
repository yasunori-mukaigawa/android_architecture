package jp.co.nsco.basearchitecture.core.validation

/**
 * フォーム全体、または複数項目に対する検証結果。
 *
 * FormValidationResult は、複数の FieldValidationError を集約し、
 * 入力全体として有効かどうかを判断するためのモデルである。
 *
 * ■ 提供する責務
 *   複数項目の検証エラー保持
 *   フォーム全体の有効/無効判定
 *   項目別エラー抽出
 *   エラー追加による新しい検証結果生成
 *
 * ■ 設計上の意図
 *   複数項目の検証結果を List<FieldValidationError> のまま扱うと、
 *   「フォームとして有効か」
 *   「特定項目にエラーがあるか」
 *   「特定項目のエラーだけ取得したい」
 *   といった判定が呼び出し側に散らばりやすくなる。
 *
 *   FormValidationResult として集約することで、
 *   UseCase / Policy / ViewModel が検証結果を一貫した形で扱えるようにする。
 *
 * ■ 注意
 *   本クラスは検証結果の集約モデルであり、検証ロジックそのものは持たない。
 *   実際の入力チェックは Policy / Validator / UseCase などで行う。
 *
 * @property errors 検証失敗した項目の一覧。空の場合はフォーム全体が有効。
 */
data class FormValidationResult(
    val errors: List<FieldValidationError> = emptyList()
) {

    /**
     * フォーム全体が有効かどうか。
     *
     * 検証エラーが1件も存在しない場合に true を返す。
     */
    val isValid: Boolean
        get() = errors.isEmpty()

    /**
     * フォーム全体が無効かどうか。
     *
     * 検証エラーが1件以上存在する場合に true を返す。
     */
    val isInvalid: Boolean
        get() = errors.isNotEmpty()

    /**
     * 指定された項目に紐づく検証エラーを取得する。
     *
     * 同一項目に複数の検証エラーが存在する場合は、すべて返す。
     *
     * @param field 取得対象の項目識別子。
     * @return 指定項目に紐づく検証エラー一覧。
     */
    fun errorsOf(field: String): List<FieldValidationError> {
        return errors.filter { error -> error.field == field }
    }

    /**
     * 指定された項目の最初の検証エラーを取得する。
     *
     * 画面上で項目ごとに1つだけエラーメッセージを表示したい場合に使用する。
     *
     * @param field 取得対象の項目識別子。
     * @return 指定項目の最初の検証エラー。存在しない場合は null。
     */
    fun firstErrorOf(field: String): FieldValidationError? {
        return errors.firstOrNull { error -> error.field == field }
    }

    /**
     * 指定された項目に検証エラーが存在するか判定する。
     *
     * @param field 判定対象の項目識別子。
     * @return 指定項目にエラーが存在する場合は true。
     */
    fun hasError(field: String): Boolean {
        return errors.any { error -> error.field == field }
    }

    /**
     * 検証エラーを追加した新しい FormValidationResult を生成する。
     *
     * 本クラスは immutable な結果モデルとして扱うため、
     * 現在の errors を直接変更せず、新しいインスタンスを返す。
     *
     * @param error 追加する検証エラー。
     * @return エラー追加後の FormValidationResult。
     */
    fun add(error: FieldValidationError): FormValidationResult {
        return copy(errors = errors + error)
    }

    /**
     * 複数の検証エラーを追加した新しい FormValidationResult を生成する。
     *
     * @param additionalErrors 追加する検証エラー一覧。
     * @return エラー追加後の FormValidationResult。
     */
    fun addAll(additionalErrors: List<FieldValidationError>): FormValidationResult {
        return copy(errors = errors + additionalErrors)
    }

    companion object {

        /**
         * 検証成功を表す FormValidationResult。
         *
         * エラーが1件も存在しない状態を表す。
         */
        val Valid = FormValidationResult()

        /**
         * 1件の検証エラーから FormValidationResult を生成する。
         *
         * @param error 検証エラー。
         * @return 指定エラーを保持する FormValidationResult。
         */
        fun invalid(error: FieldValidationError): FormValidationResult {
            return FormValidationResult(errors = listOf(error))
        }

        /**
         * 複数の検証エラーから FormValidationResult を生成する。
         *
         * @param errors 検証エラー一覧。
         * @return 指定エラー一覧を保持する FormValidationResult。
         */
        fun invalid(errors: List<FieldValidationError>): FormValidationResult {
            return FormValidationResult(errors = errors)
        }
    }
}