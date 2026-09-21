package jp.co.nsco.basearchitecture.core.coroutine

import kotlinx.coroutines.CoroutineScope

/**
 * アプリ内で共有する CoroutineScope を提供する契約。
 *
 * 本インターフェースは、長寿命のバックグラウンド処理が各所で
 * CoroutineScope を直接生成しないための境界として扱う。
 *
 * ■ 提供する責務
 *   アプリケーション単位で共有する CoroutineScope の提供
 *   Scope の所有者とライフサイクルを明示する契約の提供
 *
 * ■ 設計上の意図
 *   CoroutineScope(SupervisorJob() + Dispatchers.IO) のような生成処理を
 *   DataStore や同期処理などの利用側へ分散させない。
 *
 *   利用側は本契約から取得した Scope を利用し、独自のルート Scope を
 *   必要以上に生成しない。
 *
 * ■ 注意
 *   本契約はアプリケーション単位の長寿命処理を対象とする。
 *   ViewModel の処理では viewModelScope、Compose の UI 処理では
 *   rememberCoroutineScope など、既存のライフサイクルに紐づく Scope を優先する。
 *
 *   処理単位の子 Coroutine 管理には、coroutineScope や supervisorScope を使用し、
 *   本契約の Scope をFeature単位の汎用Scopeとして利用しない。
 */
interface CoroutineScopeProvider {

    /**
     * アプリケーション単位で共有する CoroutineScope。
     *
     * DataStore やアプリ全体で生存するバックグラウンド処理など、
     * 画面のライフサイクルに依存しない処理で使用する。
     */
    val scope: CoroutineScope
}
