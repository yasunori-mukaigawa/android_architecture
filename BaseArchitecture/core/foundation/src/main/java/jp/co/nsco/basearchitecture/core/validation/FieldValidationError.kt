package jp.co.nsco.basearchitecture.core.validation

/**
 * 入力項目単位の検証エラー。
 *
 * FieldValidationError は、特定の入力項目に対して発生した
 * 検証失敗の内容を表す。
 *
 * ■ 提供する責務
 *   エラー対象項目の保持
 *   エラーコードの保持
 *   検証失敗理由の保持
 *
 * ■ 設計上の意図
 *   入力検証エラーを単なる文字列メッセージとして扱うのではなく、
 *   field / code / reason に分けて構造化する。
 *
 *   これにより、ViewModel や Builder 側で、
 *   対象項目ごとのエラー表示、エラーコードによる文言解決、
 *   ログ出力やテスト判定を行いやすくする。
 *
 * ■ 注意
 *   reason は画面表示文言そのものではなく、
 *   検証失敗理由を表す説明として扱う。
 *   実際の表示文言は Presentation 側の MessageBuilder / ErrorResolver などで解決する。
 *
 * @property field エラーが発生した入力項目を識別する値。
 * @property code エラー種別を識別するコード。
 * @property reason 検証失敗理由。
 */
data class FieldValidationError(
    val field: String,
    val code: String,
    val reason: String
)