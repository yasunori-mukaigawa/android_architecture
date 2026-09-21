package jp.co.nsco.basearchitecture.core.coroutine

import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * アプリケーション単位の CoroutineScope を提供する標準実装。
 *
 * 本クラスは、アプリ全体で共有する長寿命処理用の Scope を一箇所で生成し、
 * CoroutineScopeProvider 契約として公開する責務を持つ。
 *
 * ■ 提供する責務
 *   ApplicationScope の生成
 *   DispatcherProvider.io の適用
 *   SupervisorJob による子処理間の独立性確保
 *
 * ■ 設計上の意図
 *   DataStore や将来のアプリ全体同期処理が、個別に
 *   CoroutineScope(SupervisorJob() + Dispatchers.IO) を生成することを防ぐ。
 *
 *   Dispatcher の選択は DispatcherProvider に委譲し、
 *   Scope の生成と所有方針だけを本クラスに集約する。
 *
 * ■ 注意
 *   本Scopeはアプリケーションのライフサイクルに合わせて使用する。
 *   ViewModel や画面単位の処理を本Scopeで起動すると、画面破棄後も処理が
 *   継続する可能性があるため、用途を長寿命処理に限定する。
 *
 * @param dispatcherProvider アプリ標準の Dispatcher 提供契約。
 */
class AppCoroutineScopeProvider @Inject constructor(
    dispatcherProvider: DispatcherProvider
) : CoroutineScopeProvider {

    /**
     * アプリケーション単位で共有する CoroutineScope。
     *
     * IO処理を標準とし、子Coroutineの失敗で他の子処理まで不要に
     * キャンセルされないよう SupervisorJob を使用する。
     */
    override val scope: CoroutineScope =
        CoroutineScope(
            SupervisorJob() + dispatcherProvider.io
        )
}
