package jp.co.nsco.basearchitecture.feature.sample.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.ui.loading.LoadingContent
import jp.co.nsco.basearchitecture.ui.theme.BaseArchitectureTheme

/**
 * Sample History 画面の Stateless Screen。
 *
 * 本 Composable は、SampleHistoryUiState に基づいて
 * 操作履歴一覧を描画し、ユーザー操作を SampleHistoryUiEvent として通知する。
 *
 * ■ 提供する責務
 *   操作履歴一覧の表示
 *   検索入力欄の表示
 *   フィルタ選択UIの表示
 *   エラーメッセージの表示
 *   日付グループごとの履歴表示
 *   空表示メッセージの表示
 *   検索・フィルタ操作の Event 通知
 *
 * ■ 設計上の意図
 *   本 Composable は Stateless Screen として定義し、
 *   ViewModel、NavController、UseCase、Repository を直接扱わない。
 *
 *   画面状態は SampleHistoryUiState として外から受け取り、
 *   ユーザー操作は SampleHistoryUiEvent として外へ通知する。
 *
 *   これにより、状態管理や副作用処理は Route / ViewModel 側に閉じ込め、
 *   Screen は表示責務に集中できる。
 *
 * @param uiState Sample History 画面の UI 状態。
 * @param onEvent ユーザー操作や画面イベントを通知するコールバック。
 * @param modifier 画面全体に適用する Modifier。
 */
