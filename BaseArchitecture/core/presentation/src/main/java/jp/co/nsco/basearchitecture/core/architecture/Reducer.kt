package jp.co.nsco.basearchitecture.core.architecture

/**
 * UI 状態を更新するための Reducer 契約。
 *
 * Reducer は、現在の State と Message を受け取り、
 * 次に表示すべき State を生成する責務を持つ。
 *
 * ■ 提供する責務
 *   Message に基づく State 更新
 *   状態遷移ルールの集約
 *   副作用を持たない状態計算
 *
 * ■ 設計上の意図
 *   State 更新処理を ViewModel から分離することで、
 *   画面状態の変更理由を Message として明確化する。
 *
 *   Reducer は純粋関数として扱い、
 *   API 呼び出し、DB アクセス、Navigation、Dialog 表示などの副作用を持たない。
 *
 * @param S 更新対象の UI 状態の型。
 * @param M State 更新理由を表す Message の型。
 */
fun interface Reducer<S : UiState, M : UiMessage> {

    /**
     * 現在の State と Message から次の State を生成する。
     *
     * @param currentState 現在の UI 状態。
     * @param message State 更新理由を表す Message。
     * @return Message を反映した次の UI 状態。
     */
    fun reduce(
        currentState: S,
        message: M
    ): S
}