package jp.co.nsco.basearchitecture.core.logging

/**
 * アプリ内ログを出力するための契約。
 *
 * 本インターフェースは、ログ出力処理を抽象化し、
 * 利用側が Android Log API や外部ログ基盤へ直接依存しないようにする。
 *
 * ■ 提供する責務
 *   Debug ログ出力の抽象化
 *   Info ログ出力の抽象化
 *   Warning ログ出力の抽象化
 *   Error ログ出力の抽象化
 *   ログ出力先の隠蔽
 *
 * ■ 設計上の意図
 *   各クラスが Log.d / Log.i / Log.e などを直接呼び出すと、
 *   テスト時の検証、ログ出力先の変更、ログ抑制、外部ログ基盤連携が難しくなる。
 *
 *   AppLogger を経由することで、
 *   本番環境・テスト環境・開発環境でログ実装を差し替えやすくする。
 *
 * ■ 注意
 *   本契約はログ出力の入口を定義する。
 *   個人情報や機密情報のマスキング、ログ送信可否、出力レベル制御などは、
 *   実装側または呼び出し前の設計で担保する。
 */
interface AppLogger {

    /**
     * Debug レベルのログを出力する。
     *
     * 主に開発時の詳細な動作確認や、一時的な調査情報の出力に使用する。
     *
     * @param message 出力するログメッセージ。
     * @param tag ログ出力に使用するTAG。未指定の場合はInitializer設定値を使用する。
     */
    fun debug(message: String, tag: String? = null)

    /**
     * Info レベルのログを出力する。
     *
     * 正常系の主要な処理通過や、アプリ動作の把握に使用する。
     *
     * @param message 出力するログメッセージ。
     * @param tag ログ出力に使用するTAG。未指定の場合はInitializer設定値を使用する。
     */
    fun info(message: String, tag: String? = null)

    /**
     * Warning レベルのログを出力する。
     *
     * 処理継続は可能だが、注意すべき状態や復旧可能な異常を記録する場合に使用する。
     *
     * @param message 出力するログメッセージ。
     * @param tag ログ出力に使用するTAG。未指定の場合はInitializer設定値を使用する。
     */
    fun warn(message: String, tag: String? = null)

    /**
     * Error レベルのログを出力する。
     *
     * 処理失敗や想定外例外など、調査対象となる異常を記録する場合に使用する。
     *
     * @param message 出力するログメッセージ。
     * @param throwable 原因となった例外。例外が存在しない場合は null。
     * @param tag ログ出力に使用するTAG。未指定の場合はInitializer設定値を使用する。
     */
    fun error(
        message: String,
        throwable: Throwable? = null,
        tag: String? = null
    )
}