@Composable
fun SampleHistoryScreen(
    uiState: SampleHistoryUiState,
    onEvent: (SampleHistoryUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LoadingContent(
        isLoading = uiState.isLoading,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                SampleHistorySearchField(
                    value = uiState.searchQuery,
                    onValueChange = {
                        onEvent(SampleHistoryUiEvent.SearchQueryChanged(it))
                    }
                )
            }

            item {
                SampleHistoryFilterRow(
                    selectedFilter = uiState.selectedFilter,
                    onFilterClick = {
                        onEvent(SampleHistoryUiEvent.FilterSelected(it))
                    }
                )
            }

            uiState.screenErrorMessage?.let { message ->
                item {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            val groupedItems = uiState.visibleItems.groupBy { it.groupLabel }
            groupedItems.forEach { (groupLabel, items) ->
                item {
                    Text(
                        text = groupLabel,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                item {
                    SampleHistoryGroupCard(items = items)
                }
            }

            if (!uiState.isLoading && uiState.visibleItems.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.sample_history_empty),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.sample_history_all_loaded),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * 操作履歴の検索入力欄を表示する。
 *
 * 入力値の保持や検索処理は行わず、入力変更を呼び出し側へ通知する。
 *
 * @param value 現在の検索文字列。
 * @param onValueChange 検索文字列変更時のコールバック。
 * @param modifier 検索欄に適用する Modifier。
 */
@Composable
private fun SampleHistorySearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(text = stringResource(R.string.sample_history_search_placeholder))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(28.dp)
    )
}

/**
 * 操作履歴フィルタの選択UIを表示する。
 *
 * @param selectedFilter 現在選択中のフィルタ。
 * @param onFilterClick フィルタ押下時のコールバック。
 * @param modifier フィルタ行に適用する Modifier。
 */
@Composable
private fun SampleHistoryFilterRow(
    selectedFilter: SampleHistoryFilter,
    onFilterClick: (SampleHistoryFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SampleHistoryFilter.entries.forEach { filter ->
            AssistChip(
                onClick = {
                    onFilterClick(filter)
                },
                label = {
                    Text(text = filter.toDisplayText())
                },
                leadingIcon = if (filter == selectedFilter) {
                    {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else {
                    null
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (filter == selectedFilter) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
                    labelColor = if (filter == selectedFilter) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    leadingIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}

/**
 * 同じ日付グループに属する履歴一覧を Card として表示する。
 *
 * @param items 同一グループ内の履歴Item一覧。
 * @param modifier Cardに適用する Modifier。
 */
@Composable
private fun SampleHistoryGroupCard(
    items: List<SampleHistoryItemUiState>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            items.forEachIndexed { index, item ->
                SampleHistoryRow(item = item)

                if (index != items.lastIndex) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    }
}

/**
 * 操作履歴1件分の行を表示する。
 *
 * エラー履歴の場合は背景色と文字色をエラー用に切り替える。
 *
 * @param item 表示対象の履歴Item。
 * @param modifier 行に適用する Modifier。
 */
@Composable
private fun SampleHistoryRow(
    item: SampleHistoryItemUiState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                if (item.isError) {
                    MaterialTheme.colorScheme.errorContainer
                } else {
                    MaterialTheme.colorScheme.surface
                }
            )
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SampleHistoryIcon(
            filter = item.filter,
            isError = item.isError
        )

        Spacer(modifier = Modifier.width(18.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (item.isError) {
                    MaterialTheme.colorScheme.onErrorContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = if (item.isError) {
                    MaterialTheme.colorScheme.onErrorContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = item.timeText,
            style = MaterialTheme.typography.bodyMedium,
            color = if (item.isError) {
                MaterialTheme.colorScheme.onErrorContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

/**
 * 操作履歴行のアイコンを表示する。
 *
 * エラー履歴の場合はエラーアイコンを優先し、
 * それ以外はフィルタ種別に対応するアイコンを表示する。
 *
 * @param filter 履歴Itemのフィルタ分類。
 * @param isError エラー表示対象かどうか。
 * @param modifier アイコン領域に適用する Modifier。
 */
@Composable
private fun SampleHistoryIcon(
    filter: SampleHistoryFilter,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    val icon = if (isError) {
        Icons.Outlined.ErrorOutline
    } else {
        filter.toIcon()
    }
    val tint = if (isError) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.primary
    }
    val background = if (isError) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }

    Box(
        modifier = modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(30.dp)
        )
    }
}

/**
 * SampleHistoryFilter を表示文言へ変換する。
 *
 * @return フィルタ表示文言。
 */
@Composable
private fun SampleHistoryFilter.toDisplayText(): String {
    return when (this) {
        SampleHistoryFilter.All -> stringResource(R.string.sample_history_filter_all)
        SampleHistoryFilter.Action -> stringResource(R.string.sample_history_filter_action)
        SampleHistoryFilter.Setting -> stringResource(R.string.sample_history_filter_setting)
        SampleHistoryFilter.Error -> stringResource(R.string.sample_history_filter_error)
    }
}

/**
 * SampleHistoryFilter を表示アイコンへ変換する。
 *
 * @return フィルタに対応するアイコン。
 */
private fun SampleHistoryFilter.toIcon(): ImageVector {
    return when (this) {
        SampleHistoryFilter.All -> Icons.Outlined.TouchApp
        SampleHistoryFilter.Action -> Icons.Outlined.TouchApp
        SampleHistoryFilter.Setting -> Icons.Outlined.Settings
        SampleHistoryFilter.Error -> Icons.Outlined.ErrorOutline
    }
}

/**
 * Sample History 画面の Preview。
 *
 * 通常表示状態のレイアウト確認に利用する。
 */
@Preview(showBackground = true)
@Composable
private fun SampleHistoryScreenPreview_Normal() {
    BaseArchitectureTheme {
        SampleHistoryScreen(
            uiState = SampleHistoryUiState(
                isLoading = false,
                searchQuery = "",
                selectedFilter = SampleHistoryFilter.All,
                items = listOf(
                    SampleHistoryItemUiState(
                        id = 1L,
                        title = "設定を変更しました",
                        summary = "通知設定を ON にしました",
                        timeText = "10:15",
                        groupLabel = "今日",
                        filter = SampleHistoryFilter.Setting,
                        isError = false
                    ),
                    SampleHistoryItemUiState(
                        id = 2L,
                        title = "Home画面を表示しました",
                        summary = "ダッシュボードを表示",
                        timeText = "09:42",
                        groupLabel = "今日",
                        filter = SampleHistoryFilter.Action,
                        isError = false
                    ),
                    SampleHistoryItemUiState(
                        id = 3L,
                        title = "エラーが発生しました",
                        summary = "保存処理に失敗しました",
                        timeText = "09:10",
                        groupLabel = "今日",
                        filter = SampleHistoryFilter.Error,
                        isError = true
                    )
                ),
                screenErrorMessage = null
            ),
            onEvent = {}
        )
    }
}