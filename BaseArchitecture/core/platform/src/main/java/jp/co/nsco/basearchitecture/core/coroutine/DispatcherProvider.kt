package jp.co.nsco.basearchitecture.core.coroutine

import kotlinx.coroutines.CoroutineDispatcher

/**
 * アプリ内で使用する CoroutineDispatcher を提供する契約。
 *
 * 本インターフェースは、非同期処理の実行 Dispatcher を抽象化し、
 * 呼び出し側が Dispatchers を直接参照しないための境界として扱う。
 *
 * ■ 提供する責務
 *   UI 処理用 Dispatcher の提供
 *   IO 処理用 Dispatcher の提供
 *   CPU バウンド処理用 Dispatcher の提供
 *   テスト時の Dispatcher 差し替え口の提供
 *
 * ■ 設計上の意図
 *   Dispatchers.Main / IO / Default を各クラスが直接参照すると、
 *   テスト時に Coroutine の実行制御が難しくなる。
 *
 *   DispatcherProvider を依存注入することで、
 *   本番では AppDispatcherProvider、
 *   Unit テストでは TestDispatcherProvider のように差し替えられる。
 *
 * ■ 注意
 *   DispatcherProvider は Dispatcher の供給のみを行う。
 *   withContext を使うか、どの Dispatcher を選ぶかは、
 *   処理内容を持つ UseCase / Repository / Gateway 側で判断する。
 */
interface DispatcherProvider {

    /**
     * UI スレッド上で実行する処理に使用する Dispatcher。
     *
     * @return Main thread 用 CoroutineDispatcher。
     */
    val main: CoroutineDispatcher

    /**
     * IO 処理に使用する Dispatcher。
     *
     * DB、ファイル、DataStore、ネットワーク通信などの処理で使用する。
     *
     * @return IO 処理用 CoroutineDispatcher。
     */
    val io: CoroutineDispatcher

    /**
     * CPU バウンド処理に使用する Dispatcher。
     *
     * 計算、変換、フィルタリングなどの処理で使用する。
     *
     * @return CPU バウンド処理用 CoroutineDispatcher。
     */
    val default: CoroutineDispatcher
}