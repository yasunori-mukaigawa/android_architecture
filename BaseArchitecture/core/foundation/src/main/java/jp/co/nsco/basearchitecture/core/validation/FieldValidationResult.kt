package jp.co.nsco.basearchitecture.core.validation

/**
 * 入力項目単位の検証結果。
 *
 * FieldValidationResult は、1つの入力項目に対する検証が
 * 成功したか失敗したかを表す。
 *
 * ■ 提供する責務
 *   入力項目検証成功の表現
 *   入力項目検証失敗の表現
 *   検証エラー情報の保持
 *
 * ■ 設計上の意図
 *   入力検証結果を Boolean や nullable message で表すと、
 *   「なぜ失敗したのか」「どの項目が失敗したのか」が曖昧になりやすい。
 *
 *   FieldValidationResult として成功/失敗を明示し、
 *   失敗時は FieldValidationError を保持することで、
 *   UseCase / Policy / ViewModel 側で検証結果を扱いやすくする。
 *
 * ■ 注意
 *   本結果は単一項目の検証結果を表す。
 *   複数項目をまとめて検証する場合は、
 *   FormValidationResult を使用する。
 */
sealed interface FieldValidationResult {

    /**
     * 入力項目の検証成功。
     *
     * 対象項目に検証エラーが存在しないことを表す。
     */
    data object Valid : FieldValidationResult

    /**
     * 入力項目の検証失敗。
     *
     * @property error 検証失敗の詳細。
     */
    data class Invalid(
        val error: FieldValidationError
    ) : FieldValidationResult
}