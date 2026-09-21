package jp.co.nsco.basearchitecture.feature.sample.presentation.history

import jp.co.nsco.basearchitecture.core.architecture.UiState

/**
 * Sample History 画面の UI 状態。
 *
 * SampleHistoryUiState は、操作履歴画面を描画するために必要な
 * 状態を保持する。
 *
 * ■ 提供する責務
 *   読み込み中状態の保持
 *   検索文字列の保持
 *   選択中フィルタの保持
 *   履歴Item一覧の保持
 *   画面表示用エラーメッセージの保持
 *   検索・フィルタ適用後の表示Item一覧の算出
 *
 * ■ 設計上の意図
 *   UI は SampleHistoryUiState の値だけを参照して画面を描画する。
 *
 *   検索文字列やフィルタ選択状態も State に含めることで、
 *   画面表示条件を一元管理する。
 *
 *   Navigation や Dialog 表示などの一回性の副作用は、
 *   SampleHistoryUiEffect として分離し、本 State には含めない。
 *
 * @property isLoading 読み込み中かどうか。
 * @property searchQuery 検索文字列。
 * @property selectedFilter 選択中の履歴フィルタ。
 * @property items 取得済みの履歴Item一覧。
 * @property screenErrorMessage 画面上に表示するエラーメッセージ。
 */
data class SampleHistoryUiState(
    val isLoading: Boolean,
    val searchQuery: String,
    val selectedFilter: SampleHistoryFilter,
    val items: List<SampleHistoryItemUiState>,
    val screenErrorMessage: String?
) : UiState {

    /**
     * 現在の検索文字列と選択中フィルタを反映した表示対象Item一覧。
     *
     * ■ 設計上の意図
     *   検索・フィルタ適用後の一覧を Composable 側で都度計算すると、
     *   表示条件が UI 実装に漏れてしまう。
     *
     *   UiState の derived property として定義することで、
     *   Screen は visibleItems を表示するだけでよくなる。
     *
     * ■ 判定条件
     *   selectedFilter が All の場合は全件対象とする。
     *   selectedFilter が Error の場合は、filter が Error のItemだけでなく
     *   isError が true のItemも表示対象とする。
     *   searchQuery が空白でない場合は、title または summary に部分一致するItemのみ表示する。
     */
    val visibleItems: List<SampleHistoryItemUiState>
        get() = items.filter { item ->
            val matchesFilter = selectedFilter == SampleHistoryFilter.All ||
                    item.filter == selectedFilter ||
                    selectedFilter == SampleHistoryFilter.Error && item.isError

            val matchesQuery = searchQuery.isBlank() ||
                    item.title.contains(searchQuery, ignoreCase = true) ||
                    item.summary.contains(searchQuery, ignoreCase = true)

            matchesFilter && matchesQuery
        }

    companion object {

        /**
         * Sample History 画面の初期状態を生成する。
         *
         * 初期表示では操作ログの購読開始前であるため、
         * Loading 状態として扱う。
         *
         * @return 初期状態。
         */
        fun initial(): SampleHistoryUiState {
            return SampleHistoryUiState(
                isLoading = true,
                searchQuery = "",
                selectedFilter = SampleHistoryFilter.All,
                items = emptyList(),
                screenErrorMessage = null
            )
        }
    }
}