package jp.co.nsco.basearchitecture.core.coroutine

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * アプリ標準の CoroutineDispatcher を提供する DispatcherProvider 実装。
 *
 * 本クラスは、Android / Kotlin Coroutines が提供する Dispatchers を、
 * アプリ内の共通契約である DispatcherProvider として公開する責務を持つ。
 *
 * ■ 提供する責務
 *   Main Dispatcher の提供
 *   IO Dispatcher の提供
 *   Default Dispatcher の提供
 *
 * ■ 設計上の意図
 *   UseCase や Repository などの利用側が Dispatchers.Main / IO / Default を
 *   直接参照しないようにする。
 *
 *   DispatcherProvider を経由することで、
 *   本番実行時は標準 Dispatcher を使用し、
 *   Unit テスト時は TestDispatcher へ差し替え可能にする。
 *
 * ■ 注意
 *   本クラスは Dispatcher の選択肢を提供するだけであり、
 *   どの処理をどの Dispatcher で実行するかの判断は利用側の責務とする。
 */
class AppDispatcherProvider @Inject constructor() : DispatcherProvider {

    /**
     * UI スレッド上で実行する処理に使用する Dispatcher。
     *
     * Compose の State 更新や UI に近い処理など、
     * Main thread が必要な処理で使用する。
     */
    override val main: CoroutineDispatcher = Dispatchers.Main

    /**
     * IO 処理に使用する Dispatcher。
     *
     * DB、ファイル、DataStore、ネットワーク通信など、
     * ブロッキングまたは IO 待ちが発生する処理で使用する。
     */
    override val io: CoroutineDispatcher = Dispatchers.IO

    /**
     * CPU バウンド処理に使用する Dispatcher。
     *
     * 計算、変換、フィルタリング、大きめのデータ加工など、
     * CPU 使用量が高い処理で使用する。
     */
    override val default: CoroutineDispatcher = Dispatchers.Default
}