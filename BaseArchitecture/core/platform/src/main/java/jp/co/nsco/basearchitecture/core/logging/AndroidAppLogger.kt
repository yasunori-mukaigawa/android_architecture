package jp.co.nsco.basearchitecture.core.logging

import javax.inject.Inject
import timber.log.Timber

/**
 * Timber を利用する AppLogger 実装。
 *
 * 本クラスは、Timberへログを渡して、
 * アプリ内ログを出力する責務を持つ。
 *
 * ■ 提供する責務
 *   Debug ログの出力
 *   Info ログの出力
 *   Warning ログの出力
 *   Error ログの出力
 *   Timber API 依存の隠蔽
 *
 * ■ 設計上の意図
 *   UseCase / Repository / Gateway / ViewModel などの利用側が、
 *   Android の Log API や Timber API を直接参照しないようにする。
 *
 *   AppLogger を経由することで、
 *   本番では AndroidAppLogger、
 *   テストでは FakeLogger、
 *   将来的には Crashlytics / Datadog / 独自ログ基盤などへ差し替えやすくする。
 *
 * ■ 注意
 *   Timber Tree の登録は AppLoggingInitializer が担当する。
 *   ログの送信、永続化、マスキング、出力制御などの高度な処理が必要な場合は、
 *   アプリ側でTreeの追加やAppLoggerの別実装を検討する。
 */
class AndroidAppLogger @Inject constructor(
    private val loggingConfiguration: AppLoggingConfiguration
) : AppLogger {

    /**
     * Debug レベルのログを出力する。
     *
     * 主に開発時の詳細な動作確認や、一時的な調査情報の出力に使用する。
     *
     * @param message 出力するログメッセージ。
     */
    override fun debug(message: String, tag: String?) {
        Timber.tag(resolveTag(tag)).d(message)
    }

    /**
     * Info レベルのログを出力する。
     *
     * 主に正常系の主要な処理通過や、アプリ動作の把握に使用する。
     *
     * @param message 出力するログメッセージ。
     */
    override fun info(message: String, tag: String?) {
        Timber.tag(resolveTag(tag)).i(message)
    }

    /**
     * Warning レベルのログを出力する。
     *
     * 処理継続は可能だが、注意すべき状態や復旧可能な異常を記録する場合に使用する。
     *
     * @param message 出力するログメッセージ。
     */
    override fun warn(message: String, tag: String?) {
        Timber.tag(resolveTag(tag)).w(message)
    }

    /**
     * Error レベルのログを出力する。
     *
     * 処理失敗や想定外例外など、調査対象となる異常を記録する場合に使用する。
     *
     * @param message 出力するログメッセージ。
     * @param throwable 原因となった例外。例外が存在しない場合は null。
     */
    override fun error(message: String, throwable: Throwable?, tag: String?) {
        Timber.tag(resolveTag(tag)).e(throwable, message)
    }

    /**
     * ログ出力に使用するTAGを解決する。
     *
     * 個別TAGが未指定または空白だけの場合は、
     * Initializerで設定された既定TAGへフォールバックする。
     *
     * @param tag ログメソッドで指定された個別TAG。
     * @return 実際にログ出力へ渡すTAG。
     */
    private fun resolveTag(tag: String?): String {
        return tag?.takeIf { it.isNotBlank() }
            ?: loggingConfiguration.defaultTag
    }
}
