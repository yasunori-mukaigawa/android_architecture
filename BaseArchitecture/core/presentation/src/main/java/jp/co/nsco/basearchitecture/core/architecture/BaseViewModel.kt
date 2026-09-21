package jp.co.nsco.basearchitecture.core.architecture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * MVI パターンに基づく ViewModel 基底クラス。
 *
 * BaseViewModel は、UiState / UiEffect の公開形式と UiEvent の入力口を統一する。
 * 画面側は sendEvent でイベントを通知し、各 Feature ViewModel は handleEvent で
 * UseCase 呼び出し、UiMessage dispatch、UiEffect 発行を実装する。
 *
 * ■ 提供する責務
 *   UiState の保持
 *   UiState の StateFlow 公開
 *   UiEffect の SharedFlow 公開
 *   UiEvent の Channel 入力
 *   UiEvent の順序処理
 *   Reducer 経由の State 更新
 *   一度きりの Effect 発行
 *
 * ■ 設計上の意図
 *   Screen / Route から ViewModel のイベント処理本体を直接呼ばせず、
 *   BaseViewModel の sendEvent を共通入口にする。
 *
 *   UiEvent は複数購読される通知ではなく、ViewModel が順番に処理する入力である。
 *   そのため SharedFlow ではなく Channel を使い、イベントをキューとして扱う。
 *   Channel が受信した順番での処理は保証するが、複数スレッドから同時に
 *   sendEvent された場合の呼び出し開始順までは保証しない。
 *
 *   将来的に多重クリック抑制、イベントログ、例外ハンドリング、Busy 中制御などを
 *   BaseViewModel に集約しやすい形にする。
 *
 * ■ 設計上の制約
 *   State を直接変更しない。
 *   State 更新は dispatch を通じて Reducer に委譲する。
 *   Reducer では副作用を発生させない。
 *   Navigation / Dialog / Snackbar などの一度きりの処理は UiEffect として扱う。
 *
 * @param S 画面が保持する UI 状態の型。
 * @param E View から通知される UI イベントの型。
 * @param M State 更新理由を表す Message の型。
 * @param F 一度きりで処理する UI 副作用の型。
 * @param initialState ViewModel 生成時の初期状態。
 * @param reducer 現在の State と Message から次の State を生成する Reducer。
 */
abstract class BaseViewModel<
        S : UiState,
        E : UiEvent,
        M : UiMessage,
        F : UiEffect
        >(
    initialState: S,
    private val reducer: Reducer<S, M>
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)

    /**
     * 画面へ公開する UI 状態。
     *
     * View / Screen は本 StateFlow を購読し、現在の State に基づいて画面を描画する。
     */
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<F>(
        replay = 0,
        extraBufferCapacity = 0
    )

    /**
     * 画面へ公開する一度きりの UI 副作用。
     *
     * Navigation / Dialog / Snackbar など、UiState として保持すべきではない処理を通知する。
     */
    val uiEffect: SharedFlow<F> = _uiEffect.asSharedFlow()

    private val eventChannel = Channel<E>(capacity = Channel.BUFFERED)

    /**
     * 現在の UI 状態。
     *
     * 派生 ViewModel がイベント処理中に現在値を参照するために利用する。
     * 取得した State は直接変更せず、必要な変更は Message を生成して dispatch する。
     */
    protected val stateValue: S
        get() = _uiState.value

    init {
        viewModelScope.launch {
            for (event in eventChannel) {
                handleEvent(event)
            }
        }
    }

    /**
     * View / Screen / Route から UI イベントを通知する。
     *
     * 本メソッドは suspend 関数にせず、画面側が通常の callback として扱えるようにする。
     * 通知されたイベントは Channel に積まれ、BaseViewModel 内で順番に handleEvent へ渡される。
     * ViewModel が破棄された後のイベント処理は保証しない。
     *
     * @param event View で発生した UI イベント。
     */
    fun sendEvent(event: E) {
        viewModelScope.launch(start = CoroutineStart.UNDISPATCHED) {
            eventChannel.send(event)
        }
    }

    /**
     * UI イベントの意味解釈を行う。
     *
     * 派生 ViewModel は本メソッドを実装し、イベントに応じて UseCase 呼び出し、
     * dispatch、emitEffect を行う。
     *
     * @param event 処理対象の UI イベント。
     */
    protected abstract suspend fun handleEvent(event: E)

    /**
     * Message を Reducer に渡し、UI 状態を更新する。
     *
     * State 更新は本メソッドを通じてのみ行う。
     * Reducer は現在の State と Message から新しい State を生成する。
     *
     * @param message State 更新理由を表す Message。
     */
    protected fun dispatch(message: M) {
        _uiState.update { current ->
            reducer.reduce(
                currentState = current,
                message = message
            )
        }
    }

    /**
     * 一度きりの UI 副作用を発行する。
     *
     * Navigation / Dialog / Snackbar など、State に保持すると再実行リスクがある処理を
     * Route へ通知するために利用する。
     * 購読者が存在しない期間のEffectは再生しない。
     *
     * @param effect 発行する UI 副作用。
     */
    protected suspend fun emitEffect(effect: F) {
        _uiEffect.emit(effect)
    }

    /**
     * 現在の UI 状態を取得する。
     *
     * 既存 ViewModel との互換用に関数形式も提供する。
     *
     * @return 現在の UI 状態。
     */
    protected fun currentState(): S {
        return stateValue
    }
}
