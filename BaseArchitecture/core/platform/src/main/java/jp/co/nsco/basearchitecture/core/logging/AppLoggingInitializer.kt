package jp.co.nsco.basearchitecture.core.logging

/**
 * アプリ起動時のログ出力基盤を初期化する契約。
 *
 * 本契約は、Timber Tree などのプロセス単位のログ出力先を、
 * Application の起動処理から初期化するために使用する。
 *
 * ■ 提供する責務
 *   ログ出力基盤の初期化入口の提供
 *   Debug / Release 環境の初期化方針受け渡し
 *   アプリ共通の既定 TAG 設定口の提供
 *   Application とログ実装の直接結合回避
 *
 * ■ 設計上の意図
 *   AppLogger は個別ログの出力契約を担当し、
 *   Timber Tree の登録のようなプロセス全体の初期化は本契約へ分離する。
 *
 *   これにより、Application は初期化処理を呼び出すだけになり、
 *   Timber の具体APIやTreeの種類を直接知らずに済む。
 *
 * ■ 注意
 *   初期化は Application の onCreate から一度だけ呼び出す。
 *   ログ出力の個別処理は AppLogger を利用し、
 *   Feature から初期化処理を呼び出さない。
 */
interface AppLoggingInitializer {

    /**
     * ログ出力基盤を初期化する。
     *
     * @param isDebug Debug ビルドの場合は true。
     * @param tag ログメソッドでTAGが指定されなかった場合に使用する既定 TAG。
     */
    fun initialize(isDebug: Boolean, tag: String? = null)
}
