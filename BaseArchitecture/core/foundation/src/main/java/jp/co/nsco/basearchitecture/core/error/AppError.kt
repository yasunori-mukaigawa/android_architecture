package jp.co.nsco.basearchitecture.core.error

/**
 * アプリ内で扱う処理失敗の意味を表すエラー契約。
 *
 * AppError は、例外そのものではなく、
 * アプリケーションとして解釈した失敗理由を表す。
 *
 * ■ 提供する責務
 *   入力検証エラーの表現
 *   業務ルール違反の表現
 *   ローカル保存処理失敗の表現
 *   ネットワーク通信失敗の表現
 *   API 応答エラーの表現
 *   API 応答形式不正の表現
 *   想定外エラーの表現
 *
 * ■ 設計上の意図
 *   Throwable をそのまま上位層へ伝播させず、
 *   アプリ内で扱いやすい失敗理由として構造化する。
 *
 *   UseCase / Repository / Gateway は、失敗を例外送出ではなく
 *   AppResult.Failure(AppError) として返すことで、
 *   呼び出し側が失敗理由に応じた分岐を行えるようにする。
 *
 *   AppError は表示文言を直接持たない。
 *   画面表示用の title / message / actions などは、
 *   Presentation 層で UiError へ変換して扱う。
 *
 * ■ 注意
 *   AppError はエラーの意味を表すものであり、
 *   ユーザー向け文言を保持しない。
 *
 *   code はログ、解析、文言解決、テスト判定に使用するため、
 *   呼び出し側で分岐しやすい安定した値を設定する。
 */
sealed interface AppError {

    /**
     * エラーを識別するコード。
     *
     * ログ出力、文言解決、テスト判定、サポート問い合わせ時の識別子として使用する。
     */
    val code: String

    /**
     * 元になった例外。
     *
     * 例外が発生していない業務エラーや入力検証エラーでは null を許容する。
     * UI 表示ではなく、ログ出力や調査用途で使用する。
     */
    val cause: Throwable?

    /**
     * 入力値または画面項目の検証エラー。
     *
     * 必須入力、形式不正、桁数不正、範囲外など、
     * 特定の入力項目に紐づく失敗を表す。
     *
     * @property code エラーを識別するコード。
     * @property field エラー対象の項目名またはフィールド識別子。
     * @property reason 検証に失敗した理由。
     * @property cause 元になった例外。通常は null。
     */
    data class Validation(
        override val code: String,
        val field: String,
        val reason: String,
        override val cause: Throwable? = null
    ) : AppError

    /**
     * 業務ルール違反によるエラー。
     *
     * 権限不足、状態不整合、操作不可条件、業務上許可されない遷移など、
     * システム例外ではなく業務判断として失敗させる場合に使用する。
     *
     * @property code エラーを識別するコード。
     * @property reason 業務ルール上、失敗と判断した理由。
     * @property cause 元になった例外。通常は null。
     */
    data class Business(
        override val code: String,
        val reason: String,
        override val cause: Throwable? = null
    ) : AppError

    /**
     * ローカル保存領域に関するエラー。
     *
     * DataStore、Room、ファイル、キャッシュなど、
     * 端末内の保存領域に対する読み書きで失敗した場合に使用する。
     *
     * @property code エラーを識別するコード。
     * @property cause 元になった例外。
     */
    data class LocalStorage(
        override val code: String,
        override val cause: Throwable? = null
    ) : AppError

    /**
     * ネットワーク通信に関するエラー。
     *
     * タイムアウト、接続不可、DNS失敗、通信中断など、
     * HTTPレスポンスを受け取る前の通信失敗を表す。
     *
     * @property code エラーを識別するコード。
     * @property cause 元になった例外。
     */
    data class Network(
        override val code: String,
        override val cause: Throwable? = null
    ) : AppError

    /**
     * API がエラーレスポンスを返した状態。
     *
     * HTTP ステータスコードがエラーである場合や、
     * サーバー側が業務エラーとして応答した場合に使用する。
     *
     * @property code エラーを識別するコード。
     * @property statusCode API から返却された HTTP ステータスコード。
     * @property reason API 応答や変換処理から得られた失敗理由。
     * @property cause 元になった例外。
     */
    data class Api(
        override val code: String,
        val statusCode: Int,
        val reason: String? = null,
        override val cause: Throwable? = null
    ) : AppError

    /**
     * API 応答形式が期待と異なるエラー。
     *
     * HTTP 通信自体は成功したが、
     * 必須項目不足、JSON 形式不正、型変換失敗、値の不整合などにより、
     * アプリが応答を正しく解釈できない場合に使用する。
     *
     * @property code エラーを識別するコード。
     * @property reason 応答を不正と判断した理由。
     * @property cause 元になった例外。
     */
    data class ApiInvalidResponse(
        override val code: String,
        val reason: String,
        override val cause: Throwable? = null
    ) : AppError

    /**
     * 想定外のエラー。
     *
     * 既存の AppError 種別に分類できない失敗や、
     * 本来発生しないはずの例外を受け止めるために使用する。
     *
     * @property code エラーを識別するコード。既定値は UNEXPECTED。
     * @property cause 元になった例外。
     */
    data class Unexpected(
        override val code: String = "UNEXPECTED",
        override val cause: Throwable? = null
    ) : AppError
}