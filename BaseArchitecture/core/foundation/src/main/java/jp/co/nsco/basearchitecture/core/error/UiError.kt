package jp.co.nsco.basearchitecture.core.error

/**
 * 画面表示用に整形されたエラー情報。
 *
 * UiError は、AppError などの処理失敗情報を、
 * ユーザーへ表示できる形に変換した Presentation 向けのエラー情報である。
 *
 * ■ 提供する責務
 *   表示用タイトルの保持
 *   表示用メッセージの保持
 *   サポート向け補足情報の保持
 *   ユーザーに提示する操作候補の保持
 *   エラーコードの保持
 *
 * ■ 設計上の意図
 *   AppError はエラーの意味を表し、UiError は表示内容を表す。
 *   この2つを分離することで、Domain / Application 層に UI 文言を持ち込まない。
 *
 *   画面側は AppError を直接解釈せず、
 *   UiError に変換済みの title / message / actions を使用して表示する。
 *
 * @property code エラーを識別するコード。
 *                ログ、問い合わせ、文言解決、テスト判定に使用する。
 * @property title エラー表示のタイトル。
 * @property message ユーザーへ表示する本文メッセージ。
 * @property supportMessage 問い合わせや詳細確認用の補足メッセージ。
 *                          表示不要な場合は null。
 * @property actions ユーザーに提示する操作候補。
 */
data class UiError(
    val code: String,
    val title: String,
    val message: String,
    val supportMessage: String? = null,
    val actions: List<UiErrorAction> = emptyList()
)